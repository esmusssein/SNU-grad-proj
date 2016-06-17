import argonaut._, Argonaut._
import org.joda.time.DateTime
import org.scalatest.FlatSpec

/**
 * Created by jaykim on 5/7/16.
 */
class VehicleMessageSpec extends FlatSpec {
  "VehicleMessage" should "calculate from a JSON list of two vehicle messages" in {
    val v1 = VehicleMessage("foo", new DateTime(2016, 4, 30, 5, 41, 8), 5, 50)
    val v2 = VehicleMessage("foo", new DateTime(2016, 4, 30, 5, 41, 9), 3, 58)
    assert(VehicleMessage.velocity(v1, v2) == ("foo", 889559.4131564699))     // you can confirm the result value at http://www.movable-type.co.uk/scripts/latlong.html
  }

  it should "get JSON string and return the data structure" in {
    val json = "{\"vehicleId\":\"foo\",\"timestamp\":\"2016-05-07T21:33:34.467+09:00\",\"longitude\":37.4812,\"latitude\":126.948004}"
    assert(Parse.decodeOption[VehicleMessage](json) ==
      Some(VehicleMessage("foo", new DateTime("2016-05-07T12:33:34.467Z"), 37.4812, 126.948004)))
  }
}
