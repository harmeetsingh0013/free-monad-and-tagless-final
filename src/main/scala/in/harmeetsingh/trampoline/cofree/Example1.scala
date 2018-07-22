package in.harmeetsingh.trampoline.cofree

object Example1 extends App {

    def unsafeFac(n: Int): Int = {
        if(n == 0) 1
        else n * unsafeFac(n - 1)
    }

    val result = unsafeFac(500000)
    println(s"Resuld: $result")
}
