/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache License 2.0
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

import scala.util.{Failure, Try}

extension [T](self: Try[T]) {

  /**
   * Map over the [[Failure]] of this [[Try]].
   *
   * @param f the function which maps the [[Throwable]] of the current Failure to a new [[Throwable]].
   * @return The resulting [[Try]].
   * @since 0.1.0
   */
  inline def mapFailure(f: Throwable => Throwable): Try[T] =
    self match
      case value: Failure[T] => Failure(f(value.exception))
      case _                 => self
}
