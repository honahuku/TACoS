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
// Pingはクライアントからのメッセージを表す
// msgフィールドにはクライアントから送信されるメッセージが含まれる
case class Ping(msg: String)

// Pongはサーバーからのメッセージを表す
// msgフィールドにはサーバーから送信されるメッセージが含まれる
case class Pong(msg: String)

// ErrorMsgはエラーメッセージを表す
// msgフィールドにはエラーメッセージが含まれる
case class ErrorMsg(msg: String)

// アプリケーション層
// PingPongServiceはPingメッセージの処理を担当するクラス
class PingPongService {
  // handlePingメソッドはPingメッセージを受け取り、適切なPongメッセージを生成する
  // メッセージが"ping"であれば、"pong!"というメッセージを含むPongを返す
  // それ以外のメッセージであれば、エラーメッセージを含むErrorMsgを返す
  def handlePing(ping: Ping): Either[ErrorMsg, Pong] = {
    if (ping.msg == "ping") Right(Pong("pong!"))
    else Left(ErrorMsg("Invalid message!"))
  }
}

// プレゼンテーション層
// PingPongServerはサーバーのエントリーポイント
// このオブジェクトはサーバーの設定とルーティングを定義する
object PingPongServer extends IOApp {
  // Http4sのDSLをインポートする
  val dsl = Http4sDsl[IO]; import dsl._

  // PingPongServiceのインスタンスを生成する
  val service = new PingPongService

  // pingPongRoutesはHTTPリクエストを処理するルートを定義する
  // POSTリクエストを"/"エンドポイントで受け取り、リクエストボディに含まれるJSONをデコードする
  // デコードが成功した場合、PingPongServiceを使用して適切なレスポンスを生成する
  val pingPongRoutes = HttpRoutes.of[IO] { case req @ POST -> Root =>
    req.decode[Json] { data =>
      data.as[Ping] match {
        case Right(ping) =>
          service.handlePing(ping) match {
            case Right(pong)    => Ok(pong.asJson)
            case Left(errorMsg) => BadRequest(errorMsg.asJson)
          }
        case _ => BadRequest(ErrorMsg("Invalid message!").asJson)
      }
    }
  }

  // runメソッドでサーバーを起動する
  // ポートを指定してサーバーを起動し、定義したルートを使用する
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
