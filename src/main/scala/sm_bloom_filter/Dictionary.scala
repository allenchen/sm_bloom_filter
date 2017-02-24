package sm_bloom_filter

import scala.io.Codec
import scala.io.Source

trait Dictionary {
  def checkWord(word: String): Boolean
  def size: Long
}

object Dictionary {
  def approximateDictionary(wordlistPath: String, falsePositiveProbability: Double)(implicit codec: Codec) = {
    new ApproximateDictionary(wordlistPath, falsePositiveProbability)(codec)
  }
}

class ApproximateDictionary(wordlistPath: String, falsePositiveProbability: Double)(implicit codec: Codec) extends Dictionary {
  private val source = Source.fromFile(wordlistPath)(codec)
  private val dictionarySize = source.getLines.size // consumes Source
  private val bloomFilter = BloomFilter.mutable(dictionarySize, falsePositiveProbability)(HashableEntities.HashableString)
  source.reset.getLines.foreach(bloomFilter.insert(_))

  override def checkWord(word: String): Boolean = bloomFilter.contains(word)
  override def size: Long = bloomFilter.size
}
