package com.github.reapermaga

import com.github.reapermaga.library.dao.ExposedDao
import com.github.reapermaga.library.entity.createEntity
import jakarta.enterprise.context.ApplicationScoped
import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import java.time.Instant

interface UserDao {

    suspend fun create(name: String, age: Int, password: String): User
    suspend fun createFriendship(userId: Long, friendId: Long)
    suspend fun findAll(): List<User>
    suspend fun findById(id: Long): User?
    suspend fun findFriends(id: Long): List<User>
    suspend fun clearAll()
    suspend fun deleteRandom(): User

}

@ApplicationScoped
class UserDaoImpl : ExposedDao<Long>(UserTable, FriendshipTable), UserDao {

    override suspend fun clearAll() {
        runQuery {
            FriendshipTable.deleteAll()
            UserTable.deleteAll()
        }
    }

    override suspend fun create(name : String, age : Int, password : String) : User = runQuery {
        val tags = listOf("tag1", "tag2")
        UserTable.insert {
            it[UserTable.name] = name
            it[UserTable.age] = age
            it[UserTable.password] = password
            it[UserTable.tags] = tags
        }.let {
            val user = User()
            user.id = it[UserTable.id]
            user.name = name
            user.age = age
            user.password = password
            user.metrics = MetricsEntity.create()
            user.tags = tags
            user
        }
    }

    override suspend fun createFriendship(userId: Long, friendId : Long) = runQuery {
        FriendshipTable.insert {
            it[FriendshipTable.user] = userId
            it[FriendshipTable.friend] = friendId
        }
        FriendshipTable.insert {
            it[FriendshipTable.user] = friendId
            it[FriendshipTable.friend] = userId
        }
        return@runQuery
    }

    override suspend fun findAll() : List<User> {
        return findAll { createEntity(it) }
    }

    override suspend fun findById(id: Long): User? {
        return findById(id) { createEntity(it) }
    }

    override suspend fun findFriends(id : Long) : List<User> = runQuery {
        UserTable.join(FriendshipTable, JoinType.LEFT, onColumn = UserTable.id, otherColumn = FriendshipTable.user)
            .selectAll()
            .where { FriendshipTable.friend eq id }
            .map { createEntity(it) }
    }

    override suspend fun deleteRandom(): User {
        val user = findAll().random()
        deleteById(user.id)
        return user
    }

}