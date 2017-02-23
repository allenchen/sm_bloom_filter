package sm_bloom_filter

import scala.util.hashing.MurmurHash3

trait Hashable[T] {
  def hash(obj: T, seed: Int): Long
}

object HashableEntities {
  object HashableString extends Hashable[String] {
    override def hash(v: String, seed: Int): Long = {
      MurmurHash3.stringHash(v, seed).toLong
    }
  }

  object HashableProduct extends Hashable[Product] {
    override def hash(v: Product, seed: Int): Long = {
      MurmurHash3.productHash(v, seed).toLong
    }
  }

  object HashableOrdered extends Hashable[TraversableOnce[Any]] {
    override def hash(v: TraversableOnce[Any], seed: Int): Long = {
      MurmurHash3.orderedHash(v, seed).toLong
    }
  }
}
