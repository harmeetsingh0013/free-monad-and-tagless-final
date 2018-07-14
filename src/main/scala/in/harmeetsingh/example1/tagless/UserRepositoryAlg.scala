package in.harmeetsingh.example1.tagless

import java.util.UUID

trait UserRepositoryAlg[F[_]] {

    case class User(id: UUID, email: String, loyaltyPoints: Int)

    def findUser(userId: UUID): F[Option[User]]
    def updatedUser(user: User): F[Unit]
}
