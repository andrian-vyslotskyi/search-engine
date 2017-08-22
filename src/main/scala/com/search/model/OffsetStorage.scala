package com.search.model

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, OffsetRange}

sealed trait OffsetStorage

object OffsetStorage {
  def getByName(name: String): KafkaStorage.type =
    name match {
      case "kafka" => KafkaStorage
      case _ => throw new IllegalArgumentException
    }
}

object KafkaStorage extends OffsetStorage {
  def saveOffsets[K, V](stream: InputDStream[ConsumerRecord[K, V]], offsetRanges: Array[OffsetRange]): Unit = {
    stream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
  }
}

object DatabaseStorage extends OffsetStorage
