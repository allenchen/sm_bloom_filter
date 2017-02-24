# sm_bloom_filter

This is a simple bloom filter implemented in Scala. For specifics on what a bloom filter is, see: https://en.wikipedia.org/wiki/Bloom_filter

It implements `insert`, `contains` and `size`.

`insert` inserts an element into the bloom filter.
`contains` checks whether or not the element may have been inserted before. It may return false positives, but will never return false negatives.
`size` _approximates_ the number of elements inserted into the bloom filter based on the number of bits set in its underlying array.

Initial testing benchmarks can be run with `sbt run`.