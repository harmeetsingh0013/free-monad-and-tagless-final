package in.harmeetsingh.example3.tagless

import java.util.UUID

case class User(id: UUID, email: String, loyaltyPoints: Int)
case class DatabaseError(message: String)

trait UserRepositoryAlg[F[_]] {

    def create(user: User): F[Boolean]
    def read(userId: UUID): F[Either[DatabaseError, User]]
    def delete(userId: UUID): F[Either[DatabaseError, Unit]]
}
