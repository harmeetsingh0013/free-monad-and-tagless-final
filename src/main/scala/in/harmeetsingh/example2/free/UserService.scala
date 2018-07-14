package in.harmeetsingh.example2.free

import java.util.UUID
import cats.free.Free
import in.harmeetsingh.example2.free.RepositoryAlg.{Emails, UserAndEmailAlg, Users}
import in.harmeetsingh.example2.free.Interpreters._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global

object UserService extends App {

    def addPoints(userId: UUID, pointsToAdd: Int)(
        implicit ur: Users[UserAndEmailAlg],
        es: Emails[UserAndEmailAlg]
    ): Free[UserAndEmailAlg, Either[String, Unit]] = {

        ur.findUser(userId).flatMap {
            case None => Free.pure(Left(s"User $userId not found"))
            case Some(user) =>
                val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
                for {
                    _ <- ur.updatedUser(updated)
                    _ <- es.sendEmail(user.email, "Points Winner",
                        s"You have won $pointsToAdd points")
                } yield Right(())
        }
    }

    val futureUserAndEmailInternpreter = futureUserInterpreter or futureEmailInterpreter

    val result : Future[Either[String, Unit]] =
        addPoints(UUID.randomUUID(), 10).foldMap(futureUserAndEmailInternpreter)

    val output : Either[String, Unit] = Await.result(result, 1 seconds)
    println(s"Output: $output")
}
