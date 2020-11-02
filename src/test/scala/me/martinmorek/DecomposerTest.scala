package me.martinmorek

import org.scalatest.flatspec.AnyFlatSpec
import Decomposer._

class DecomposerTest extends AnyFlatSpec {

  case class Simple(i: Int) extends Decomposable
  case class Person(name: String, props: Simple) extends Decomposable

  val simple = Simple(10)
  val person = Person("John", simple)

  "decompose" should "decompose simple case class to structured output" in {
    val result = decompose(simple)
    assert(result == CaseClass(List(Element(10, "Simple.i"))))
  }

  "decompose" should "decompose nested case class to structured output" in {
    val result = decompose(person)
    assert(
      result ==
        CaseClass(
          List(
            Element("John", "Person.name"),
            CaseClass(List(Element(10, "Person.props.i")))
          )
        )
    )
  }
}
