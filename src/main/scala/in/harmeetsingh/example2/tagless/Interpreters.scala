package in.harmeetsingh.example2.tagless

import java.util.UUID
import scala.concurrent.Future

object Interpreters {

    trait FutureUserInterpreter extends UserRepositoryAlg[Future] {
        override def findUser(userId : UUID) : Future[Option[User]] = {
            // perform some db operations
            Future.successful(Some(User(userId, "harmeet@in", 13)))
        }

        override def updatedUser(user : User) : Future[Unit] = {
            // perform whatever database operations
            Future.successful(())
        }
    }

    trait FutureEmailInterpreter extends EmailAlg[Future] {
        override def sendEmail(email : String, subject : String, body : String) : Future[Unit] = {
            // perform send email operation
            println(s"Email successfully send to $email")
            Future.successful(())
        }
    }
}
