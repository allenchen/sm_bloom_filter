package sm_bloom_filter

object SimpleBenchmarks {
  def log(m: String): Unit = System.out.println(m)

  def time(execution: => Unit): Unit = {
    val startTime = System.nanoTime()
    val result = execution
    val endTime = System.nanoTime()
    System.out.println(s"Time Elapsed: ${endTime - startTime}ns")
  }

  def main(args: Array[String]): Unit = {
    //time(TestImmutableBloomFilter(100000L, 0.001)) // way too slow
    time(TestMutableBloomFilter(10000L, 0.001))
    time(TestMutableBloomFilter(100000L, 0.001))
    time(TestMutableBloomFilter(1000000L, 0.001))
    time(TestMutableBloomFilter(1000000L, 0.0001))
  }

  // def TestImmutableBloomFilter[T](items: Long, fpProb: Double): Unit = {
  //   val bf = BloomFilter.immutable[String](items, fpProb)(HashableEntities.HashableString)
  //   TestBloomFilter(bf, items, fpProb)
  // }

  def TestMutableBloomFilter[T](items: Long, fpProb: Double): Unit = {
    val bf = BloomFilter.mutable[String](items, fpProb)(HashableEntities.HashableString)
    TestBloomFilter(bf, items, fpProb)
  }

  def TestBloomFilter(
    bf: BloomFilter[String],
    items: Long,
    fpProb: Double
  ): Unit = {
    log(s"Testing $bf with $items items and $fpProb false positive rate")
    val itemsToInsert = (1 to items.toInt).map { index => s"$index$fpProb" }
    // val populatedBf = itemsToInsert.foldLeft(bf) { (cum, item) =>
    //   cum.insert(item)
    // }
    itemsToInsert.foreach(bf.insert(_))

    require(itemsToInsert.map(bf.contains(_)).forall(_ == true))

    log(s"Bloom filter reports size: ${bf.size}")

    val itemsNotInserted = (1 to items.toInt).map { index => s"XX$index$fpProb" }
    val falsePositives = itemsNotInserted.map(bf.contains(_)).filter(_ == true).size
    val fpObservedRate = falsePositives/itemsNotInserted.size.toDouble
    log(s"False positives found: $falsePositives/$items ($fpObservedRate)")
  }
}
