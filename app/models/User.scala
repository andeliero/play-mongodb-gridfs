package models

import play.api.libs.json.Json

case class User(id: String, name: String, surname: String, email: String, phone: String, nation: String)
object User {
  implicit val formatter = Json.format[User]
}
