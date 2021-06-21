package petstore

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.circe.generic.auto._
import io.circe.parser._
import org.openapitools.server.model.Pet

import scala.util.Random

class PetApiSimulationExample extends Simulation {

  // Eksempel for get pet med omgjøring til case class
  val httpProtocol = http
    .baseUrl("http://localhost/v2")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  val idFeeder = Iterator.continually(Map("pet_id" -> (Random.nextLong(10)+1)))

  val scngetPetById_1 = scenario("getPetByIdSimulation")
    .feed(idFeeder)
    .exec(http("getPetById")
      .httpRequest("GET", "/pet/${pet_id}")
      .check(status.is(200))
      .check(regex(""""id":${pet_id}"""))
    )

  val scngetPetById = scenario("getPetByIdSimulation")
    .feed(idFeeder)
    .exec(http("getPetById")
      .httpRequest("GET", "/pet/${pet_id}")
      .check(status.is(200))
      .check(bodyString.transformWithSession (
        (body, session) =>
          checkPet(body, session("pet_id").as[Long])
      ).is(true))
    )

  def checkPet(body: String, expectedId: Long): Boolean = {
    decode[Pet](body) match {
      case Left(failure) => return false
      case Right(pet) => if (pet.id.get.equals(expectedId)) return true else return false
    }
  }

  setUp(scngetPetById.inject(atOnceUsers(1))).protocols(httpProtocol)

  //Alt under er autogenerert
  val scnaddPet = scenario("addPetSimulation")
    .exec(http("addPet")
      .httpRequest("POST", "/pet")
    )


  val scndeletePet = scenario("deletePetSimulation")
    .exec(http("deletePet")
      .httpRequest("DELETE", "/pet/${petId}")
      .header("api_key", "${api_key}")
    )

  val scnfindPetsByStatus = scenario("findPetsByStatusSimulation")
    .exec(http("findPetsByStatus")
      .httpRequest("GET", "/pet/findByStatus")
      .queryParam("status", "${status}")
    )

  val scnfindPetsByTags = scenario("findPetsByTagsSimulation")
    .exec(http("findPetsByTags")
      .httpRequest("GET", "/pet/findByTags")
      .queryParam("tags", "${tags}")
    )

  val scnupdatePet = scenario("updatePetSimulation")
    .exec(http("updatePet")
      .httpRequest("PUT", "/pet")
    )

  val scnupdatePetWithForm = scenario("updatePetWithFormSimulation")
    .exec(http("updatePetWithForm")
      .httpRequest("POST", "/pet/${petId}")
    )

}
