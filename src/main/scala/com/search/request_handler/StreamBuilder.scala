package com.search.request_handler

import model.{DatabaseStorage, KafkaStorage, OffsetStorage}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}

object StreamBuilder {
    def buildStream[K, V](topics: Array[String], kafkaParams: Map[String, Object], offsetStorage: OffsetStorage)
                         (implicit ssc: StreamingContext): InputDStream[ConsumerRecord[K, V]] = {
        offsetStorage match {
            case KafkaStorage => buildFromKafkaOffsets[K, V](topics, kafkaParams)

            case DatabaseStorage => throw new UnsupportedOperationException
        }
    }

    def buildFromKafkaOffsets[K, V](topics: Array[String], kafkaParams: Map[String, Object])
                                   (implicit ssc: StreamingContext): InputDStream[ConsumerRecord[K, V]] = {
        KafkaUtils.createDirectStream[K, V](
            ssc,
            LocationStrategies.PreferConsistent,
            ConsumerStrategies.Subscribe[K, V](topics, kafkaParams)
        )
    }
}
