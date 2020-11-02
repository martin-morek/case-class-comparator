package me.martinmorek

import org.scalatest.flatspec.AnyFlatSpec

class ComparatorTest extends AnyFlatSpec {

  case class Address(street: String, houseNumber: Int, city: String)
      extends Decomposable

  case class Person(firstName: String,
                    lastName: String,
                    age: Int,
                    address: Address)
      extends Decomposable

  case class Group(name: String, leader: Person) extends Decomposable

  val address: Address = Address("Main street", 230, "Boston")
  val person: Person = Person("Jack", "Smith", 24, address)
  val group: Group = Group("Development", person)

  "compare" should "return error if comparing different case classes" in {
    case class FakeAddress(street: String, houseNumber: Int, city: String)
        extends Decomposable

    val fakeAddress = FakeAddress("Main street", 230, "Boston")

    val result = Comparator.compare(address, fakeAddress)

    assert(result.isLeft)
    assert(result.merge.isInstanceOf[CompareError])
  }

  "compare" should "return empty list of differences if case classes are equals" in {
    val duplicateAddress = Address("Main street", 230, "Boston")

    val result = Comparator.compare(address, duplicateAddress)

    assert(result.isRight)
    assert(result match {
      case Right(value) => value.isEmpty
      case _            => false
    })
  }

  "compare" should "return list of properties which are different" in {
    val anotherAddress = address.copy(city = "New York")
    val anotherPerson = person.copy(address = anotherAddress)
    val anotherGroup: Group = Group("Finance", anotherPerson)

    val result = Comparator.compare(group, anotherGroup)

    assert(result.isRight)
    assert(result match {
      case Right(value) =>
        value == List(
          Difference("Group.name", "Development", "Finance"),
          Difference("Group.leader.address.city", "Boston", "New York")
        )
      case _ => false
    })
  }

  "compare" should "return recognize if element is missing" in {
    val anotherPerson = person.copy(firstName = "Jill", address = null)
    val anotherGroup: Group = Group("Finance", anotherPerson)

    val result =
      Comparator.compare(group, anotherGroup)

    assert(result.isRight)
    assert(result match {
      case Right(value) =>
        value == List(
          Difference("Group.name", "Development", "Finance"),
          Difference("Group.leader.firstName", "Jack", "Jill"),
          Difference("Group.leader.address", null, "Unexpected value")
        )
      case _ => false
    })
  }

}
