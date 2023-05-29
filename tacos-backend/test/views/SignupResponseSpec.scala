package views

import org.scalatestplus.play.PlaySpec
import play.api.libs.json._

class SignupResponseSpec extends PlaySpec {

  "SignupResponse" should {

    "be serializable to JSON" in {
      val signupResponse = SignupResponse("operation was successful!")
      val json = Json.toJson(signupResponse)

      (json \ "message").as[String] mustBe "operation was successful!"
    }
  }
}
