package com.search.producer

import com.natpryce.konfig.*
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import java.util.*

object kafka : PropertyGroup() {
    val servers by stringType
    val acks by stringType
    val retries by intType
    val key by stringType
    val value by stringType
}

object SimpleConsoleProducer {
    val topic = "search-query"

    @JvmStatic
    fun main(args: Array<String>) {
        val config = ConfigurationProperties.fromResource("app.properties")
        val props = Properties()
        props.put("bootstrap.servers", config[kafka.servers])
        props.put("acks", config[kafka.acks])
        props.put("retries", config[kafka.retries])
        props.put("key.serializer", config[kafka.key])
        props.put("value.serializer", config[kafka.value])

        val producer = KafkaProducer<String, String>(props);

        while (true) {
            val str = readLine()
            if (str == "e") break
            producer.send(ProducerRecord <String, String>(topic, str, str))
        }

        producer.close()
    }
}