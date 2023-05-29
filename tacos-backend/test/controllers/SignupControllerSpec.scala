package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import models.SignupRequest
import models.SignupResponse

class SignupControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {
  // テストのスコープ内でSignupRequestのWritesとSignupResponseのReadsを定義し、テストでのJSON変換を可能にする
  implicit val signupRequestWrites: Writes[SignupRequest] = Json.writes[SignupRequest]
  implicit val signupResponseReads: Reads[SignupResponse] = Json.reads[SignupResponse]

  "SignupController POST" should {

    "process a valid signup request" in {
      val controller = new SignupController(stubControllerComponents())
      val signupRequest = SignupRequest("test@example.com", "password123")
      val request = FakeRequest(POST, "/signup").withJsonBody(Json.toJson(signupRequest))
      val signup = controller.signup().apply(request)

      status(signup) mustBe OK
      contentType(signup) mustBe Some("application/json")
      val signupResponse = contentAsJson(signup).as[SignupResponse]
      signupResponse.message mustBe "operation was successful!"
    }

    "return BadRequest for an invalid signup request" in {
      val controller = new SignupController(stubControllerComponents())
      val request = FakeRequest(POST, "/signup").withJsonBody(Json.toJson(""))
      val signup = controller.signup().apply(request)

      status(signup) mustBe BAD_REQUEST
      contentType(signup) mustBe Some("application/json")
      val signupResponse = contentAsJson(signup).as[SignupResponse]
      signupResponse.message mustBe "processing failed"
    }

    // TODO:
    // "reject invalid JSON format by missing closing double quote"
    // "reject invalid JSON format by missing closing curly brace"
  }
}
