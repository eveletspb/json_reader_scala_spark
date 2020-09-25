package com.github.mrpowers.my.cool.project

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession
import org.json4s.jackson.Serialization
import org.apache.spark.{SparkConf, SparkContext}
import org.json4s._
import org.json4s.jackson.JsonMethods.parse


object JsonReader extends App {

  val spark: SparkSession = SparkSession
    .builder()
    .appName(name = "JsonReader")
    .master(master = "local")
    .getOrCreate()

  val sc = SparkContext.getOrCreate()
  implicit val formats: AnyRef with Formats = Serialization.formats(ShortTypeHints(List(classOf[JsonObject])))
  val json_rdd: RDD[String] = sc.textFile("winemag-data-130k-v2.json")
  val currentTime = LocalDateTime.now()
  var i = 1
  json_rdd
    .map(parse(_).extract[JsonObject])
    .foreach { it =>
      println(s"Data $i ${it} ")
      i += 1
    }
  val endTime = LocalDateTime.now()
  val chrono = ChronoUnit.SECONDS.between(currentTime, endTime)
  println(s"Размер полученной коллекции ${json_rdd.count()}")
  println(s"Затраченное время $chrono")


  sc.stop()

}



