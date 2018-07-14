package in.harmeetsingh.example1.free

import java.util.UUID
import UserRepositoryAlg._
import cats.free.Free
import in.harmeetsingh.example1.free.Interpreters.futureInterpreter
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import cats.implicits._
import scala.concurrent.ExecutionContext.Implicits.global

object UserService extends App {

    def addPoints(userId: UUID, pointsToAdd: Int): UserRepository[Either[String, Unit]] = {
        findUser(userId).flatMap {
            case None => Free.pure(Left(s"User $userId not found"))
            case Some(user) =>
                val updated = user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
                updatedUser(updated).map(_ => Right())
        }

    }

    val result : Future[Either[String, Unit]] =
        addPoints(UUID.randomUUID(), 10).foldMap(futureInterpreter)

    val output : Either[String, Unit] = Await.result(result, 1 seconds)
    println(s"Output: $output")
}
