package kurs

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import scala.util.Random

object User {
  val headers_59 = Map(
    "Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
    "Accept-Encoding" -> "gzip, deflate, br",
    "Accept-Language" -> "nb-NO,nb;q=0.9,no;q=0.8,nn;q=0.7,en-US;q=0.6,en;q=0.5",
    "Cache-Control" -> "max-age=0",
    "Origin" -> "http://localhost:8080",
    "Sec-Fetch-Dest" -> "document",
    "Sec-Fetch-Mode" -> "navigate",
    "Sec-Fetch-Site" -> "same-origin",
    "Sec-Fetch-User" -> "?1",
    "Upgrade-Insecure-Requests" -> "1",
    "sec-ch-ua" -> """Google Chrome";v="93", " Not;A Brand";v="99", "Chromium";v="93""",
    "sec-ch-ua-mobile" -> "?0",
    "sec-ch-ua-platform" -> "macOS")

  val emailFeeder = Iterator.continually(Map("email" -> (Random.alphanumeric.take(20).mkString + "@foo.com")))

  val postPersonalDataForm: ChainBuilder = {
    feed(emailFeeder)
    .exec(
      http("request_59")
        .post("/bank/signup")
        .headers(headers_59)
        .formParam("title", "Mr.")
        .formParam("firstName", "ms")
        .formParam("lastName", "sa")
        .formParam("gender", "M")
        .formParam("dob", "12/31/2000")
        .formParam("ssn", "123-45-6789")
        .formParam("emailAddress", "${email}")
        .formParam("password", "12345678_Aa")
        .formParam("address", "")
        .formParam("locality", "")
        .formParam("region", "")
        .formParam("postalCode", "")
        .formParam("country", "")
        .formParam("homePhone", "")
        .formParam("mobilePhone", "")
        .formParam("workPhone", "")
        .formParam("username", "")
        .check(status.is(200))
        .check(header("Connection").is("keep-alive"))
    )
  }

  val postAddressDataForm: ChainBuilder =
    exec(http("request_84")
      .post("/bank/register")
      .headers(headers_59)
      .formParam("address", "123 sdfsdf")
      .formParam("locality", "sdfdsf")
      .formParam("region", "sd")
      .formParam("postalCode", "12345")
      .formParam("country", "NO")
      .formParam("homePhone", "805570-5490")
      .formParam("mobilePhone", "805570-5490")
      .formParam("workPhone", "")
      .formParam("agree-terms", "on")
      .formParam("title", "Mr.")
      .formParam("firstName", "ms")
      .formParam("lastName", "sa")
      .formParam("gender", "M")
      .formParam("ssn", "123-45-6789")
      .formParam("dob", "12/31/2000")
      .formParam("emailAddress", "aa@bb.test")
      .formParam("username", "aa@bb.test")
      .formParam("password", "12345678_Aa")
    )

}
