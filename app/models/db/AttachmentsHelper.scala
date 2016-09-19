package models.db

import java.io.{File, FileInputStream, InputStream}
import javax.inject.Singleton

import com.google.inject.ImplementedBy
import com.mongodb.casbah.commons.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.gridfs.{GridFS, GridFSInputFile}
import models.Attachment
import org.bson.types.ObjectId

import scala.util.{Failure, Success, Try}


@ImplementedBy(classOf[AttachmentsHelperMongo])
trait AttachmentsHelper {
  def save(file: File, fileName: String, contentType: String): Attachment
  def find(id: String): Option[Attachment]
  def delete(id: String): Unit
  def getContent(attachment: Attachment): InputStream
  def all: Seq[Attachment]
}

@Singleton
class AttachmentsHelperMongo extends AttachmentsHelper {


  val attachments = GridFS(CasbahFactory.mongoDatabase)

  def save(file: File, fileName: String, contentType: String): Attachment = {
    val fileInputStream= new FileInputStream(file)
    val gridFsInputFile: GridFSInputFile = attachments.createFile(fileInputStream)
    gridFsInputFile.filename = fileName
    gridFsInputFile.contentType = contentType
    gridFsInputFile.save()
    Attachment(
      id = gridFsInputFile._id.get.toHexString,
      filename = gridFsInputFile.filename.get,
      chunkSize = gridFsInputFile.chunkSize,
      length = gridFsInputFile.length,
      contentType = gridFsInputFile.contentType.get)
  }

  val partialFunction: PartialFunction[DBObject, Attachment] = new PartialFunction[DBObject, Attachment] {
    override def isDefinedAt(dBObject: DBObject): Boolean = {
      //noinspection ComparingUnrelatedTypes
      dBObject.get("_id").isInstanceOf[ObjectId] &&
        dBObject.get("filename").isInstanceOf[String] &&
        dBObject.get("chunkSize").isInstanceOf[Long] &&
        dBObject.get("length").isInstanceOf[Long] &&
        dBObject.get("contentType").isInstanceOf[String]
    }

    override def apply(dBObject: DBObject): Attachment = Attachment(
      id = dBObject.get("_id").asInstanceOf[ObjectId].toHexString,
      filename = dBObject.get("filename").asInstanceOf[String],
      chunkSize = dBObject.get("chunkSize").asInstanceOf[Long],
      length = dBObject.get("length").asInstanceOf[Long],
      contentType = dBObject.get("contentType").asInstanceOf[String]
    )
  }

  override def find(id: String): Option[Attachment] = {
    Try{
      new ObjectId(id)
    } match {
      case Failure(exception) => None
      case Success(objectId) =>
        val query = MongoDBObject("_id" -> objectId)
        attachments.find(query).collectFirst[Attachment](partialFunction)
    }
  }

  override def getContent(attachment: Attachment): InputStream = {
    val objectId: ObjectId = new ObjectId(attachment.id) //May trow an error
    attachments.findOne(objectId).get.inputStream
  }

  override def all: Seq[Attachment] = {
    attachments.find(MongoDBObject()).collect(partialFunction)
  }

  override def delete(id: String): Unit = {
    val objectId: ObjectId = new ObjectId(id)
    attachments.remove(objectId)
  }
}