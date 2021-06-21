package kurs

import io.gatling.core.structure.{ChainBuilder, ScenarioBuilder}
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import objects.ShopBase

import java.util.concurrent.TimeUnit
import scala.util.Random

class ViewItem extends Simulation {
  val httpProtocol = http
    .baseUrl("https://www.demoblaze.com")
    .inferHtmlResources()
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36")

  //val feeder = csv("feeder/feeder.csv").circular

  val feeder = Iterator.continually(Map("id" -> Random.nextInt(10), "category" -> "phone"))

  def viewItemScenario: ScenarioBuilder = {
    scenario("View Item")
      .exec(ShopBase.getStartPage)
      .feed(feeder)
      .pause("10", TimeUnit.SECONDS)
      .exec(ShopBase.getItem)
      .doIfOrElse(session => session("response").asOption[String].isDefined) {
        exec(ShopBase.printResponse)
      } {
       feed(feeder)
         .exec(ShopBase.getItem)
      }
  }

  setUp(viewItemScenario.inject(atOnceUsers(3))).protocols(httpProtocol)

}
