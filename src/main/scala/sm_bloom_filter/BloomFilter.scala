package sm_bloom_filter

import math.{abs, ceil, log}
import scala.collection.BitSet
import scala.collection.mutable
import scala.util.hashing.MurmurHash3

object BloomFilter {
  def numHashes(fpProb: Double): Int = {
    require(fpProb > 0)

    // From https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions
    ceil(-1 * log(fpProb) / log(2)).toInt
  }

  def bits(numItems: Long, fpProb: Double): Int = {
    require(fpProb > 0)

    // From https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions
    ceil((-1 * numItems * log(fpProb)) / (log(2) * log(2))).toInt
  }

  def immutable[T](numItems: Long, falsePositiveProbability: Double)(implicit hashable: Hashable[T]): BloomFilter[T] = {
    val numHashes = BloomFilter.numHashes(falsePositiveProbability)
    val bits = BloomFilter.bits(numItems, falsePositiveProbability)

    new ImmutableBloomFilter(numHashes, bits, BitSet(bits))(hashable)
  }

  def mutable[T](numItems: Long, falsePositiveProbability: Double)(implicit hashable: Hashable[T]): BloomFilter[T] = {
    val numHashes = BloomFilter.numHashes(falsePositiveProbability)
    val bits = BloomFilter.bits(numItems, falsePositiveProbability)

    new MutableBloomFilter(numHashes, bits)(hashable)
  }
}

trait BloomFilter[T] {
  def insert(item: T): BloomFilter[T]
  def contains(item: T): Boolean

  def hashable: Hashable[T]
  def numHashes: Int
  def bits: Int

  protected def hashIndices(item: T): Seq[Int] = {
    // TODO(achen): Because the default Scala MurmurHash3 is 32bit, the strategy here to
    // map the result of the hash to bits can end up significantly biasing towards the
    // beginning of the array if the size of the bloom filter gets closer to INT_MAX.
    // Changing to a 64-bit or 128-bit hash function can help alleviate this effect.
    (1 to numHashes).map { index =>
      abs(hashable.hash(item, index)) % bits
    }.map(_.toInt)
  }

  override def toString: String = {
    s"${this.getClass}(numHashes=$numHashes, bits=$bits)"
  }
}

case class MutableBloomFilter[T](numHashes: Int, bits: Int)(implicit hashableParam: Hashable[T]) extends BloomFilter[T] {
  val bitSet = mutable.BitSet(bits)

  def hashable = hashableParam

  def insert(item: T): BloomFilter[T] = {
    hashIndices(item).foreach(bitSet.add(_))
    this
  }

  def contains(item: T): Boolean = {
    hashIndices(item).forall(bitSet.contains(_))
  }
}

case class ImmutableBloomFilter[T](numHashes: Int, bits: Int, bitSet: BitSet)(implicit hashableParam: Hashable[T]) extends BloomFilter[T] {

  def hashable = hashableParam

  def insert(item: T): BloomFilter[T] = {
    new ImmutableBloomFilter[T](numHashes, bits, bitSet ++ hashIndices(item))(hashable)
  }

  def contains(item: T): Boolean = {
    hashIndices(item).forall(bitSet.contains(_))
  }
}
