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

// ドメイン層
// クライアントからのメッセージ
case class Ping(msg: String)
// サーバーからのメッセージ
case class Pong(msg: String)
// サーバーから返すエラーメッセージ
case class ErrorMsg(msg: String)

// アプリケーション層
// Pingメッセージの処理
class PingPongService {
  // Pingメッセージを受け取り、適切なPongメッセージを生成する
  def handlePing(ping: Ping): Either[ErrorMsg, Pong] = {
    // msgが"ping"であれば、"pong!"というメッセージを含むPongを返す
    if (ping.msg == "ping") Right(Pong("pong!"))
    // エラーメッセージを含むErrorMsgを返す
    else Left(ErrorMsg("Invalid message!"))
  }
}

// プレゼンテーション層
// エントリーポイント
object PingPongServer extends IOApp {
  val dsl = Http4sDsl[IO]; import dsl._
  // PingPongServiceのインスタンスを生成
  val service = new PingPongService

  // HTTPリクエストを処理するルートを定義する
  // req @ で/を指定
  val pingPongRoutes = HttpRoutes.of[IO] { case req @ POST -> Root =>
    req.attemptAs[Json].value.flatMap {
      // dataにデコードした結果がある
      case Right(data) =>
        // pingオブジェクトとの比較
        data.as[Ping] match {
          case Right(ping) =>
            // デコードが成功した場合、PingPongServiceを使用して適切なレスポンスを生成する
            service.handlePing(ping) match {
              case Right(pong) => Ok(pong.asJson)
              // エラーメッセージを含むJSONを返す
              case Left(errorMsg) => BadRequest(errorMsg.asJson)
            }
          case _ => BadRequest(ErrorMsg("Invalid message!").asJson)
        }
      case Left(_) =>
        BadRequest(ErrorMsg("The request body was malformed.").asJson)
    }
  }

  // サーバー起動
  // ポートと定義したルートを指定
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
