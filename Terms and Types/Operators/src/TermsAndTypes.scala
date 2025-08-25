object TermsAndTypes {

  def staticTyping(): Range.Inclusive =
    1 to 0  /* deliberately used a rim case to learn about semantics */

  def main(args: Array[String]): Unit = {
    println(staticTyping())
  }
}