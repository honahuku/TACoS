package com.example.tacosbackend

import cats.effect.{ExitCode, IO, IOApp}

object Main extends IOApp.Simple:
  val run = TacosbackendServer.run[IO]
