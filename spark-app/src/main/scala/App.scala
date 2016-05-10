import argonaut._, Argonaut._
import org.apache.spark.streaming._
import org.apache.spark.streaming.dstream.DStream
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf

import scala.collection.immutable.HashMap

import VehicleMessage._

object App extends App {
  val Array(zkQuorum) = args
  val sparkConf = new SparkConf().setAppName("App")
  val ssc = new StreamingContext(sparkConf, Seconds(1))
  ssc.checkpoint("checkpoint")

  val velocities: DStream[(String, Double)] = KafkaUtils.createStream(ssc, zkQuorum, "spark", HashMap(("vehicle", 1)))
    .flatMap { case (_, v) => Parse.decodeOption[List[VehicleMessage]](v) }
    .flatMap { as => for (v1 <- as.lift(0); v2 <- as.lift(1)) yield (v1, v2) }
    .map { case (v1, v2) => velocity(v1, v2) }
  velocities.print()

  ssc.start()
  ssc.awaitTermination()
}
