package com.search.request_handler

import com.search.model.RequestType._
import com.search.model.{KafkaStorage, OffsetStorage, Request}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

object RequestHandler {
  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load("app.properties")

    val offsetStorage: OffsetStorage = OffsetStorage.getByName(config.getString("offset.storage"))

    val conf = new SparkConf().setMaster("local[*]").setAppName("handler")
    implicit val ssc: StreamingContext = new StreamingContext(conf, Milliseconds(500))

    val kafkaOriginalParams = Map[String, Object](
      "bootstrap.servers" -> config.getString("kafka.servers"),
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "group.id" -> config.getString("kafka.group"),
      "enable.auto.commit" -> (false: java.lang.Boolean)
    )
    val kafkaParams = offsetStorage match {
      case KafkaStorage => kafkaOriginalParams + ("auto.offset.reset" -> "latest")
      case _ => kafkaOriginalParams
    }

    val topics: Array[String] = config.getString("consumer.topics").split(",")

    val stream = StreamBuilder.buildStream[String, String](topics, kafkaParams, offsetStorage)

    stream
      .foreachRDD(rdd => {
        val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
        offsetStorage match {
          case KafkaStorage =>
            //                        simpleProcess[String, String](rdd)
            process(rdd)
            KafkaStorage.saveOffsets(stream, offsetRanges)
          case _ => //transactional block
        }
      })

    ssc.start()
    ssc.awaitTermination()
  }

  def process[K, V <: String](data: RDD[ConsumerRecord[K, V]]): Unit = {
    val mapped: RDD[(K, Request)] = data.map(record => (record.key, Request.fromJson(record.value)))
    val searchRequests = mapped.filter { case (key, request) => request.requestType == Search }
    val putRequests = mapped.filter { case (key, request) => request.requestType == Put }
  }

  def simpleProcess[K, V](data: RDD[ConsumerRecord[K, V]]): Unit = {
    data.map(record => (record.key, record.value))
      .foreach { case (key, value) =>
        println("---------------------------------------------")
        println(s"$key - $value")
        println("---------------------------------------------")
      }
  }
}
