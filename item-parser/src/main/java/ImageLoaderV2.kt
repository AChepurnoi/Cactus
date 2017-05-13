import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.io.File
import java.util.concurrent.atomic.AtomicInteger


//Nt Zalando
object ImageLoaderV2 {


  val FOLDER = "datav2"
  @JvmStatic
  fun main(args: Array<String>) {
    val mapper = ObjectMapper().registerKotlinModule()
    val items = File("datav2/items.json").readLines().map { mapper.readValue<ItemV2>(it, ItemV2::class.java) }

//    Form valid json file for future...
//    File("datav2/items-formed.json").printWriter().use { it.println(mapper.writeValueAsString(items))}
    val counter = AtomicInteger(items.size)

    items.forEach { item ->
      async(CommonPool) {
        retryOnFail(5, { loadImage(FOLDER, item) }, { println("Failed: ${item.id}. Can't load pic: ${item.image_url}") })
        counter.decrementAndGet()
      }
    }

    while (counter.get() > 0) {
      Thread.sleep(5000)
      println("Current counter: ${counter.get()}. Loading images...")
    }
  }


}