package in.harmeetsingh.example2.free

import java.util.UUID
import cats.InjectK
import cats.data.EitherK
import cats.free.Free


case class User(id: UUID, email: String, loyaltyPoints: Int)

sealed trait UserRepositoryAlg[T]
case class FindUser(id: UUID) extends UserRepositoryAlg[Option[User]]
case class UpdatedUser(user: User) extends UserRepositoryAlg[Unit]

sealed trait EmailAlg[T]
case class SendEmail(email: String, subject: String, body: String) extends EmailAlg[Unit]

object RepositoryAlg {
    type UserAndEmailAlg[T] = EitherK[UserRepositoryAlg, EmailAlg, T]

    class Users[F[_]](implicit i: InjectK[UserRepositoryAlg, F]) {
        def findUser(userId: UUID): Free[F, Option[User]] = Free.inject(FindUser(userId))
        def updatedUser(user: User): Free[F, Unit] = Free.inject(UpdatedUser(user))
    }

    object Users {
        implicit def users[F[_]](implicit i: InjectK[UserRepositoryAlg, F]) = new Users
    }

    class Emails[F[_]](implicit i: InjectK[EmailAlg, F]) {
        def sendEmail(email: String, subject: String, body: String) =
            Free.inject(SendEmail(email, subject, body))
    }

    object Emails {
        implicit def emails[F[_]](implicit i: InjectK[EmailAlg, F]) = new Emails
    }

}

