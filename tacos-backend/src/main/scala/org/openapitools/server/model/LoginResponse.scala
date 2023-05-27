package org.openapitools.server.model


/**
 * @param token  for example: ''null''
*/
final case class LoginResponse (
  token: Option[String] = None
)

