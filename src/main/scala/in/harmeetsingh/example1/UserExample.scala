package in.harmeetsingh.example1

import java.util.UUID
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object UserExample extends App {

    case class User(id: UUID, email: String, loyaltyPoints: Int)

    class UserRepository {
        def findUser(id: UUID): Future[Option[User]] = {
            // interact with database
            Future.successful(Some(User(id, "harmeet@in", 100)))
        }
        def updateUser(user: User): Future[Unit] = {
            // interact with database and update the user
            Future.successful(())
        }
    }

    class UserService(userRepository: UserRepository) {
        def addPoints(userId: UUID, pointsToAdd: Int): Future[Either[String, Unit]] = {
            userRepository.findUser(userId).flatMap {
                case None => Future.successful(Left(s"User $userId not found"))
                case Some(user) =>
                    val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
                    userRepository.updateUser(updated).map(_ => Right())
            }
        }
    }

    val userService = new UserService(new UserRepository)
    val result : Future[Either[String, Unit]] =
        userService.addPoints(UUID.randomUUID(), 10)

    val output : Either[String, Unit] = Await.result(result, 1 seconds)
    println(s"Output: $output")
}