package in.harmeetsingh.example1.tagless

import java.util.UUID
import scala.concurrent.Future

trait FutureInterpreter extends UserRepositoryAlg[Future] {

    override def findUser(userId : UUID) : Future[Option[User]] = {
        // perform some database operation and return user
        Future.successful(Some(User(userId, "harmeet@in", 13)))
    }

    override def updatedUser(user : User) : Future[Unit] = {
        // perform whatever db operations and update user
        Future.successful(())
    }
}
