/*package controllers


import java.nio.file.{Files, Paths}

import org.junit.runner._
import org.specs2.concurrent.ExecutionEnv
import org.specs2.runner._
import play.api.http.{HeaderNames, Writeable}
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import play.api.mvc.{AnyContentAsMultipartFormData, Codec, MultipartFormData}
import play.api.test._

object MultipartFormDataWritable {
  val boundary = "--------ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"

  def formatDataParts(data: Map[String, Seq[String]]) = {
    val dataParts = data.flatMap { case (key, values) =>
      values.map { value =>
        val name = s""""$key""""
        s"--$boundary\r\n${HeaderNames.CONTENT_DISPOSITION}: form-data; name=$name\r\n\r\n$value\r\n"
      }
    }.mkString("")
    Codec.utf_8.encode(dataParts)
  }

  def filePartHeader(file: FilePart[TemporaryFile]) = {
    val name = s""""${file.key}""""
    val filename = s""""${file.filename}""""
    val contentType = file.contentType.map { ct =>
      s"${HeaderNames.CONTENT_TYPE}: $ct\r\n"
    }.getOrElse("")
    Codec.utf_8.encode(s"--$boundary\r\n${HeaderNames.CONTENT_DISPOSITION}: form-data; name=$name; filename=$filename\r\n$contentType\r\n")
  }

  val singleton = Writeable[MultipartFormData[TemporaryFile]](
    transform = { form: MultipartFormData[TemporaryFile] =>
      formatDataParts(form.dataParts) ++
        form.files.flatMap { file =>
          val fileBytes = Files.readAllBytes(Paths.get(file.ref.file.getAbsolutePath))
          filePartHeader(file) ++ fileBytes ++ Codec.utf_8.encode("\r\n")
        } ++
        Codec.utf_8.encode(s"--$boundary--")
    },
    contentType = Some(s"multipart/form-data; boundary=$boundary")
  )
}

@RunWith(classOf[JUnitRunner])
class Controllers$Test(implicit ee: ExecutionEnv) extends PlaySpecification {

  //lazy val appBuilder = new GuiceApplicationBuilder()
  //lazy val injector = appBuilder.injector()
  //lazy val bookHelper = injector.instanceOf[BooksHelperMongo]
  //lazy val attachmentsHelper = injector.instanceOf[AttachmentsHelperMongo]

  implicit val anyContentAsMultipartFormWritable: Writeable[AnyContentAsMultipartFormData] = {
    MultipartFormDataWritable.singleton.map(_.mdf)
  }

  "Controller$Test" should {
    //TODO finire
    "insert new Attachment" in new WithApplication {
      //val inputStream: InputStream = this.getClass.getResourceAsStream("Bici_molle.jpg")
      /*val uri: URI = this.getClass.getResource("/Bici_molle.jpg").toURI
      val file: File = new File(uri)
      val temporaryFile = TemporaryFile(file)
      val filePart: MultipartFormData.FilePart[TemporaryFile] = MultipartFormData.FilePart("uploadFile", file.getName, Option("asd/foo"), temporaryFile)
      val seqFileParts: Seq[FilePart[TemporaryFile]] = Seq[FilePart[TemporaryFile]](filePart)
      val multipartFormData: MultipartFormData[TemporaryFile] = MultipartFormData(Map[String, Seq[String]](), seqFileParts, Seq[BadPart]())
      val resultOpt: Option[Future[play.api.mvc.Result]] = route(app, FakeRequest(routes.AttachmentsRepo.postAttachment()).withMultipartFormDataBody(multipartFormData))(anyContentAsMultipartFormWritable)
      val result = resultOpt.get
      status(result) must be_==(OK)CREATED*/
      ok
    }

    /*"insert new Attachment" in new WithServer {
      val uri: URI = this.getClass.getResource("/Bici_molle.jpg").toURI
      val file: File = new File(uri)
      val temporaryFile = TemporaryFile(file)
      val filePart: MultipartFormData.FilePart[TemporaryFile] = MultipartFormData.FilePart("uploadFile", file.getName, Option("asd/foo"), temporaryFile)
      val seqFileParts: Seq[FilePart[TemporaryFile]] = Seq[FilePart[TemporaryFile]](filePart)
      val multipartFormData: MultipartFormData[TemporaryFile] = MultipartFormData(Map[String, Seq[String]](), seqFileParts, Seq[BadPart]())
      val futureWSResponse: Future[WSResponse] = WsTestClient.wsCall(routes.AttachmentsRepo.postAttachment()).post(body = multipartFormData)
      val response = await(futureWSResponse)
      response.status mustEqual OK CREATED
    }*/
  }

}
*/