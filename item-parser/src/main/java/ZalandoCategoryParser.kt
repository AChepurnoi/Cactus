import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import java.io.File

object ZalandoCategoryParser {

  val mapper = ObjectMapper().registerKotlinModule()

  @JvmStatic
  fun main(args: Array<String>) {
    val root = Category("https://www.zalando.co.uk/womens-clothing/", "Women clothes", null)
    val leaves = CategoryLeavesParser.parseCategories(root)
    val content = mapper.writeValueAsString(leaves)
    File("leaf-categories.json").printWriter().use { it.println(content) }

  }

}