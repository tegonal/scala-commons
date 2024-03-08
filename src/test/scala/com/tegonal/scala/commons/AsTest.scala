/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

                                         Version: v0.1.0-SNAPSHOT
##################################*/
package com.tegonal.scala.commons

import munit.Location

class AsTest extends munit.FunSuite {

  test("shows receiver object in error message") {
    interceptAndMessageContains[ClassCastException]("expected type scala.Predef.String got java.lang.Integer") {
      val a: Any = 1
      a.as[String]
    }
  }
  test("cast null to reference type doesn't throw") {
    (null: CharSequence).as[String]
  }

  List[Any](1, 1.toByte, 1.toShort, 1.toLong, 1.toFloat).foreach { x =>
    test(s"no conversion from ${x.getClass.getSimpleName} to Double, fails with ClassCastException") {
      intercept[ClassCastException] {
        x.as[Double]
      }
    }
  }

  test("doesn't throw from Double to Double") {
    1.0.as[Double]
  }

  test(s"no unboxing from null to primitive type, fails with ClassCastException") {

    interceptAndMessageContains[ClassCastException]("expected type scala.Byte got `null`") {
      val d: Any = null
      d.as[Byte]
    }

    interceptAndMessageContains[ClassCastException]("expected type scala.Short got `null`") {
      (null: Any).as[Short]
    }

    def foo(): Any = null

    interceptAndMessageContains[ClassCastException]("expected type scala.Int got `null`") {
      foo().as[Int]
    }

    intercept[ClassCastException] {
      (null: Any).as[Long]
    }
    intercept[ClassCastException] {
      (null: Any).as[Float]
    }
    intercept[ClassCastException] {
      (null: Any).as[Double]
    }
    intercept[ClassCastException] {
      (null: Any).as[Char]
    }
    intercept[ClassCastException] {
      (null: Any).as[Boolean]
    }
  }
}
