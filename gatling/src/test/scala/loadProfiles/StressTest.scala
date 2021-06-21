package loadProfiles

import io.gatling.core.Predef._
import kurs.ViewItemAfterProfiles

import scala.concurrent.duration.DurationInt

class StressTest extends Simulation{
  private val loadTestScenario =
    scenario("Testing").exec(ViewItemAfterProfiles.viewItemScenario)

  setUp(
    loadTestScenario.inject(
      rampUsersPerSec(0).to(1).during(5.minutes),
      constantUsersPerSec(1).during(5.minutes),
      rampUsersPerSec(1).to(2).during(5.minutes),
      constantUsersPerSec(2).during(5.minutes),
      rampUsersPerSec(2).to(3).during(5.minutes),
      constantUsersPerSec(3).during(5.minutes)
    ).protocols(ViewItemAfterProfiles.httpProtocol)
  )
}
