package com.example.tacosbackend

import cats.effect.{Async, IO, IOApp}
import cats.syntax.all._
import com.comcast.ip4s._
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger

object TacosbackendServer extends IOApp.Simple {

  def run: IO[Unit] = {
    for {
      client <- EmberClientBuilder.default[IO].build
      helloWorldAlg = HelloWorld.impl[IO]
      jokeAlg = Jokes.impl[IO](client)
      sampleAlg = SampleService[IO]

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract segments not checked
      // in the underlying routes.
      httpApp = (
        TacosbackendRoutes.helloWorldRoutes[IO](helloWorldAlg) <+>
        TacosbackendRoutes.jokeRoutes[IO](jokeAlg) <+>
        TacosbackendRoutes.sampleRoutes[IO](sampleAlg)
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      _ <- EmberServerBuilder.default[IO]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalHttpApp)
        .build
        .useForever
    } yield ()
  }
}
