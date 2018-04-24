package com.search.cache

import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import com.datastax.spark.connector._
import com.datastax.spark.connector.writer.{TTLOption, WriteConf}

//.set("spark.cassandra.connection.host", "192.168.123.10")
//.set("spark.cassandra.auth.username", "cassandra")
//.set("spark.cassandra.auth.password", "cassandra")

/*class CassandraCacheProvider(
                              sparkContext: SparkContext,
                              keyspaceName: String,
                              tableName: String,
                              isTTL: Boolean = true,
                              ttl: Int = 3600 //1h
                            ) extends CacheProvider {
  override def get[K, V](keys: RDD[K]): RDD[V] = {
    keys.joinWithCassandraTable[V](keyspaceName, tableName).map(_._2)
    //    keys.map(Key[K]).joinWithCassandraTable[V](keyspaceName, tableName)
    //    keys.joinWithCassandraTable[V](keyspaceName, tableName, selectedColumns = SomeColumns("", "")) === keys.joinWithCassandraTable[V](keyspaceName, tableName).select("", "")
    //    keys.joinWithCassandraTable[V](keyspaceName, tableName).on(SomeColumns("key"))
  }

  override def put[T](data: RDD[T]): Unit = {
    if (isTTL)
      data.saveToCassandra(keyspaceName, tableName, writeConf = WriteConf(ttl = TTLOption.constant(ttl)))
    else
      data.saveToCassandra(keyspaceName, tableName)
  }
}

case class Key[T](key: T)*/
