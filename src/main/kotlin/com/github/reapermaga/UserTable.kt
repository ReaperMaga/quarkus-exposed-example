package com.github.reapermaga

import com.github.reapermaga.library.table.LongTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant

object UserTable : LongTable("user") {
    val name = varchar("name", 255)
    val age = integer("age")
    val password = varchar("password", 255)
    val createdAt = timestamp("created_at").default(Instant.now())
    var tags = array<String>("tags")
}

object FriendshipTable : Table("friendships") {
    val user = reference("user", UserTable.id, ReferenceOption.CASCADE)
    val friend = reference("friend", UserTable.id, ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(user, friend)
}