package in.harmeetsingh.fpformax

import scala.io.StdIn._
import scala.util.Random

object Problem extends App {

    println(s"What is your name")
    val name = readLine()

    println("Hello " + name + " welcome to the game!!")

    var exec = true

    while(exec) {
        val num = Random.nextInt(5) + 1

        println("Dear "+ name + ", please guess a number from 1 to 5")

        // This is a partial function, because wile we are passing wrong input
        // instead of int, we are getting errors. So, that's why this program is
        // partially successful
        val guess = readLine().toInt

        if(guess == num) println("Your guess right, "+ name + " !")
        else println("You guessed wrong, "+ name + " ?")

        println("Do you want to continue, "+ name + " ?")

        readLine() match {
            case "y" => exec = true
            case "n" => exec = false

        }
    }
}
