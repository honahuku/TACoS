package views

import play.api.libs.json._

case class SignupResponse(message: String)

object SignupResponse {
  implicit val writes: Writes[SignupResponse] = Json.writes[SignupResponse]
}
