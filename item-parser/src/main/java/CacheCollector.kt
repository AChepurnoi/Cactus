import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

object CacheCollector {

  @JvmStatic
  fun main(args: Array<String>) {
    val mapper = ObjectMapper().registerKotlinModule()
    val items = (29..43) //Cache count
            .map { File("cache-$it.json") }
            .flatMap { mapper.readValue<List<Item>>(it, object : TypeReference<List<Item>>(){})}
    val content = mapper.writeValueAsString(items)
    File("datav3/items.json").printWriter().use { it.println(content) }
  }

}