package me.martinmorek

trait Decomposer {
  def decompose[A](a: Any): DecomposedInput[Any]
}

object Decomposer extends Decomposer {

  override def decompose[A](a: Any): DecomposedInput[Any] = {
    def run(a: Any, path: String): DecomposedInput[Any] = {
      a match {
        case value: Decomposable =>
          CaseClass(
            value.productIterator.toList
              .zip(value.productElementNames.toList)
              .map { case (elm, name) => run(elm, s"$path.$name") }
          )
        case value => Element(value, s"$path")
      }
    }
    run(a, a.getClass.getSimpleName)
  }

}
