package kurs

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.circe.syntax._
import io.circe.generic.auto._
import io.circe.parser._
import objects.json.{Phone, PhoneRequest}

class RecordedSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("https://www.demoblaze.com")
    .inferHtmlResources()
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36")

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

  val scn = scenario("RecordedSimulation")
    .exec(http("request_0")
      .get("/")
      .headers(headers_0).disableFollowRedirect)
    .exec(
      http("request_44")
        .post(uri2 + "/view")
        .headers(headers_44)
        .body(StringBody(PhoneRequest("1").asJson.toString())) //4
        .check(status.is(200)) //3
        .check(substring(""""id":1""")) //4
        .check(regex(""""cat":"phone"""")) //3
        .check(bodyString.transform(
          body =>
            checkCategory(body, "phone") //4
        ).is(true))
        .check(bodyString.transform(
          body =>
            checkResponseBodyIsPhoneRequest(body) //4
        ).is(true))
        .check(bodyString.saveAs("response"))// 4
    )
    .exec {
      session =>
          println(s"""Respons: ${session("response").as[String]}""") //4
        session
    }

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

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)

}

