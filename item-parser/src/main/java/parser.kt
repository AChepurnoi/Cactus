import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.gargoylesoftware.htmlunit.WebClient
import com.gargoylesoftware.htmlunit.html.HtmlPage
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.EmptyCoroutineContext.plus
import kotlin.coroutines.experimental.buildSequence
import kotlin.reflect.jvm.internal.impl.load.java.lazy.ContextKt


val url = "https://www.zalando.co.uk"
val userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_4) " +
        "AppleWebKit/537.36 (KHTML, like Gecko) " +
        "Chrome/57.0.2987.130 Safari/537.36"




fun getPage(url: String) = Jsoup.connect(url)
        .userAgent(userAgent)
        .get()


object CategoryLeavesParser {
  fun parseCategories(root: Category): List<Category> {
    val leaves = parseCategory(root).flatMap(this::parseCategory)
    return leaves
  }

  private fun parseCategory(target: Category?): List<Category> {
    if (target == null) return emptyList()
    Thread.sleep(300)
    val document = retry { getPage(target.url) }

    val cats = document.select(".isActive + ul.subCat").select("li")
    val linksToName = cats.map {
      val link = it.select("a").attr("href")
      val name = it.select("span").text()
      Category(url + link, name, target)
    }

    println("Parsing ${target.name}")
    linksToName.forEach(::println)

    target.visited = true
    return linksToName
  }

}


object ItemTileParser {

  fun parseCategory(category: Category, limit: Int = 50): List<Tile> {
    var page = 2 //Start from 2 because ?p=1 redirects TODO add first page support
    val tiles = mutableListOf<Tile>()
    var previousList = emptyList<Tile>()

    while (page < limit) {
      Thread.sleep(300)
      val pageURL = category.url + "?p=$page"
      println("Loading $pageURL")
      val document = retry { getPage(pageURL) }

      val tilesList = document
              .select("#catalogItemsListParent")
              .select("li")
              .map { Tile(url + it.select("a").attr("href"), category) }
      if (tilesList == previousList) break
      tiles.addAll(tilesList)
      previousList = tilesList
      page += 1
    }
    return tiles.distinct()
  }
}

object ItemParser {

  fun parseItems(tiles: List<TileData>): List<Item> {
    val driver = ChromeDriver()
    driver.manage().timeouts().pageLoadTimeout(15, TimeUnit.SECONDS)
    println("Started parsing")
    val items = tiles.mapIndexed { index, it ->
      retryFailover {
        if (index % 10 == 0) println("Item parser on item with index $index")
        driver.get(it.url)
        val description = driver.findElementByCssSelector(".zvui-accordion").text.replace("\n", " ")
        val title = driver.findElementByCssSelector(".zvui-main-info").findElement(By.cssSelector(".zvui-product-title")).text.replace("\n", " ")
        val price = driver.findElementByCssSelector(".zvui-main-info").findElement(By.cssSelector(".zvui-product-price")).text.replace("\n", " ")
        val pic = driver.findElementByCssSelector(".gallery_image").getAttribute("src")
        Item(title, description, price, it.category, it.crumbs, pic)
      }
    }.filterNotNull()

    driver.quit()
    return items
  }

}

