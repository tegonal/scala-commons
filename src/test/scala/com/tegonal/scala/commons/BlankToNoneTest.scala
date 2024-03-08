/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons


class BlankToNoneTest extends munit.FunSuite {

  private def testData: List[(String, String, Option[String])] = List(
    ("null", null, None),
    ("empty", "", None),
    ("space", " ", None),
    ("tab", "\t", None),
    ("newline", "\n", None),
    ("not only spaces", " f ", Some(" f ")),
    ("not only whitespaces", "\tf\n", Some("\tf\n")),
    ("text", "f", Some("f")),
  )

  testData.foreach { case (description, subject, expected) =>
    test("string: " + description) {
      assertEquals(subject.blankToNone(), expected)
    }
  }

  testData.foreach { case (description, string, expected) =>
    test("charsequence: " + description) {
      val subject: CharSequence = if string == null then null else StringBuilder(string)
      assertEquals(subject.blankToNone(), expected.map(_ => subject))
    }
  }
}
