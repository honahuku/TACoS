package models

import play.api.libs.json._

case class SignupRequest(email: String, password: String)

object SignupRequest {
  implicit val reads: Reads[SignupRequest] = Json.reads[SignupRequest]
}
