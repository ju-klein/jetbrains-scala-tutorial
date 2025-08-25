object StandardLibrary {

  def smaller (a:Int, b:Int) = a < b  /* no idea how lambda expressions work in Scala */
  val cond: (Int, Int) => Boolean = smaller

      def insert(x: Int, xs: List[Int]): List[Int] =
        xs match {
          case List() => x :: Nil
          case y :: ys =>
            if (cond(x, y)) x :: y :: ys
            else y :: insert(x, ys)
        }

      def main(args: Array[String]): Unit = {
        println(insert(4, 1::8::Nil))
      }

}