package controllers

import javax.inject._
import play.api.mvc._
import models.SignupRequest
import models.SignupResponse
import play.api.libs.json.Json

@Singleton
class SignupController @Inject() (
    val controllerComponents: ControllerComponents
) extends BaseController {

  def signup: Action[AnyContent] = Action {
    implicit request: Request[AnyContent] =>
      val json = request.body.asJson

      json.flatMap(_.asOpt[SignupRequest]) match {
        case Some(signupRequest) =>
          // ターミナルにemailとpasswordを表示
          println(
              s"Email: ${signupRequest.email}, Password: ${signupRequest.password}"
          )
          // ここでsignupRequestを使って何か処理を行う
          Ok(Json.toJson(SignupResponse("operation was successful!")))
        case None =>
          BadRequest(Json.toJson(SignupResponse("processing failed")))
      }
  }
}
