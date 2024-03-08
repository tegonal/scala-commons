/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

class CharSequenceExtensionsTest extends munit.FunSuite {

  private val testDataIsEmpty: List[(String, String, Boolean)] = List(
    ("empty", "", true),
    ("space", " ", false),
    ("tab", "\t", false),
    ("newline", "\n", false),
    ("not only spaces", " f ", false),
    ("not only whitespaces", "\tf\n", false),
    ("text", "f", false)
  )

  testDataIsEmpty.foreach { case (description, subject, expected) =>
    test(s"isEmpty on $description returns $expected") {
      assertEquals((subject: CharSequence).isEmpty, expected)
    }
  }

  testDataIsEmpty.foreach { case (description, subject, expectIsEmpty) =>
    val expected = expectIsEmpty.not
    test(s"nonEmpty on $description returns $expected") {
      assertEquals(subject.nonEmpty, expected)
    }
  }

  private val testDataIsBlank: List[(String, String, Boolean)] = List(
    ("empty", "", true),
    ("space", " ", true),
    ("tab", "\t", true),
    ("newline", "\n", true),
    ("not only spaces", " f ", false),
    ("not only whitespaces", "\tf\n", false),
    ("text", "f", false)
  )

  testDataIsBlank.foreach { case (description, subject, expected) =>
    test(s"isBlank on $description returns $expected") {
      assertEquals((subject: CharSequence).isBlank, expected)
    }
  }

  testDataIsBlank.foreach { case (description, subject, expectIsBlank) =>
    val expected = expectIsBlank.not
    test(s"nonBlank on $description returns $expected") {
      assertEquals(subject.nonBlank, expected)
    }
  }
}
