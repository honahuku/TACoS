package models

import org.scalatestplus.play.PlaySpec
import play.api.libs.json._

class SignupRequestSpec extends PlaySpec {

  "SignupRequest" should {

    "be deserializable from JSON" in {
      val json = Json.parse("""
        {
          "email": "test@example.com",
          "password": "password123"
        }
      """)

      val signupRequest = json.as[SignupRequest]

      signupRequest.email mustBe "test@example.com"
      signupRequest.password mustBe "password123"
    }
  }
}

class SignupResponseSpec extends PlaySpec {

  "SignupResponse" should {

    "be serializable to JSON" in {
      val signupResponse = SignupResponse("operation was successful!")
      val json = Json.toJson(signupResponse)

      (json \ "message").as[String] mustBe "operation was successful!"
    }
  }
}
