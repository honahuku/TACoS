package com.example.tacosbackend

import cats.effect._
import org.http4s._

object TacosbackendRoutes {

  val pingPongService = new PingPongService

  val routes: HttpRoutes[IO] = pingPongService.service
}
