package com.example.tacosbackend

import cats.effect.Concurrent
import cats.implicits._
import io.circe.generic.auto._
import org.http4s.{EntityDecoder, EntityEncoder, HttpRoutes}
import org.http4s.dsl.Http4sDsl
import org.http4s.circe._

case class Message(content: String)

object SampleService {
  implicit def apply[F[_]: Concurrent]: SampleService[F] = new SampleService[F]

  class SampleService[F[_]: Concurrent] extends Http4sDsl[F] {
    implicit val messageEncoder: EntityEncoder[F, Message] =
      jsonEncoderOf[F, Message]
    implicit val messageDecoder: EntityDecoder[F, Message] = jsonOf[F, Message]

    val service: HttpRoutes[F] = HttpRoutes.of[F] {
      case GET -> Root / "welcome" =>
        Ok(Message("welcome"))

      case req @ POST -> Root / "echo" =>
        req.as[Message].flatMap { message =>
          Ok(message)
        }
    }
  }
}
