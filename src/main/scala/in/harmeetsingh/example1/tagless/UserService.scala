package in.harmeetsingh.example1.tagless

import java.util.UUID
import cats.implicits._
import cats.Monad
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._


class UserService[F[_]: Monad](userRepository: UserRepositoryAlg[F]) {
    def addPoints(userId: UUID, pointsToAdd: Int): F[Either[String, Unit]] = {
        userRepository.findUser(userId).flatMap {
            case None => implicitly[Monad[F]].pure(Left(s"User $userId not found"))
            case Some(user) =>
                val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
                userRepository.updatedUser(user).map(_ => Right())
        }
    }
}

object UserService extends App {
    val userService = new UserService(new FutureInterpreter {})

    val result : Future[Either[String, Unit]] =
        userService.addPoints(UUID.randomUUID(), 10)

    val output : Either[String, Unit] = Await.result(result, 1 seconds)
    println(s"Output: $output")
}
