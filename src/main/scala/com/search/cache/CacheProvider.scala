package com.search.cache

/* TODO: few types of cache:
  - cache all search results with TTL (~ 1h)
  - cache all search results with dropping after new Put
  - update all search results with new results after Put (slooooow)
*/
trait CacheProvider {
  def get(search: String): String
}

object CacheProvider {
  def getCacheProvider(cacheType: String): CacheProvider = ???
}
