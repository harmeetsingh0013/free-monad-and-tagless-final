package in.harmeetsingh.trampoline.alexeyblog

object Example1 extends App {

    def even[A](list: List[A]): Boolean = {
        list match {
            case Nil => true
            case x :: xs =>
                println(s"Even $x")
                odd(xs)
        }
    }

    def odd[A](list: List[A]): Boolean = {
        list match {
            case Nil => false
            case x :: xs =>
                println(s"Odd $x")
                even(xs)
        }
    }

    even((0 to 1000000).toList)
}
