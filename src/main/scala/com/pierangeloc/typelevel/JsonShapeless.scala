package com.pierangeloc.typelevel

/**
  *
  * scala-intro - 27/07/16
  * Created with ♥ in Amsterdam
  */
object JsonShapeless {
  import argonaut._, Argonaut._, ArgonautShapeless._
  case class CC(i: Int, s: String)
  // encoding
  val encode = EncodeJson.of[CC]

  val json = encode(CC(2, "a"))
  json.nospaces == """{"i":2,"s":"a"}"""

  // decoding
  val decode = DecodeJson.of[CC]

  val result = decode.decodeJson(json)
}
