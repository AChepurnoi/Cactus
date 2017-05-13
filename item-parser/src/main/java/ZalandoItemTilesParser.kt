import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

object ZalandoItemTilesParser {

  @JvmStatic
  fun main(args: Array<String>) {
    val mapper = ObjectMapper().registerKotlinModule()

    val leaves = mapper.readValue<List<Category>>(File("leaf-categories.json"), object : TypeReference<List<Category>>(){})
    val list = Collections.synchronizedList(ArrayList<Tile>())
    println(leaves.size)

    val counter = AtomicInteger(leaves.size)
    leaves.forEach {
      async(CommonPool) {
        val tiles = ItemTileParser.parseCategory(it)
        list.addAll(tiles)
        val left = counter.decrementAndGet()
        println("Counter left $left")
      }
    }

    while (counter.get() > 0) {
      Thread.sleep(2000)
      println("Waiting for tiles...")
    }

    val content = mapper.writeValueAsString(list)
    File("tiles.json").printWriter().use { it.println(content) }

  }

}