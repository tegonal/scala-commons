/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

                                         Version: v0.1.0-SNAPSHOT
##################################*/
package com.tegonal.scala.commons

class AnyOperationsTest extends munit.FunSuite {

  test("pipe") {
    assertEquals(1.pipe(_ + 1), 2)
  }

  test("tap") {
    var i = 1
    2.tap(i = _)
    assertEquals(i, 2)
  }
}
