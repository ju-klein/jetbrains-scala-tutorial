object StandardLibrary {

  def optionMap(x: Option[Int]) : Option[Int]= {
   x.map(x => x + 1)
  }

  def optionFilter(x: Option[Int]): Option[Int] = {
    x.filter(x => x % 2 == 0)
  }

  def optionFlatMap(x: Option[Int]): Option[Int] = {
    x.flatMap(x => Some(x + 1)) /* this was a bug, and my solution read Some(x).
                              However, I believe this was not suficiently specified.
                              I also do not want to hide my mistakes. */
  }

  def main(args: Array[String]): Unit = {
    println(optionMap(Some(7)))
    println(optionMap(None))
    println(optionFilter(Some(5)))
    println(optionFilter(Some(6)))
    println(optionFlatMap(Some(4)))
    println(optionFlatMap(None))
  }

}