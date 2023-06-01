package com.example.tacosbackend

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.{Encoder, Decoder}
import io.circe.syntax._

case class Message(message: String)

object SampleService {

  implicit val messageDecoder: Decoder[Message] = Decoder.forProduct1("message")(Message.apply)
  implicit val messageEncoder: Encoder[Message] = Encoder.forProduct1("message")(m => m.message)

  implicit val messageEntityDecoder: EntityDecoder[IO, Message] = jsonOf[IO, Message]
  implicit val messageEntityEncoder: EntityEncoder[IO, Message] = jsonEncoderOf[IO, Message]

  val service = HttpRoutes.of[IO] {
    case req @ POST -> Root =>
      req.attemptAs[Message].value.flatMap {
        case Right(msg) if msg.message == "hello" =>
          Ok(Message("welcome").asJson)
        case _ =>
          BadRequest("Invalid message")
      }
  }
}
