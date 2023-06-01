package com.example.tacosbackend

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = TacosbackendServer.run[IO]
}
