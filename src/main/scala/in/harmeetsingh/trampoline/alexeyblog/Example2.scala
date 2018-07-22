package in.harmeetsingh.trampoline.alexeyblog

//https://medium.com/@olxc/trampolining-and-stack-safety-in-scala-d8e86474ddfa

object Example2 extends App {

    sealed trait Trampoline[A]
    case class Done[A](value: A) extends Trampoline[A]
    case class More[A](call: () => Trampoline[A]) extends Trampoline[A]


    def even[A](list: List[A]): Trampoline[Boolean] = {
        list match {
            case Nil => Done(true)
            case x :: xs =>
                println(s"Even : $x")
                More(() => odd(xs))
        }
    }

    def odd[A](list: List[A]): Trampoline[Boolean] = {
        list match {
            case Nil => Done(false)
            case x :: xs =>
                println(s"Odd: $x")
                More(() => even(xs))
        }
    }

    def run[A](trampoline: Trampoline[A]): A = {
        trampoline match {
            case Done(value) => value
            case More(r) => run(r())
        }
    }

    val trampoline = even((0 to 1000000).toList)
    run(trampoline)
}
