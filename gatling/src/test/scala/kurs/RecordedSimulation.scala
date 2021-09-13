package kurs

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class RecordedSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://localhost:8080")
    .inferHtmlResources()
    .disableFollowRedirect
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36")


  val scn = scenario("RecordedSimulation")
  exec(
    StartPage.getStartPage
  ).pause(30)
    .exec(
      User.postPersonalDataForm
    ).pause(30)
    .exec(
			User.postAddressDataForm
		)


  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}