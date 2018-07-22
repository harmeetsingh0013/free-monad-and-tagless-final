package in.harmeetsingh.trampoline.cofree

// http://free.cofree.io/2017/08/24/trampoline/

object Example2 extends App {

    sealed trait TailRec[A] {
        def map[B](f: A => B): TailRec[B] = flatMap(f andThen (Return(_)))
        def flatMap[B](f: A => TailRec[B]): TailRec[B] = FlatMap(this, f)
    }

    final case class Return[A](a: A) extends TailRec[A]
    final case class Suspend[A](resume: () => TailRec[A]) extends TailRec[A]
    final case class FlatMap[A, B](sub: TailRec[A], f: A => TailRec[B]) extends TailRec[B]

    def fac(n: Int): TailRec[Int] = {
        if(n == 0) Return(1)
        else FlatMap[Int, Int](Suspend(() => fac(n - 1)), x => Return(n * x))
    }

    def run[A](tailRec: TailRec[A]): A = tailRec match {
        case Return(value) => value
        case Suspend(r) => run(r())
        case FlatMap(x ,f) => x match {
            case Return(v) => run(f(v))
            case Suspend(r) => run(FlatMap(r(), f))
            case FlatMap(y, g) => run(y.flatMap(g(_) flatMap f))
        }
    }

    val trampoline = fac(500000)
    val result = run(trampoline)
    println(s"Result: $result")
}
