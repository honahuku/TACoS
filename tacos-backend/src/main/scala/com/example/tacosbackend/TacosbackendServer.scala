package com.example.tacosbackend

import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.implicits._

object TacosbackendServer extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(TacosbackendRoutes.routes.orNotFound)
      .resource
      .use(_ => IO.never)
      .as(ExitCode.Success)
  }
}
