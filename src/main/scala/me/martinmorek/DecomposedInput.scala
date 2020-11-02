package me.martinmorek

sealed trait DecomposedInput[+A]

case class Element[A](value: A, path: String) extends DecomposedInput[A]

case class CaseClass[A](c: List[DecomposedInput[A]]) extends DecomposedInput[A]
