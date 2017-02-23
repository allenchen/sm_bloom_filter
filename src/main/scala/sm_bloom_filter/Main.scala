package sm_bloom_filter

object SmBloomFilter {
  def main(args: Array[String]): Unit = {
    System.out.println("Starting test for Bloom Filter!")
    val bf = BloomFilter.mutable[String](1000L, 0.05)(HashableEntities.HashableString)
    System.out.println(s"$bf")

    val newbf = bf.insert("Hello World!")
      .insert("Goodbye!")
      .insert("ASDF!")
      .insert("Blah!")
      .insert("Bar")

    System.out.println(newbf.contains("Hello World!"))
    System.out.println(newbf.contains("Hello World!!"))
    System.out.println(newbf.contains("Goodbye!"))
    System.out.println(newbf.contains("Gds!"))
    System.out.println(newbf.contains("Blah!"))
    System.out.println(newbf.contains("Blahh!"))
    System.out.println(newbf.contains("Bar"))
    System.out.println(newbf.contains("Bar2"))
  }
}
