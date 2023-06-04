package com.example.tacosbackend

import cats.effect._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.circe._
import io.circe.generic.auto._
import io.circe.Json
import io.circe.syntax._
import scala.concurrent.ExecutionContext

case class Ping(msg: String)
case class Pong(msg: String)

object PingPongServer extends IOApp {
  val dsl = Http4sDsl[IO]; import dsl._

  val pingPongRoutes = HttpRoutes.of[IO] {
    case req @ POST -> Root =>
      req.decode[Json] { data =>
        data.as[Ping] match {
          case Right(ping) if ping.msg == "ping" => Ok(Pong("pong!").asJson)
          case _ => BadRequest("Invalid message!")
        }
      }
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val pingPongApp = pingPongRoutes.orNotFound

    BlazeServerBuilder[IO](ExecutionContext.global)
      .bindHttp(8080, "localhost")
      .withHttpApp(pingPongApp)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
