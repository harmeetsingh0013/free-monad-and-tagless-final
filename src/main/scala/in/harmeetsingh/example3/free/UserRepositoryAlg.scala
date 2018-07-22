package in.harmeetsingh.example3.free

import java.util.UUID
import cats.free.Free

case class User(id: UUID, email: String, loyaltyPoints: Int)
case class DatabaseError(message: String)

sealed trait UserRepositoryAlg[T]
case class Create(user: User) extends UserRepositoryAlg[Boolean]
case class Read(userId: UUID) extends UserRepositoryAlg[Either[DatabaseError, User]]
case class Delete(userId: UUID) extends UserRepositoryAlg[Either[DatabaseError, Unit]]

object UserRepositoryAlg {
    type UserRepository[T] = Free[UserRepositoryAlg, T]

    def create(user: User): UserRepository[Boolean] = {
        Free.liftF[UserRepositoryAlg, Boolean](Create(user))
    }

    def read(userId: UUID): UserRepository[Either[DatabaseError, User]] = {
        Free.liftF[UserRepositoryAlg, Either[DatabaseError, User]](Read(userId))
    }

    def delete(userId: UUID): UserRepository[Either[DatabaseError, Unit]] = {
        Free.liftF[UserRepositoryAlg, Either[DatabaseError, Unit]](Delete(userId))
    }
}
