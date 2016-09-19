package models.db

import javax.inject.Singleton
import com.google.inject.ImplementedBy
import com.mongodb.casbah.MongoCollection
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import form.UserForm
import models.User
import org.bson.types.ObjectId
import scala.util.{Failure, Success, Try}

@ImplementedBy(classOf[UsersHelperMongo])
trait UsersHelper {
  def find(id: String): Option[User]
  def save(user: UserForm): User
}

@Singleton
class UsersHelperMongo extends UsersHelper {
  val users: MongoCollection = CasbahFactory.mongoDatabase("users")

  val partialFunction: PartialFunction[DBObject, User] = new PartialFunction[DBObject, User] {
    override def isDefinedAt(dBObject: DBObject): Boolean = {
      //noinspection ComparingUnrelatedTypes
      dBObject.get("_id").isInstanceOf[ObjectId] &&
        dBObject.get("name").isInstanceOf[String] &&
        dBObject.get("surname").isInstanceOf[String] &&
        dBObject.get("email").isInstanceOf[String] &&
        dBObject.get("phone").isInstanceOf[String] &&
        dBObject.get("nation").isInstanceOf[String]
    }

    override def apply(dBObject: DBObject): User = User(
      id = dBObject.get("_id").asInstanceOf[ObjectId].toHexString,
      name = dBObject.get("name").asInstanceOf[String],
      surname = dBObject.get("surname").asInstanceOf[String],
      email = dBObject.get("email").asInstanceOf[String],
      phone = dBObject.get("phone").asInstanceOf[String],
      nation = dBObject.get("nation").asInstanceOf[String]
    )
  }

  override def find(id: String): Option[User] = {
    Try{
      new ObjectId(id)
    } match {
      case Failure(exception) => None
      case Success(objectId) =>
        val query = MongoDBObject("_id" -> objectId)
        users.find(query).collectFirst[User](partialFunction)
    }
  }

  override def save(user: UserForm): User = {
    val objectId = ObjectId.get()
    val mongoDBObject = MongoDBObject(
      "_id" -> objectId,
      "name" -> user.name,
      "surname" -> user.surname,
      "email" -> user.email,
      "phone" -> user.phone,
      "nation" -> user.nation
    )
    users.insert(mongoDBObject)
    User(
      id = objectId.toString,
      name = user.name,
      surname = user.surname,
      email = user.email,
      phone = user.phone,
      nation = user.nation
    )
  }
}