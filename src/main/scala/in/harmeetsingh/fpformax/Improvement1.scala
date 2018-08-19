package in.harmeetsingh.fpformax

import scala.io.StdIn.readLine
import scala.util.{Random, Try}

// remove partial functions and make code safe

object Improvement1 extends App {

    def parseInt(input: String): Option[Int] = Try(input.toInt).toOption

    println(s"What is your name")
    val name = readLine()

    println("Hello " + name + " welcome to the game!!")

    var exec = true

    while(exec) {
        val num = Random.nextInt(5) + 1

        println("Dear "+ name + ", please guess a number from 1 to 5")

        val guess = parseInt(readLine())

        guess match {
            case None => println("You didn't enter a number")
            case Some(_) =>
                if(guess == num) println("Your guess right, "+ name + " !")
                else println("You guessed wrong, "+ name + " ?")
        }

        var cont = true

        while(cont) {
           cont = false

            println("Do you want to continue, "+ name + " ?")

            readLine() match {
                case "y" => exec = true
                case "n" => exec = false
                case _ => cont = true
            }
        }
    }
}
