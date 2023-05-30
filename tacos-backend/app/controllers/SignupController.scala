package controllers

import javax.inject._
import play.api.mvc._
import models.SignupRequest
import models.SignupResponse
import play.api.libs.json.Json
import views.html.defaultpages.badRequest
import play.api.libs.json._

@Singleton
class SignupController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  def signup: Action[SignupRequest] = Action(parse.json[SignupRequest]) {
    request =>
      println(
        s"Email: ${request.body.email}, Password: ${request.body.password}"
      )
      // ここでsignupRequestを使って何か処理を行う
      Ok(Json.toJson(SignupResponse("operation was successful!")))
  }
}
