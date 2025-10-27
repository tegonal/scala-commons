package com.tegonal.scala.commons

extension [T <: Tuple](self: T) {
  def map[This >: this.type <: Tuple, E, R](n: Int, f: Element[This, n.type] => R): Map[This]
}

type Map[T <: Tuple, N <: Int] <: Tuple = N match {
	case S[n1] => T match {
		case EmptyTuple => EmptyTuple

		case x *: xs => Map[xs, n1]
	}
}

type Element[T <: Tuple, N <: Int] <: Tuple = N match {
	case S[n1] => T match {
		case EmptyTuple => EmptyTuple
		case x *: xs => Map[xs, n1]
	}
}
