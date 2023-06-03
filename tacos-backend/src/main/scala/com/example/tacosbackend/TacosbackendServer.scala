package com.example.tacosbackend

import cats.effect.{IO, IOApp}
import org.http4s.server.middleware.Logger
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._

object TacosbackendServer extends IOApp.Simple {

  def run: IO[Unit] = {
    val sampleServiceInstance = SampleService[IO]
    val httpApp = TacosbackendRoutes.sampleRoutes(sampleServiceInstance).orNotFound
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    EmberServerBuilder.default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(finalHttpApp)
      .build
      .use(_ => IO.never)
  }
}
