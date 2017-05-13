import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Category(val url: String, val name: String, val parent: Category? = null, @JsonIgnore var visited: Boolean = false) {

  @JsonProperty("parentName", access = JsonProperty.Access.READ_ONLY)
  fun parentName() = parent?.name ?: "null"

  override fun equals(other: Any?): Boolean {
    if (other is Category) {
      return name == other.name
    } else return super.equals(other)
  }

  @JsonProperty("crumbs", access = JsonProperty.Access.READ_ONLY)
  fun crumbs(): String {
    return (parent?.let { p -> p.crumbs() + " > " } ?: "") + name
  }

  override fun toString(): String {
    return "Category(name='$name', parent=${parent?.name})"
  }

  override fun hashCode(): Int {
    return name.hashCode()
  }
}


data class Tile(val url: String, @JsonIgnore val category: Category) {

  @JsonProperty("category", access = JsonProperty.Access.READ_ONLY)
  fun category() = category.name

  @JsonProperty("crumbs", access = JsonProperty.Access.READ_ONLY)
  fun crumbs() = category.crumbs()
}


data class TileData(val url: String, val category: String, val crumbs: String)


data class Item(val title: String, val description: String, val price: String,
                val category: String,
                val crumbs: String,
                val pic: String,
                val id: String = UUID.randomUUID().toString()) {

  override fun toString(): String {
    return "Item(title='$title'\n, description='$description'\n, price='$price'\n', pic=$pic)"
  }
}

data class ItemV2(val description: String, val title: String, val category: String,
                  val url: String, val image_url: String, val id: Int,
                  val price: Double, val detail: String) {

  fun convertToItem(): Item {
    val last = category.split("|").last()
    val crumbss = category.replace("|", " > ")
    return Item(title = title, description = description + detail, price = price.toString(),
            category = last, crumbs = crumbss, pic = image_url, id = id.toString())

  }

}


