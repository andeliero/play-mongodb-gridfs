package models.db

import java.io.{File, InputStream}

import models.Attachment
import org.specs2.mutable.{BeforeAfter, Specification}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.test.{PlaySpecification, WithApplication}

import scala.io.Source

class AttachmentsHelperMongoTest extends PlaySpecification {

  lazy val appBuilder = new GuiceApplicationBuilder()
  lazy val injector = appBuilder.injector()
  lazy val attachmentsHelper = injector.instanceOf[AttachmentsHelperMongo]

  trait Context extends BeforeAfter {

    def after: Any = {
      attachmentsHelper.attachments.filesCollection.drop()
      CasbahFactory.mongoDatabase("fs.chunks").drop()
    }
    def before: Any = {
      attachmentsHelper.attachments.filesCollection.drop()
      CasbahFactory.mongoDatabase("fs.chunks").drop()
    }
  }

  trait PresetContext extends Context {
    var id = "change it!"
    val fileName = "Absolut-Louisiana.jpg"
    val contentType = "image/jpeg"

    override def before: Any = {
      super.before
      val file: File = new File(this.getClass.getResource("/Absolut-Louisiana.jpg").getFile)
      val inputFile = attachmentsHelper.attachments.createFile(file)
      inputFile.filename = fileName
      inputFile.contentType = contentType
      inputFile.save()
      id = inputFile._id.get.toString
    }
  }

  "AttachmentsHelperMongoTest" should {
    "save a new File in save" in new WithApplication with Context {
      val file: File = new File(this.getClass.getResource("/Bici_molle.jpg").getFile)
      val attachment: Attachment = attachmentsHelper.save(file, file.getName, "image/jpeg")
      attachment.filename must be_==(file.getName)
      attachment.contentType must be_==("image/jpeg")
    }
    "return None in find" in new WithApplication with Context {
      val attachmentOpt = attachmentsHelper.find("hahahahaha")
      attachmentOpt must beNone
    }
    "return Some in find" in new WithApplication with PresetContext {
      val attachmentOpt = attachmentsHelper.find(id = id)
      attachmentOpt must beSome[Attachment]
      val attachment = attachmentOpt.get
      attachment.id must be_==(id)
      attachment.filename must be_==(fileName)
      attachment.contentType must be_==(contentType)
    }
  }
}
