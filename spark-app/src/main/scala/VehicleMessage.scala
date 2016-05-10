import argonaut._, Argonaut._
import org.joda.time.DateTime

/**
 * Created by jaykim on 5/7/16.
 */
case class VehicleMessage(id: String, dt: DateTime, longitude: Double, latitude: Double)

object VehicleMessage {
  implicit def codecJson: CodecJson[VehicleMessage] =
    CodecJson(
      (v: VehicleMessage) =>
        ("vehicleId" := v.id) ->:
        ("timestamp" := v.dt.toString) ->:
        ("longitude" := v.longitude) ->:
        ("latitude" := v.latitude) ->:
        jEmptyObject,
      c => for {
        id <- (c --\ "vehicleId").as[String]
        dt <- (c --\ "timestamp").as[String] map { s => new DateTime(s) }
        lg <- (c --\ "longitude").as[Double]
        lt <- (c --\ "latitude").as[Double]
      } yield VehicleMessage(id, dt, lg, lt))

  def velocity(v1: VehicleMessage, v2: VehicleMessage): (String, Double) = {
    def deg2rad(deg: Double): Double = deg * (Math.PI / 180)
    val R = 6371000     // radius of the Earth in meter
    val dLat = deg2rad(v2.latitude - v1.latitude)
    val dLon = deg2rad(v2.longitude - v2.longitude)
    val a = Math.sin(dLat/2) * Math.sin(dLat/2) +
      Math.cos(deg2rad(v1.latitude)) * Math.cos(deg2rad(v2.latitude)) * Math.sin(dLon/2) * Math.sin(dLon/2)
    val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
    val dt = (v2.dt.getMillis - v1.dt.getMillis) / 1000
    (v1.id, Math.abs(R * c / dt))
  }
}
