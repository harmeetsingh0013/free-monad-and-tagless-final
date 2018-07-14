package in.harmeetsingh.example1.free

import java.util.UUID

case class User(id: UUID, email: String, loyaltyPoints: Int)

sealed trait UserRepositoryAlg[T]
case class FindUser(id: UUID) extends UserRepositoryAlg[Option[User]]
case class UpdatedUser(user: User) extends UserRepositoryAlg[Unit]

object UserRepositoryAlg {
    import cats.free.Free

    type UserRepository[T] = Free[UserRepositoryAlg, T]

    def findUser(userId: UUID): UserRepository[Option[User]] = Free.liftF(FindUser(userId))
    def updatedUser(user: User): UserRepository[Unit] = Free.liftF(UpdatedUser(user))
}

