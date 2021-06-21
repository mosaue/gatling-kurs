package loadProfiles

import io.gatling.core.Predef._
import kurs.ViewItemAfterProfiles

import scala.concurrent.duration.DurationInt

class LoadTest extends Simulation{
  private val loadTestScenario =
    scenario("Testing").exec(ViewItemAfterProfiles.viewItemScenario)

  setUp(
    loadTestScenario.inject(
      rampUsersPerSec(0).to(1).during(10.minutes),
      constantUsersPerSec(1).during(20.minutes),
      rampUsersPerSec(1).to(0).during(10.minutes)
    ).protocols(ViewItemAfterProfiles.httpProtocol)
  )
}
