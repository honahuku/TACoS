package controllers

import javax.inject._
import play.api.mvc._
import play.api.libs.json._

@Singleton
class EchoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def echo: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    request.body.asJson.map { json =>
      Ok(json)
    }.getOrElse {
      BadRequest("Expecting application/json request body")
    }
  }
}
