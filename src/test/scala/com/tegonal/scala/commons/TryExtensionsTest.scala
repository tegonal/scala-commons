/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

                                         Version: v0.1.0-SNAPSHOT
##################################*/
package com.tegonal.scala.commons

import scala.util.{Failure, Success, Try}

class TryExtensionsTest extends munit.FunSuite {

  test("mapFailure maps only over failure case of Success") {
    val s = Success[Int](1)
    assert(s.mapFailure(_ => throw IllegalStateException("bla")) eq s, "Success not the same instance")
  }

  test("mapFailure returns mapped result in case of Failure") {
    val failure = Failure[Int](IllegalStateException("old msg"))
      .mapFailure(c => IllegalStateException("new msg", c))
    failure match
      case Failure(exception) => assertEquals(exception.getMessage, "new msg")
      case Success(value)     => fail("was suddenly a success")
  }
}
