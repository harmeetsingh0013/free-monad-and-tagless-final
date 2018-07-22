package in.harmeetsingh.trampoline.alexeyblog

import scala.annotation.tailrec

object Example extends App {

    @tailrec
    def rec[A](list: List[A]): Boolean = {
        list match {
            case Nil => true
            case x :: xs =>
                println(s"Element $x")
                rec(xs)
        }
    }

    rec((0 to 1000000).toList)
}
