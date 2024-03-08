/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache License 2.0
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

class CheckUtilsTest extends munit.FunSuite {

  test("failIf throws if predicate is true") {
    interceptMessage[IllegalStateException]("test") {
      failIf(true, "test")
    }
  }
  test("failIf doesn't throw and neither evaluate message if predicate is false") {
    failIf(false, throw IllegalStateException("bla"))
  }

  test("check throws if predicate is false") {
    interceptMessage[IllegalStateException]("test") {
      check(false, "test")
    }
  }
  test("check doesn't throw and neither evaluate message if predicate is true") {
    check(true, throw IllegalStateException("bla"))
  }

  test("check throws if predicate is false") {
    interceptMessage[IllegalArgumentException]("test") {
      require(false, "test")
    }
  }
  test("check doesn't throw and neither evaluate message if predicate is true") {
    require(true, throw IllegalArgumentException("bla"))
  }
}
