package in.harmeetsingh.example3

import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object UserService extends App {

    val userRepo = new UserRepo
    case class User(id: UUID, email: String, loyaltyPoints: Int)

    val user = User(UUID.randomUUID(), "harmeet@knoldus.com", 13)
    val futureResult = for {
        _ <- userRepo.addUser(user)
        errorOrUser <- userRepo.updateUser(user.copy(email = "harmeet@in"))
    } yield errorOrUser

    val result = Await.result(futureResult, 1 seconds)
    println(s"Result: $result")
}
