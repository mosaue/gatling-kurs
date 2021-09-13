package kurs

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:8080")
		.inferHtmlResources()
		.disableFollowRedirect
		.userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36")

	val headers_9 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9",
		"Accept-Encoding" -> "gzip, deflate, br",
		"Accept-Language" -> "nb-NO,nb;q=0.9,no;q=0.8,nn;q=0.7,en-US;q=0.6,en;q=0.5",
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"Upgrade-Insecure-Requests" -> "1",
		"sec-ch-ua" -> """Google Chrome";v="93", " Not;A Brand";v="99", "Chromium";v="93""",
		"sec-ch-ua-mobile" -> "?0",
		"sec-ch-ua-platform" -> "macOS")

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

	val scn = scenario("RecordedSimulation")
		.exec(http("request_9")
			.get("/bank")
			.headers(headers_9))
		.pause(50)
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
			.formParam("emailAddress", "aa@bb.test")
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
		.pause(43)
		.exec(http("request_84")
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


	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}