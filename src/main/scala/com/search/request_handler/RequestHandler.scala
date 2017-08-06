package com.search.request_handler

import model.KafkaStorage
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.{Milliseconds, StreamingContext}

object RequestHandler {
    def main(args: Array[String]): Unit = {
        val offsetStorage = KafkaStorage //Todo: read from parameters

        val conf = new SparkConf().setMaster("local[*]").setAppName("handler")
        implicit val ssc: StreamingContext = new StreamingContext(conf, Milliseconds(500))

        //Todo: read from parameters
        val kafkaParams = Map[String, Object](
            "bootstrap.servers" -> "localhost:9092,localhost:9093,localhost:9094",
            "key.deserializer" -> classOf[StringDeserializer],
            "value.deserializer" -> classOf[StringDeserializer],
            "group.id" -> "search_spark",
            "auto.offset.reset" -> "latest",
            "enable.auto.commit" -> (false: java.lang.Boolean)
        )

        val topics = Array("search-query")  //Todo: read from parameters

        val stream = StreamBuilder.buildStream[String, String](topics, kafkaParams, offsetStorage)

        stream
            .foreachRDD(rdd => {
                val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
                offsetStorage match {
                    case KafkaStorage =>
                        process(rdd)
                        KafkaStorage.saveOffsets(stream, offsetRanges)
                    case _ =>   //transactional block
                }
            })

        ssc.start()
        ssc.awaitTermination()
    }

    def process[K, V](data: RDD[ConsumerRecord[K, V]]): Unit = {
        data.map(record => (record.key, record.value))
            .foreach { case (key, value) =>
                println("---------------------------------------------")
                println(s"$key - $value")
                println("---------------------------------------------")
            }
    }
}
