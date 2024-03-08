/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

                                         Version: v0.1.0-SNAPSHOT
##################################*/
package com.tegonal.scala.commons

extension [T <: CharSequence](self: Option[T]) {

  /**
   * Turns this Option into None if the contained String is blank.
   *
   * @return None if the String is blank otherwise the String as Some.
   * @since 0.1.0
   */
  def blankToNone(): Option[T] = self.flatMap(c => if c.isBlank then None else Some(c))
}

extension [T <: CharSequence](self: T) {

  /**
   * Turns this String into an Option where None is used in case this String is null or blank and Some otherwise.
   *
   * @return None if the String is blank otherwise the String as Some.
   * @since 0.1.0
   */
  def blankToNone(): Option[T] = if self == null || self.isBlank then None else Some(self)
}

extension (self: String) {

  /**
   * Turns this String into an Option where None is used in case this String is null or blank and Some otherwise.
   *
   * @return None if the String is blank otherwise the String as Some.
   * @since 0.1.0
   */
  def blankToNone(): Option[String] = if self == null || self.isBlank then None else Some(self)
}
