package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}


class Application extends Controller {

  val header: List[(String, String)] = List(
    ACCESS_CONTROL_ALLOW_ORIGIN ->"*",
    ACCESS_CONTROL_ALLOW_METHODS ->
      "GET, POST, PUT, DELETE, OPTIONS",
    ACCESS_CONTROL_ALLOW_HEADERS ->
      "Authorization, Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
    ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true",
    ACCESS_CONTROL_MAX_AGE -> (60 * 60 * 24).toString
  )

  def index = Action {
    Ok(Json.obj("status" -> OK, "message" -> "server up")).withHeaders(header :_*)
  }

}
