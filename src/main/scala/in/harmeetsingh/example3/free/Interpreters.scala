package in.harmeetsingh.example3.free

import cats.~>
import in.harmeetsingh.example3.UserService.User
import scala.concurrent.Future

object Interpreters {

    val FutureInterpreter = new (UserRepositoryAlg ~> Future) {
        override def apply[A](fa : UserRepositoryAlg[A]) : Future[A] = fa match {
            case Create(user) =>
                // perform database operation
                Future.successful(true).asInstanceOf[Future[A]]
            case Read(userId) =>
                // perform database operations
                Future.successful(Right(User(userId, "harmeet@knoldus.com", 13)))
                    .asInstanceOf[Future[A]]
            case Delete(userId) =>
                // perform database operations
                Future.successful(Right(())).asInstanceOf[Future[A]]
        }
    }
}
