package objects

import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import objects.json.{Phone, PhoneRequest}

object ShopBase {
  val headers_0 = Map(
    "accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "nb,en-US;q=0.9,en;q=0.8",
    "sec-ch-ua" -> """ Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91""",
    "sec-ch-ua-mobile" -> "?0",
    "sec-fetch-dest" -> "document",
    "sec-fetch-mode" -> "navigate",
    "sec-fetch-site" -> "none",
    "sec-fetch-user" -> "?1",
    "upgrade-insecure-requests" -> "1")

  val headers_44 = Map(
    "accept" -> "*/*",
    "accept-encoding" -> "gzip, deflate, br",
    "accept-language" -> "nb,en-US;q=0.9,en;q=0.8",
    "content-type" -> "application/json",
    "origin" -> "https://www.demoblaze.com",
    "sec-ch-ua" -> """ Not;A Brand";v="99", "Google Chrome";v="91", "Chromium";v="91""",
    "sec-ch-ua-mobile" -> "?0",
    "sec-fetch-dest" -> "empty",
    "sec-fetch-mode" -> "cors",
    "sec-fetch-site" -> "same-site")

  val uri2 = "https://api.demoblaze.com"

  def checkResponseBodyIsPhoneRequest(body: String): Boolean = {
    decode[Phone](body) match {
      case Left(failure) => return false
      case Right(success) => return true
    }
  }

  def checkCategory(body: String, expectedCategory: String): Boolean = {
    decode[Phone](body) match {
      case Left(failure) => return false
      case Right(phone) => if (phone.cat.equals(expectedCategory)) return true else return false
    }
  }

  def getStartPage: ChainBuilder =
    exec(
      http("request_0")
        .get("/")
        .headers(headers_0).disableFollowRedirect
    )

  def getItem: ChainBuilder =
    exec(
      http("request_44")
        .post(uri2 + "/view")
        .headers(headers_44)
        .body(StringBody(PhoneRequest("${id}").asJson.toString()))
        .check(status.is(200))
        .check(substring(""""id":${id}"""))
        .check(regex(""""cat":"${category}""""))
        .check(bodyString.transformWithSession (
          (body, session) =>
            checkCategory(body, session("category").as[String])
        ).is(true))
        .check(bodyString.transform(
          body =>
            checkResponseBodyIsPhoneRequest(body)
        ).is(true))
        .check(bodyString.saveAs("response"))
    )

    def printResponse: ChainBuilder =
      exec {
        session =>
          println(s"""Respons: ${session("response").as[String]}""")
          session
      }

}
