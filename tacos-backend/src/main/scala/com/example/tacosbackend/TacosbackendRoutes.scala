package com.example.tacosbackend

import org.http4s.HttpRoutes

object TacosbackendRoutes {

  def sampleRoutes[F[_]](S: SampleService.SampleService[F]): HttpRoutes[F] = S.service
}
