package sm_bloom_filter

import scala.io.Codec
import scala.io.Source
import scala.util.Random

object DictionaryTest {
  def log(s: String) = { System.out.println(s) }

  def main(args: Array[String]): Unit = {
    log("Running test for Dictionary")
    val wordlistPath = "src/main/resources/wordlist.txt"
    val wordlistCodec = Codec.ISO8859
    val dictionary = Dictionary.approximateDictionary(wordlistPath, 0.001)(wordlistCodec)
    log(s"Approximate Dictionary Size: ${dictionary.size}")

    truePositiveTest(wordlistPath: String, wordlistCodec, dictionary)
    falsePositiveTest(wordlistPath, wordlistCodec, dictionary)
  }

  def truePositiveTest(wordlistPath: String, wordlistCodec: Codec, dictionary: Dictionary): Unit = {
    val wordlistWords = Source.fromFile(wordlistPath)(wordlistCodec).getLines
    wordlistWords.foreach { word =>
      val result = dictionary.checkWord(word)
      require(result)
    }
    log("True Positive Test passed.")
  }

  def falsePositiveTest(wordlistPath: String, wordlistCodec: Codec, dictionary: Dictionary): Unit = {
    val originalDictionary = Source.fromFile(wordlistPath)(wordlistCodec).getLines.toSet

    val alpha = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    def randomAlpha: Stream[Char] = alpha(Random.nextInt(alpha.size)) #:: randomAlpha
    val falsePositives = (1 to 5000).map { _ =>
      val word = (randomAlpha take 5).mkString
      val result = dictionary.checkWord(word)
      if (result && !originalDictionary.contains(word)) {
        1
      } else {
        0
      }
    }.sum

    log(s"False positives found: $falsePositives")
  }
}
