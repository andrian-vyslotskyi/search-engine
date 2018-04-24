package com.search.controller

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.{Directives, Route}
import com.search.model.{Request, RequestType}
import spray.json.{DefaultJsonProtocol, PrettyPrinter}

case class RequestDTO(text: String)

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val printer = PrettyPrinter
  implicit val prettyPrintedItemFormat = jsonFormat1(RequestDTO)
}

/*object RouteService extends Directives with JsonSupport {
  val route: Route = createRoute()

  def createRoute(): Route = {
    path("/") {
      get {
        parameter('text.as[String]) { text =>
          search(text)
          complete()
        }
      } ~
        post {
          entity(as[RequestDTO]) { request =>
            search(request.text)
            complete()
          }
        } ~
        put {
          entity(as[RequestDTO]) { request =>
            save(request.text)
            complete()
          }
        }
    }
  }

  def search(text: String) = {
    Request(
      uuid = UUID.randomUUID().toString,
      requestType = RequestType.Search,
      text = text
    )
  }

  def save(text: String) = {
    Request(
      uuid = UUID.randomUUID().toString,
      requestType = RequestType.Put,
      text = text
    )
  }
}*/
