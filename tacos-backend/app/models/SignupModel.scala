package models

import play.api.libs.json._

case class SignupRequest(email: String, password: String)
case class SignupResponse(message: String)

object SignupRequest {
  implicit val reads: Reads[SignupRequest] = Json.reads[SignupRequest]
}

object SignupResponse {
  implicit val writes: Writes[SignupResponse] = Json.writes[SignupResponse]
}
