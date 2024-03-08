/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

extension (self: CharSequence) {

  /**
   * Returns `true` if the receiver object consists of no char, `false` otherwise.
   *
   * @return `true` if non empty, `false` otherwise.
   * @since 0.1.0
   */
  def isEmpty: Boolean = self.length() == 0

  /**
   * Returns true if the receiver object consists of at least 1 char, false otherwise.
   *
   * @return `true` if non empty, `false` otherwise.
   * @since 0.1.0
   */
  def nonEmpty: Boolean = isEmpty.not

  /**
   * Returns `true` if the receiver object [[isEmpty]] as well as all chars are either
   * [[Character.isWhitespace]] or [[Character.isSpaceChar]], `false` otherwise.
   *
   * @return `true` if empty or only whitespace/space bar chars, `false` otherwise.
   * @since 0.1.0
   */
  def isBlank: Boolean = self.length().pipe { length =>
    length == 0 ||
    Range(0, length).forall(
      self.charAt(_).pipe(c => c.isWhitespace || c.isSpaceChar)
    )
  }

  /**
   * Returns `true` if the receiver object contains at least 1 char which is neither
   * [[Character.isWhitespace]] nor [[Character.isSpaceChar]], `false` otherwise.
   *
   * @return `true` if neither empty nor, `false` otherwise.
   * @since 0.1.0
   */
  def nonBlank: Boolean = isBlank.not

  /**
   * Returns `true` if the receiver object is neither null nor [[isEmpty]].
   *
   * @return `true` if neither `null` nor empty, `false` otherwise
   * @since 0.1.0
   */
  def isNotNullNorEmpty: Boolean = self != null && self.nonEmpty

  /**
   * Returns `true` if the receiver object is neither `null` nor [[isBlank]].
   *
   * @return `true` if neither `null` nor blank, `false` otherwise
   * @since 0.1.0
   */
  def isNotNullNorBlank: Boolean = self != null && self.nonBlank

}
