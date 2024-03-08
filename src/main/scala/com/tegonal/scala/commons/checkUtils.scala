/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under European Union Public License v. 1.2
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

/**
 * Requires the given `predicate` to be `true` and throws a [[java.lang.IllegalArgumentException]] otherwise with the given
 * `errorMessage`.
 *
 * @since 0.1.0
 */
inline def require(predicate: Boolean, inline errorMessage: => String): Unit =
  if (predicate.not) throw IllegalArgumentException(errorMessage)

/**
 * Requires the given `predicate` to be `true` and throws a [[java.lang.IllegalStateException]] otherwise with the given
 * `errorMessage`.
 *
 * @since 0.1.0
 */
inline def check(predicate: Boolean, inline errorMessage: => String): Unit =
  failIf(predicate.not, errorMessage)

/**
 * Throws an [[java.lang.IllegalStateException]] with the given `errorMessage` if the given `predicate` is `true`.
 *
 * @since 0.1.0
 */
inline def failIf(predicate: Boolean, inline errorMessage: => String): Unit =
  if (predicate) throw IllegalStateException(errorMessage)
