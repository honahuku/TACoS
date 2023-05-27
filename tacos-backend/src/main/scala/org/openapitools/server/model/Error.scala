package org.openapitools.server.model


/**
 * @param message  for example: ''null''
*/
final case class Error (
  message: Option[String] = None
)

