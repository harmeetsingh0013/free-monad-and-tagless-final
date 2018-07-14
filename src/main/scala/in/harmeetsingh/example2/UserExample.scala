package in.harmeetsingh.example2

import java.util.UUID
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

object UserExample extends App {

    case class User(id: UUID, email: String, loyaltyPoints: Int)

    class EmailService {
        def sendEmail(email: String, subject: String, body: String): Future[Unit] = {
            // send an email using external source
            println(s"Email send successfully to $email")
            Future.successful(())
        }
    }

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


    class UserService(userRepository: UserRepository, es: EmailService) {
        def addPoints(userId: UUID, pointsToAdd: Int): Future[Either[String, Unit]] = {
            userRepository.findUser(userId).flatMap {
                case None => Future.successful(Left(s"User $userId not found"))
                case Some(user) =>
                    val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
                    for {
                        _ <- userRepository.updateUser(updated).map(_ => Right())
                        _  <- es.sendEmail(user.email, "Points Winner",
                            s"You have won $pointsToAdd points")
                    } yield Right(())
            }
        }
    }

    val userService = new UserService(new UserRepository, new EmailService)
    val result : Future[Either[String, Unit]] =
        userService.addPoints(UUID.randomUUID(), 10)

    val output : Either[String, Unit] = Await.result(result, 1 seconds)
    println(s"Output: $output")
}
