package in.harmeetsingh.stackless


case class State[S, +A](runS: S => (A, S)) {
    def map[B](f: A => B): State[S, B] = {
        State( s => {
            val (a, r1) = runS(s)
            (f(a), r1)
        })
    }

    def flatMap[B](f: A => State[S, B]): State[S, B] = {
        State( s => {
         val (a, r1) = runS(s)
            f(a) runS r1
        })
    }
}

object Problem {

}
