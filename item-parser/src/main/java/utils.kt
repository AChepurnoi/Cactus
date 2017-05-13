import java.io.File
import java.net.URL

public fun <T> Sequence<T>.batch(n: Int): Sequence<List<T>> {
  return BatchingSequence(this, n)
}

private class BatchingSequence<T>(val source: Sequence<T>, val batchSize: Int) : Sequence<List<T>> {
  override fun iterator(): Iterator<List<T>> = object : AbstractIterator<List<T>>() {
    val iterate = if (batchSize > 0) source.iterator() else emptyList<T>().iterator()
    override fun computeNext() {
      if (iterate.hasNext()) setNext(iterate.asSequence().take(batchSize).toList())
      else done()
    }
  }
}


fun loadImage(DEST_FOLDER: String, item: Item) {
  println("Loading image: ${item.pic} for item with id ${item.id}")
  val bytes = URL(item.pic).readBytes()
  val name = item.id
  File("$DEST_FOLDER/$name.jpg").writeBytes(bytes)
}

fun loadImage(DEST_FOLDER: String, item: ItemV2) {
  val bytes = URL(item.image_url).readBytes()
  val name = item.id
  File("$DEST_FOLDER/$name.jpg").writeBytes(bytes)
}



fun <T> retry(count: Int = 5, call: () -> T): T {
  var step = 0
  var ex: Exception? = null
  while (step < count)
    try {
      val res = call()
      return res
    } catch (e: Exception) {
      step += 1
      ex = e
      continue
    }
  throw RuntimeException("Retry pool is over. Error " + ex?.toString())
}

fun <T> retryFailover(count: Int = 50, call: () -> T): T? {
  var step = 0
  var ex: Exception? = null
  while (step < count)
    try {
      val res = call()
      return res
    } catch (e: Exception) {
      step += 1
      ex = e
      continue
    }
  println("Skipped retrying. Failed after $count. EX: ${ex?.toString()}")
  return null
}


fun <T> retryOnFail(count: Int = 50, call: () -> T, fail: (e: Exception) -> Unit): T? {
  var step = 0
  var ex: Exception? = null
  while (step < count)
    try {
      val res = call()
      return res
    } catch (e: Exception) {
      step += 1
      ex = e
      continue
    }
  ex?.run(fail)
  return null
}
