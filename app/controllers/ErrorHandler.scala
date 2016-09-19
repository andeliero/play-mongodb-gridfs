package controllers

import javax.inject.{Inject, Provider, Singleton}

import play.api.http.DefaultHttpErrorHandler
import play.api.http.Status
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import play.api.mvc.Results._
import play.api.routing.Router

import scala.concurrent._

@Singleton
class ErrorHandler @Inject() (
                               env: Environment,
                               config: Configuration,
                               sourceMapper: OptionalSourceMapper,
                               router: Provider[Router]
                             ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  //Useful for angular application
  val header: List[(String, String)] = {
    List(
      "Access-Control-Allow-Origin" -> "*",
      "Access-Control-Allow-Methods" -> "GET, POST, PUT, DELETE, OPTIONS",
      "Access-Control-Allow-Headers" -> "Authorization, Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
      "Access-Control-Allow-Credentials" -> "true",
      "Access-Control-Max-Age" -> (60 * 60 * 24).toString)
  }


  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    Logger.error(s"""${request.method} ${request.uri}""",exception)
    Future.successful(
      InternalServerError(Json.obj("status"-> Status.INTERNAL_SERVER_ERROR,"message"->exception.getMessage))
        .withHeaders(header: _*)
    )
  }

}
