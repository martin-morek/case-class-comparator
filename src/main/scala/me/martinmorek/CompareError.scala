package me.martinmorek

object CompareError extends CompareError

sealed trait CompareError {

  case object DifferentCaseClass extends CompareError
}
