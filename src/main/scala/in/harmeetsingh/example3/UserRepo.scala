package in.harmeetsingh.example3

import java.util.UUID
import cats.data.EitherT
import cats.implicits._
import in.harmeetsingh.example3.UserService.User
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserRepo {
    case class DatabaseError(message: String)

    def getUser(userId: UUID): Future[Either[DatabaseError, User]] = {
        println(s"Returning a user with given id")
        Future.successful(Right(User(userId, "harmeet@knoldus.com", 13)))
    }

    def addUser(user: User): Future[Boolean] = {
        println(s"Insert user successfully")
        Future.successful(true)
    }

    def updateUser(user: User): Future[Either[DatabaseError, Boolean]] = (for {
        _ <- EitherT(getUser(UUID.randomUUID())) // verify user is exist of not
        added <- EitherT.liftF[Future, DatabaseError, Boolean](addUser(user))
    } yield added).value

}
