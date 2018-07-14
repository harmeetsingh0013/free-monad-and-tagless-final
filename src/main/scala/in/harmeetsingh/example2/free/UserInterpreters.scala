package in.harmeetsingh.example2.free

import cats.~>
import in.harmeetsingh.example1.free.{FindUser, UpdatedUser}
import scala.concurrent.Future

object UserInterpreters {

    val futureUserInterpreter = new (UserRepositoryAlg ~> Future) {
        override def apply[A](fa : UserRepositoryAlg[A]) : Future[A] = fa match {
            case FindUser(userId) =>
                // Go to the database and find the user
                Future.successful(Some(User(userId, "harmeet@in", 13))).asInstanceOf[Future[A]]
            case UpdatedUser(user) =>
                // Go to the database and update user
                Future.successful(()).asInstanceOf[Future[A]]
        }
    }

    val futureEmailInterpreter = new (EmailAlg ~> Future) {
        override def apply[A](fa : EmailAlg[A]) : Future[A] = fa match {
            case SendEmail(email, _, _) =>
                // send an email use thrid party service
                println(s"Email successfully send to $email")
                Future.successful(()).asInstanceOf[Future[A]]
        }
    }
}
