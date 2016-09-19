/*package controllers

import org.junit.runner.RunWith
import org.specs2.concurrent.ExecutionEnv
import org.specs2.mutable.Specification
import org.specs2.runner.JUnitRunner
import play.api.libs.json.{JsValue, Json}
import play.api.test.{FakeRequest, PlaySpecification, WithApplication}

import scala.concurrent.Future

@RunWith(classOf[JUnitRunner])
class UserServiceTest (implicit ee: ExecutionEnv) extends PlaySpecification {

  "UserService" should {
    "insert post new User" in new WithApplication {
      val userForm: JsValue = Json.obj(
        "name" -> "Mario",
        "surname" -> "Rossi",
        "email" -> "emeil@example.com",
        "phone" -> "3494934949",
        "nation" -> "Italia"
      )
      val postResult: Future[play.api.mvc.Result] =
        route(app, FakeRequest(routes.UserService.postUser()).withJsonBody(userForm)).get
      status(postResult) must be_==(OK)
      val userJson: JsValue = contentAsJson(postResult)
      val idOpt: Option[String] = (userJson \ "_id").asOpt[String]
      idOpt must beSome[String]
      val id = idOpt.get
      val getResult: Future[play.api.mvc.Result] =
        route(app, FakeRequest(routes.UserService.getUser(id))).get
      status(getResult) must be_==(OK)
      contentAsJson(getResult) must be_==(userJson)
    }
  }

}*/