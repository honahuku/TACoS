package org.openapitools.server.model


/**
 * @param email  for example: ''null''
 * @param password  for example: ''null''
*/
final case class LoginRequest (
  email: String,
  password: String
)

