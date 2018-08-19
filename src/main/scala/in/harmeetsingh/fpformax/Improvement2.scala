package in.harmeetsingh.fpformax

import in.harmeetsingh.fpformax.lib.IO
import scala.io.StdIn.readLine
import scala.util.{Random, Try}

object Improvement2 extends App {

    def parseInt(input: String): Option[Int] = Try(input.toInt).toOption

    // This means, nit actually executed the line of code, just return the
    // description how to print the lone of code to console
    def printStr(string: String): IO[Unit] = IO{ () => println(string) }

    // Same, doesn't means read the line, means return the description
    // how to read the string from console
    def getString(): IO[String] = IO(() => readLine())

    def getNextInt(value: Int): IO[Int] = IO(() => Random.nextInt(value))

    def checkContinue(name: String): IO[Boolean] = for {
        _       <-  printStr("Do you want to continue, "+ name + " ?")
        input   <-  getString().map(_.toLowerCase)
        cont    <-  input match {
                        case "y" => IO.point(true)
                        case "n" => IO.point(false)
                        case _   => checkContinue(name)
                    }
    } yield cont

    def gameLoop(name: String): IO[Unit] = for {
        num     <- getNextInt(5).map(_ + 1)
        _       <- printStr("Dear "+ name + ", please guess a number from 1 to 5")
        input   <- getString()
        _       <- parseInt(input).fold[IO[Unit]](
                        printStr("You didn't enter a number")
                    )(guess =>
                        if(guess == num) printStr("Your guess right, "+ name + " !")
                        else printStr("You guessed wrong, "+ name + " ?")
                    )
        cont    <- checkContinue(name)
        _       <- if(cont) gameLoop(name) else IO.point(())
    } yield ()

    val program : IO[Unit] = for {
        _       <- printStr("What is your name")
        name    <- getString()
        _       <- printStr("Hello " + name + " welcome to the game!!")
        _       <- gameLoop(name)
    } yield ()

    program.unsafeRun()
}
