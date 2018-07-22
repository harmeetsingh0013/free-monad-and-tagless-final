package in.harmeetsingh.example3.tagless

import java.util.UUID
import cats.Monad
import cats.data.EitherT
import cats.implicits._
import in.harmeetsingh.example3.tagless.Interpreters._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class UserService[F[_]: Monad](userRepo: UserRepositoryAlg[F]) {

    def readUser(userId: UUID): F[Either[DatabaseError, User]] = userRepo.read(userId)

    def addUser(user: User): F[Boolean] = userRepo.create(user)

    def updateUser(user: User) : F[Either[DatabaseError, Boolean]] = (for {
        _ <- EitherT(userRepo.read(user.id))
        added <- EitherT.liftF[F, DatabaseError, Boolean](userRepo.create(user))
    } yield added).value
}

object UserService extends App {

    val userService = new UserService(futureInterpreters)

    val user = User(UUID.randomUUID(), "harmeet@knoldus.com", 13)
    val futureResult = userService.updateUser(user)
    val result = Await.result(futureResult, 1 seconds)
    println(s"Result: $result")
}
