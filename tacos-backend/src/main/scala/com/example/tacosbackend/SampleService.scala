package com.example.tacosbackend

import cats.effect._
import org.http4s.dsl.io._
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.dsl.impl.Responses.BadRequestOps

case class Message(message: String)

object SampleService {
    implicit val messageDecoder = jsonOf[IO, Message]
    implicit val MessageEncoder = jsonEncoderOf[IO, Message]

    val service = HttpRoutes.of[IO] {
        case req @ POST -> Root =>
            req.decode[Message] { msg => 
                if (msg.message == "hello") {
                    Ok(Message("Welcome").asJson)
                } else {
                    BadRequestOps("Invalid message")
                }
            }
    }
}
