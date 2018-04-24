package com.search.model

import com.fasterxml.jackson.core.`type`.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, JsonScalaEnumeration}

object RequestType extends Enumeration {
  type RequestType = Value
  val Search, Put = Value
}

class RequestTypeRef extends TypeReference[RequestType.type]

case class Request(
                    uuid: String,
                    @JsonScalaEnumeration(classOf[RequestTypeRef]) requestType: RequestType.RequestType,
                    text: String
                  )

object Request {
  val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)

  def fromJson(json: String): Request = {
    mapper.readValue[Request](json)
  }
}
