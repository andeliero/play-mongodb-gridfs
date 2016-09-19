package controllers


import javax.inject.Inject

import form.UserForm
import models.User
import models.db.UsersHelper
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.mvc.{Action, Controller}

class UsersControl @Inject()(val userHelper: UsersHelper) extends Controller {

  def postUser = Action { request =>
    request.body.asJson match {
      case None => BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "json not provided"))
      case Some(json) =>
        json.validate[UserForm] match {
          case JsError(errors) => BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "not valid json"))
          case JsSuccess(userForm, path) =>
            val user: User = userHelper.save(userForm)
            Ok(Json.obj("status" -> OK, "user" -> user))
        }
    }
  }

  def getUser(id: String) = Action { request =>
    userHelper.find(id) match {
      case None => NotFound(Json.obj("status" -> NOT_FOUND, "message" -> "user id not found"))
      case Some(user) => Ok(Json.obj("status" -> OK, "user" -> user))
    }
  }

}
