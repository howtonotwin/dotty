import scala.quoted._
import scala.quoted.matching._

inline def rewrite[T](inline x: Any): Any = ${ stringRewriter('x) }

private def stringRewriter(e: Expr[Any]) with QuoteContext : Expr[Any] =
  StringRewriter.transform(e)

private object StringRewriter extends util.ExprMap {

  def transform[T](e: Expr[T]) with (QuoteContext, Type[T]) : Expr[T] = e match
    case Const(s: String) =>
      Expr(s.reverse) match
        case '{ $x: T } => x
        case _ => e // e had a singlton String type
    case _ => transformChildren(e)

}
