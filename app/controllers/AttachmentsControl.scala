package controllers


import javax.inject.Inject

import akka.stream.IOResult
import akka.stream.scaladsl.{Source, StreamConverters}//FileIO
import akka.util.ByteString
import models.Attachment
import models.db.AttachmentsHelper
import play.api.mvc._
import play.api.libs.json._

import scala.concurrent.Future
import scala.tools.nsc.interpreter.InputStream

class AttachmentsControl @Inject()(attachmentsHelper: AttachmentsHelper) extends Controller {

  val header: List[(String, String)] = List(
      ACCESS_CONTROL_ALLOW_ORIGIN ->"*",
      ACCESS_CONTROL_ALLOW_METHODS ->
        "GET, POST, PUT, DELETE, OPTIONS",
      ACCESS_CONTROL_ALLOW_HEADERS ->
        "Authorization, Accept, Origin, Content-type, X-Json, X-Prototype-Version, X-Requested-With",
      ACCESS_CONTROL_ALLOW_CREDENTIALS -> "true",
      ACCESS_CONTROL_MAX_AGE -> (60 * 60 * 24).toString
  )

  def getAttachments = Action {
    val attachments: Seq[Attachment] = attachmentsHelper.all
    Ok(Json.obj("status" -> OK, "attachments" -> attachments))
  }

  def getAttachment(id: String) = Action {
    attachmentsHelper.find(id) match {
      case None => NotFound(Json.obj("status" -> NOT_FOUND, "message" -> "not valid id"))
      case Some(attachment) => Ok(Json.obj("status" -> OK, "attachment" -> attachment))
    }
  }

  def deleteAttachment(id: String) = Action {
    attachmentsHelper.find(id) match {
      case None => NotFound(Json.obj("status" -> NOT_FOUND, "message" -> "not valid id"))
      case Some(attachment) =>
        attachmentsHelper.delete(attachment.id)
        Ok(Json.obj("status" -> OK, "message" -> "attachment deleted"))
    }
  }

  def getAttachmentContent(id: String) = Action { request =>
    attachmentsHelper.find(id) match {
      case None => NotFound(Json.obj("status" -> NOT_FOUND, "message" -> "not valid id"))
      case Some(attachment) =>
        val inputStream: InputStream = attachmentsHelper.getContent(attachment)
        val source: Source[ByteString, Future[IOResult]] = StreamConverters.fromInputStream(() => inputStream)
        request.getQueryString("inline") match {
          case Some("true") =>
            Ok.chunked(source).withHeaders(header: _*).withHeaders(
              ACCESS_CONTROL_EXPOSE_HEADERS -> CONTENT_DISPOSITION,
              CONTENT_LENGTH -> attachment.length.toString,
              CONTENT_DISPOSITION -> s"""inline; filename="${attachment.filename}" """)
              .as(attachment.contentType)
          case _ =>
            Ok.chunked(source).withHeaders(header: _*).withHeaders(
              ACCESS_CONTROL_EXPOSE_HEADERS -> CONTENT_DISPOSITION,
              CONTENT_LENGTH -> attachment.length.toString,
              CONTENT_DISPOSITION -> s"""attachment; filename="${attachment.filename}" """)
              .as(attachment.contentType)
        }
    }
  }

  def postAttachment = Action { request =>
    request.body.asMultipartFormData.map(_.file("file")) match {
      case None =>
        BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "multipartFormData not provided"))
          .withHeaders(header: _*)
      case Some(maybeFilePart) => maybeFilePart match {
        case None =>
          BadRequest(Json.obj("status" -> BAD_REQUEST, "message" -> "file not provided"))
            .withHeaders(header: _*)
        case Some(filePart) =>
          val attachment: Attachment =
            attachmentsHelper.save(filePart.ref.file, filePart.filename, filePart.contentType.get)
          Created(Json.obj("status" -> CREATED, "attachment" -> attachment)).withHeaders(header: _*)
      }
    }
  }

}
