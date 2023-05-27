package org.openapitools.server.api

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.marshalling.ToEntityMarshaller
import akka.http.scaladsl.unmarshalling.FromEntityUnmarshaller
import akka.http.scaladsl.unmarshalling.FromStringUnmarshaller
import org.openapitools.server.AkkaHttpHelper._
import org.openapitools.server.model.Error
import org.openapitools.server.model.LoginRequest
import org.openapitools.server.model.LoginResponse
import org.openapitools.server.model.SignupRequest
import org.openapitools.server.model.SignupResponse


class AuthApi(
    authService: AuthApiService,
    authMarshaller: AuthApiMarshaller
) {

  
  import authMarshaller._

  lazy val route: Route =
    path("auth" / "login") { 
      post {  
            entity(as[LoginRequest]){ loginRequest =>
              authService.login(loginRequest = loginRequest)
            }
      }
    } ~
    path("auth" / "signup") { 
      post {  
            entity(as[SignupRequest]){ signupRequest =>
              authService.signup(signupRequest = signupRequest)
            }
      }
    }
}


trait AuthApiService {

  def login200(responseLoginResponse: LoginResponse)(implicit toEntityMarshallerLoginResponse: ToEntityMarshaller[LoginResponse]): Route =
    complete((200, responseLoginResponse))
  def login400(responseError: Error)(implicit toEntityMarshallerError: ToEntityMarshaller[Error]): Route =
    complete((400, responseError))
  /**
   * Code: 200, Message: Successful operation, DataType: LoginResponse
   * Code: 400, Message: Invalid input, DataType: Error
   */
  def login(loginRequest: Option[LoginRequest])
      (implicit toEntityMarshallerLoginResponse: ToEntityMarshaller[LoginResponse], toEntityMarshallerError: ToEntityMarshaller[Error]): Route

  def signup200(responseSignupResponse: SignupResponse)(implicit toEntityMarshallerSignupResponse: ToEntityMarshaller[SignupResponse]): Route =
    complete((200, responseSignupResponse))
  def signup400(responseError: Error)(implicit toEntityMarshallerError: ToEntityMarshaller[Error]): Route =
    complete((400, responseError))
  /**
   * Code: 200, Message: Successful operation, DataType: SignupResponse
   * Code: 400, Message: Invalid input, DataType: Error
   */
  def signup(signupRequest: Option[SignupRequest])
      (implicit toEntityMarshallerSignupResponse: ToEntityMarshaller[SignupResponse], toEntityMarshallerError: ToEntityMarshaller[Error]): Route

}

trait AuthApiMarshaller {
  implicit def fromEntityUnmarshallerSignupRequest: FromEntityUnmarshaller[SignupRequest]

  implicit def fromEntityUnmarshallerLoginRequest: FromEntityUnmarshaller[LoginRequest]



  implicit def toEntityMarshallerLoginResponse: ToEntityMarshaller[LoginResponse]

  implicit def toEntityMarshallerSignupResponse: ToEntityMarshaller[SignupResponse]

  implicit def toEntityMarshallerError: ToEntityMarshaller[Error]

}

