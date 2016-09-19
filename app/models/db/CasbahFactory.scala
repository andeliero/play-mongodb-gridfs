package models.db

import com.mongodb.casbah.MongoClient
import com.typesafe.config.{Config, ConfigFactory}



object CasbahFactory {
  private val conf = ConfigFactory.load()
  val host = conf.getString("mongodb.host")
  val port = conf.getInt("mongodb.port")
  val db = conf.getString("mongodb.db")
  val mongoClient = MongoClient(host, port)
  val mongoDatabase = mongoClient(db)
}
