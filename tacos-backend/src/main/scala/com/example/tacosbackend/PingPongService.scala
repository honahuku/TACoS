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

/**
 * ドメイン層
 * @param msg クライアントからのメッセージ
 */
case class Ping(msg: String)

/**
 * ドメイン層
 * @param msg サーバーからのメッセージ
 */
case class Pong(msg: String)

/**
 * ドメイン層
 * @param msg サーバーから返すエラーメッセージ
 */
case class ErrorMsg(msg: String)

/**
 * アプリケーション層
 * Pingメッセージの処理を担当するクラス
 */
class PingPongService {
  /**
   * Pingメッセージを受け取り、適切なPongメッセージを生成する
   * @param ping Pingメッセージ
   * @return Pongメッセージまたはエラーメッセージ
   */
  def handlePing(ping: Ping): Either[ErrorMsg, Pong] = {
    if (ping.msg == "ping") Right(Pong("pong!"))
    else Left(ErrorMsg("Invalid message!"))
  }
}

/**
 * プレゼンテーション層
 * サーバーのエントリーポイント
 */
object PingPongServer extends IOApp {
  val dsl = Http4sDsl[IO]; import dsl._
  val service = new PingPongService

  /**
   * HTTPリクエストを処理するルートを定義する
   * POSTリクエストを"/"エンドポイントで受け取り、リクエストボディに含まれるJSONをデコードする
   * デコードが成功した場合、PingPongServiceを使用して適切なレスポンスを生成する
   * デコードが失敗した場合、エラーメッセージを含むJSONを返す
   */
  val pingPongRoutes = HttpRoutes.of[IO] { case req @ POST -> Root =>
    req.attemptAs[Json].value.flatMap {
      case Right(data) =>
        data.as[Ping] match {
          case Right(ping) =>
            service.handlePing(ping) match {
              case Right(pong) => Ok(pong.asJson)
              case Left(errorMsg) => BadRequest(errorMsg.asJson)
            }
          case _ => BadRequest(ErrorMsg("Invalid message!").asJson)
        }
      case Left(_) => BadRequest(ErrorMsg("The request body was malformed.").asJson)
    }
  }

  /**
   * サーバーを起動する
   * ポートを指定してサーバーを起動し、定義したルートを使用する
   * @param args コマンドライン引数
   * @return 終了コード
   */
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
