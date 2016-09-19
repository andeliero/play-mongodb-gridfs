package models

import play.api.libs.json.{Format, Json}

case class Attachment(id: String, filename: String, chunkSize: Long, length: Long, contentType: String)
object Attachment {
  implicit val attachmentFormat: Format[Attachment] = Json.format[Attachment]
}
