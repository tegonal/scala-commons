/*
    __                          __
   / /____ ___ ____  ___  ___ _/ /       This file is provided to you by https://github.com/tegonal/scala-commons
  / __/ -_) _ `/ _ \/ _ \/ _ `/ /        Copyright 2024 Tegonal Genossenschaft <info@tegonal.com>
  \__/\__/\_, /\___/_//_/\_,_/_/         It is licensed under Apache License 2.0
         /___/                           Please report bugs and contribute back your improvements

##################################*/
package com.tegonal.scala.commons

import scala.annotation.nowarn
import scala.quoted.*

extension [T](self: T) {

  /**
   * Applies the given function `f` to the receiver object.
   *
   * Basically the same as the scala.util.chaining.scalaUtilChainingOps, but for sure inlines and since we use it often
   * at Tegonal and we add this library to `-Yimports` we automatically have it in scope.
   *
   * @param f the function to apply to the receiver object.
   * @tparam R the resulting type of the function application
   * @return the result of the function application
   * @since 0.1.0
   */
  inline def pipe[R](inline f: T => R): R = f(self)

  /**
   * Perform some side effect defined in the given function `f` based on this receiver object.
   *
   * Basically the same as the scala.util.chaining.scalaUtilChainingOps, but for sure inlines and since we use it often
   * at Tegonal and we add this library to `-Yimports` we automatically have it in scope.
   *
   * @param f the function which is executed, passing the receiver object as input.
   * @since 0.1.0
   */
  inline def tap(inline f: T => Unit): Unit = f(self)

  /**
   * Casts the receiver object to type [[TSub]], inserting runtime checks where possible emitting a warning otherwise.
   *
   * @tparam TSub the subtype to which we want to cast
   * @return The receiver object cast to [[TSub]]
   * @since 0.1.0
   */
  inline def as[TSub <: T]: T = ${ asImpl[TSub, T]('self) }
}

@nowarn
private def asImpl[T, U >: T](x: Expr[U])(using Type[T], Type[U], Quotes): Expr[T] = {
  import quotes.reflect.TypeRepr

  val sym = TypeRepr.of[T].typeSymbol
  val code = Expr(x.show)

  emitWarningIfUnableToTypeCheckEntirely[T]
  val isPrimitive = Expr(Type.of[T] match {
    case '[Boolean] | '[Char] | '[Byte] | '[Short] | '[Int] | '[Long] | '[Float] | '[Double] => true
    case _                                                                                   => false
  })

  '{
    $x match {
      case t: T => t
      case a =>
        if (a == null) {
          if ($isPrimitive) {
            throw ClassCastException(
              s"expected type ${${ Expr(Type.show[T]) }} got `null`\nUse `asInstanceOf[${${ Expr(Type.show[T]) }}]` if you want to unbox, i.e. use a default value in case of `null`)"
            )
          }
          null.asInstanceOf[T]
        } else {
          throw ClassCastException(
            s"expected type ${${ Expr(Type.show[T]) }} got ${a.getClass.getName}"
          )
        }
    }
  }
}

private def emitWarningIfUnableToTypeCheckEntirely[T](using Type[T], Quotes) = {
  import quotes.reflect.report
  import quotes.reflect.TypeRepr

  val repr = TypeRepr.of[T]
  val sym = repr.typeSymbol
  if (sym.isAbstractType) {
    if sym.isTypeParam then
      report.warning(
        s"unsafe cast, cannot type check against a type parameter (${Type.show[T]}) due to type erasure (can only check against an upper bound if defined)"
      )
    else
      report.warning(
        s"unsafe cast, cannot type check against an abstract type member (${Type.show[T]}) due to type erasure (can only check against an upper bound if defined)"
      )
  } else if (sym.isClassDef && repr.typeArgs.nonEmpty) {
    report.warning(s"unsafe cast, cannot type check against a higher-kinded type (${Type.show[T]}) due to type erasure")
  }
}
