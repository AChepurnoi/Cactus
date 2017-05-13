import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.sun.javafx.runtime.SystemProperties
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.newFixedThreadPoolContext
import org.openqa.selenium.firefox.FirefoxDriver
import java.io.File
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger

object ZalandoItemsParser {


  @JvmStatic
  fun main(args: Array<String>) {
    System.setProperty("webdriver.chrome.driver", "/Users/Sasha/chromedriver")

    val mapper = ObjectMapper().registerKotlinModule()
    val tiles = mapper.readValue<List<TileData>>(File("tiles.json"), object : TypeReference<List<TileData>>() {})

    //Split into 3 buckets by 75 elements
    val batchedItems = tiles.asSequence().batch(100).batch(10).toList()

    val coroutines = Semaphore(3)


    batchedItems.forEachIndexed { index, list ->
      if (index > 28) { // Resume from cache-i file
        coroutines.acquire()
        async(CommonPool) {
          println("Starting processing cache $index")
          val fileWriter = CacheWriter(index)
          list.forEach {
            val parsed = ItemParser.parseItems(it)
            fileWriter.append(parsed)
          }
          println("Finished processing cache $index")
          coroutines.release()
        }
      }
    }
  }

}