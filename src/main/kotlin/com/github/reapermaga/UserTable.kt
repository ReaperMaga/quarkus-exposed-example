package com.github.reapermaga

import com.github.reapermaga.library.entity.ExposedEntity
import com.github.reapermaga.library.table.LongTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object UserTable:LongTable("user") {
    val name = varchar("name", 255)
    val age = integer("age")
    val password = varchar("password", 255)
    var tags = array<String>("tags")
    val metrics = metrics()
}

object FriendshipTable:Table("friendships") {
    val user = reference("user", UserTable.id, ReferenceOption.CASCADE)
    val friend = reference("friend", UserTable.id, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(user, friend)
}

fun Table.metrics(): Metrics {
    return object : Metrics {
        override val createdAt : Column<Instant> = timestamp("created_at").default(Instant.now())
        override val updatedAt : Column<Instant> = timestamp("updated_at").default(Instant.now())
    }
}

interface Metrics {
    val createdAt: Column<Instant>
    val updatedAt: Column<Instant>
}

class MetricsEntity : ExposedEntity() {
    var createdAt : Instant by bind(UserTable.metrics.createdAt)
    var updatedAt : Instant by bind(UserTable.metrics.updatedAt)

    companion object {
        fun create() = MetricsEntity().apply {
            createdAt = Instant.now()
            updatedAt = Instant.now()
        }
    }
}


