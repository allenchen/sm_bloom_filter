# sm_bloom_filter

This is a simple bloom filter implemented in Scala. For specifics on what a Bloom filter is, see: https://en.wikipedia.org/wiki/Bloom_filter

The class is initialized with `BloomFilter.mutable(numItems, falsePositiveProbability)`. `numItems` is the number of items expected to be inserted into the Bloom filter, and falsePositiveProbability is the acceptable false positive probability rate. A Bloom filter (backed by a bit array) will be initialized with an optimal array size and # of hash functions according to https://en.wikipedia.org/wiki/Bloom_filter#Optimal_number_of_hash_functions.

The Bloom filter supports very basic functionality: `insert`, `contains` and `size`.

`insert` inserts an element into the Bloom filter.

`contains` checks whether or not the element may have been inserted before. It may return false positives, but will never return false negatives.

`size` _approximates_ the number of elements inserted into the Bloom filter based on the number of bits set in its underlying array.

Initial testing benchmarks can be run with `sbt run`, selecting sm_bloom_filter.SimpleBenchmarks.
A toy example exercising the bloom filter as a Dictionary can be found by selecting sm_bloom_filter.DictionaryTest.