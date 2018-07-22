package in.harmeetsingh.example3.tagless

import java.util.UUID
import scala.concurrent.Future

object Interpreters {

    val futureInterpreters = new UserRepositoryAlg[Future] {
        override def create(user : User) : Future[Boolean] = {
            // perform required database operations
            println(s"User created $user")
            Future.successful(true)
        }

        override def read(userId : UUID) : Future[Either[DatabaseError, User]] = {
            // perform require database operations
            println(s"Find user by Id $userId")
            Future.successful(Right(User(userId, "harmeet@knoldus.com", 13)))
        }

        override def delete(userId : UUID) : Future[Either[DatabaseError, Unit]] = {
            // perform require database operations
            println(s"Delete user by Id $userId")
            Future.successful(Right(()))
        }
    }
}
