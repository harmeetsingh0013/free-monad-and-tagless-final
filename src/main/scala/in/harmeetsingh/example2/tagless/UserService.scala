package in.harmeetsingh.example2.tagless

import java.util.UUID
import cats.Monad
import cats.implicits._
import in.harmeetsingh.example2.tagless.Interpreters.{FutureEmailInterpreter, FutureUserInterpreter}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class UserService[F[_]: Monad] (ur: UserRepositoryAlg[F], es: EmailAlg[F]) {

    def addPoints(userId: UUID, pointsToAdd: Int): F[Either[String, Unit]] = {
        ur.findUser(userId).flatMap {
            case None => implicitly[Monad[F]].pure(Left(s"User $userId not found"))
            case Some(user) =>
                val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
                for {
                    _ <- ur.updatedUser(updated)
                    _ <- es.sendEmail(user.email, "Points Winner",
                        s"You have won $pointsToAdd points")
                } yield Right(())
        }
    }
}

object UserService extends App {

    val userService = new UserService(new FutureUserInterpreter {}, new FutureEmailInterpreter {})

    val result : Future[Either[String, Unit]] =
        userService.addPoints(UUID.randomUUID(), 13)

    val output : Either[String, Unit] = Await.result(result, 1 seconds)
    println(s"Output: $output")
}
