import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

class CacheWriter(val id: Int){

  private val mapper = ObjectMapper().registerKotlinModule()
  private val cacheFile = "cache-$id.json"

  init {
    val content = mapper.writeValueAsString(mutableListOf<Item>())
    File(cacheFile).printWriter().use { it.println(content) }
  }

  fun append(list: List<Item>){
    val saved = mapper.readValue<List<Item>>(File(cacheFile), object : TypeReference<List<Item>>() {}).toMutableList()
    saved.addAll(list)
    val content = mapper.writeValueAsString(saved)
    File(cacheFile).printWriter().use { it.println(content) }
  }

}