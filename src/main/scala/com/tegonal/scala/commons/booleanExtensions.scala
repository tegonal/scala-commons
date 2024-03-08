/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

extension (self: Boolean) {

  /**
   * Negates the receiver object, turning a `true` into a `false` and a `false` into a `true`.
   *
   * @return the negation of the receiver object
   * @since 0.1.0
   */
  inline def not: Boolean = !self
}
