object TypeClasses {
  /**
    * Returns an integer whose sign communicates how the first parameter
    * compares to the second parameter.
    *
    * The result sign has the following meaning:
    *  - Negative if the first parameter is less than the second parameter
    *  - Positive if the first parameter is greater than the second parameter
    *  - Zero otherwise
    */
  val compareRationals: (Rational, Rational) => Int = (first, second) => {
    /* Rationals should be reduced already, so... */

    val first_extended = first.numer * second.denom
    val second_extended = second.numer * first.denom
     if (first_extended < second_extended) {
       -1
    }
     else if (first_extended > second_extended) {
       1
     }
     else 0
  }

  implicit val rationalOrder: Ordering[Rational] =
    (x: Rational, y: Rational) => compareRationals(x, y)

  def main(args: Array[String]): Unit = {
    val half      = new Rational(1, 2)
    val third     = new Rational(1, 3)
    val fourth    = new Rational(1, 4)
    val rationals = List(third, half, fourth)
    println(Sorting.insertionSort(rationals) == List(fourth, third, half))
  }

}