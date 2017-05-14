import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

object DataCollector {
  val mapper = ObjectMapper().registerKotlinModule()
  const val folder = "datav2"

  @JvmStatic
  fun main(args: Array<String>) {
    mergeData()
//    collectDataV2();


  }

  private fun mergeData(){
    val dataTwo = mapper.readValue<List<Item>>(File("final/final.json"), object: TypeReference<List<Item>>(){})
    val dataOne = mapper.readValue<List<Item>>(File("final/data2.json"), object: TypeReference<List<Item>>(){})
    val total = dataOne + dataTwo
    val content = mapper.writeValueAsString(total)
    File("final/complete.json").printWriter().use { it.println(content) }



  }

  private fun collectDataV1V3(){
    val itemOne = mapper.readValue<List<Item>>(File("datav1/items.json"), object: TypeReference<List<Item>>(){})
    val itemThree = mapper.readValue<List<Item>>(File("datav3/items.json"), object: TypeReference<List<Item>>(){})
    val total = itemOne + itemThree
    val content = mapper.writeValueAsString(total)
    File("final/data13.json").printWriter().use { it.println(content) }
  }

  private fun collectDataV2(){
    val items = File("v4/items.json").readLines().map { mapper.readValue<ItemV2>(it, object: TypeReference<ItemV2>(){}) }
//    val items =mapper.readValue<ItemV2>(File("v4/items.json"), object: TypeReference<ItemV2>(){})
    val mapped = items.map(ItemV2::convertToItem)
    val content = mapper.writeValueAsString(mapped)
    File("final/data2.json").printWriter().use { it.println(content) }
  }

}