import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.io.File
import java.net.URL
import java.util.concurrent.atomic.AtomicInteger

//Zalando
object ImageLoader {


  const val FOLDER = "final"

  @JvmStatic
  fun main(args: Array<String>) {
    val mapper = ObjectMapper().registerKotlinModule()
    val items = mapper.readValue<List<Item>>(File("$FOLDER/data2.json"), object : TypeReference<List<Item>>() {})
    val counter = AtomicInteger(items.size)
    items.forEach { item ->
      async(CommonPool) {
        retryOnFail(5, { loadImage(FOLDER, item) }, { println("Failed: ${item.id}. Can't load pic: ${item.pic}")})
        counter.decrementAndGet()
      }
    }

    while (counter.get() > 0){
      Thread.sleep(5000)
      println("Counter: ${counter.get()}Loading images...")
    }
  }


}