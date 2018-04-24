package com.search.cache

import java.util.Properties

import org.apache.spark.rdd.RDD

/* TODO: few types of cache:
  - cache all search results with TTL (~ 1h)
  - cache all search results with dropping after new Put
  - update all search results with new results after Put (slooooow)
*/
trait CacheProvider {
  def get[K, V](keys: RDD[K]): RDD[V]
  def put[T](data: RDD[T])
}

object CacheProvider {
  def getCacheProvider(properties: Properties): CacheProvider = ???
}
