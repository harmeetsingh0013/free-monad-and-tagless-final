package in.harmeetsingh.example3.free

import java.util.UUID
import cats.data.EitherT
import cats.implicits._
import in.harmeetsingh.example3.free.Interpreters.FutureInterpreter
import in.harmeetsingh.example3.free.UserRepositoryAlg._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object UserService extends App  {

    def readUser(userId: UUID): UserRepository[Either[DatabaseError, User]] = read(userId)

    def addUser(user: User): UserRepository[Boolean] = create(user)

    def updateUser(user: User) : UserRepository[Either[DatabaseError, Boolean]] = (for {
        _ <- EitherT.liftF(read(user.id))
        added <- EitherT.liftF[UserRepository, DatabaseError, Boolean](addUser(user))
    } yield added).value

    val user = User(UUID.randomUUID(), "harmeet@knoldus.com", 13)
    val interpreterResult = for {
        _ <- addUser(user)
        errorOrUser <- updateUser(user.copy(email = "harmeet@in"))
    } yield errorOrUser

    val futureResult = interpreterResult.foldMap(FutureInterpreter)
    val result = Await.result(futureResult, 1 seconds)
    println(s"Result: $result")
}
