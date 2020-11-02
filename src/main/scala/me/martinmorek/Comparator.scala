package me.martinmorek

import me.martinmorek.CompareError.DifferentCaseClass
import me.martinmorek.Decomposer._

import scala.reflect.ClassTag

trait Comparator {
  type CompareResult = Either[CompareError, List[Difference]]

  def compare(a: Decomposable, b: Decomposable): CompareResult
}

object Comparator extends Comparator {

  def compare(a: Decomposable, b: Decomposable): CompareResult = {
    if (isSameType(a, b)) {

      def run(decomposedA: DecomposedInput[Any],
              decomposedB: DecomposedInput[Any],
              diffs: List[Difference]): List[Difference] =
        (decomposedA, decomposedB) match {
          case (CaseClass(valA), CaseClass(valB)) =>
            valA.zip(valB).flatMap {
              case (a, b) => run(a, b, diffs)
            }
          case (Element(elm1, path1), Element(elm2, _)) =>
            if (elm1 != elm2) {
              Difference(path1, elm1, elm2) :: diffs
            } else diffs
          case (_, Element(elm, path)) =>
            Difference(path, elm, "Unexpected value") :: diffs
          case (Element(elm, path), _) =>
            Difference(path, elm, "Unexpected value") :: diffs
        }

      val differences = run(decompose(a), decompose(b), List())
      Right(differences)
    } else
      Left(DifferentCaseClass)
  }

  private def isSameType(a: Any, b: Any): Boolean = {
    val B = ClassTag(b.getClass)

    ClassTag(a.getClass) match {
      case B => true
      case _ => false
    }
  }

}
