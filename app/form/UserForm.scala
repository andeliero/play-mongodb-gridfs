package form

import play.api.libs.json.{Json, Reads}

case class UserForm(name: String, surname: String, email: String, phone: String, nation: String)
object UserForm {
  implicit lazy val reads: Reads[UserForm] = Json.reads[UserForm]
}