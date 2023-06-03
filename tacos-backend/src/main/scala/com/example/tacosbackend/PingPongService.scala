package com.example.tacosbackend

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import io.circe.generic.auto._
import org.http4s.circe._

case class Ping(value: String)
case class Pong(value: String)

class PingPongService extends Http4sDsl[IO] {

  implicit val pingDecoder = jsonOf[IO, Ping]
  implicit val pongEncoder = jsonEncoderOf[IO, Pong]

  val service: HttpRoutes[IO] = HttpRoutes.of[IO] {
    case req @ POST -> Root / "ping" =>
      req.as[Ping].flatMap { ping =>
        if (ping.value == "ping") {
          Ok(Pong("pong!"))
        } else {
          BadRequest("Invalid request")
        }
      }
  }
}
