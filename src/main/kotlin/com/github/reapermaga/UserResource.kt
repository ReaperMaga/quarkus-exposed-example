package com.github.reapermaga

import jakarta.ws.rs.GET
import jakarta.ws.rs.Path

@Path("/")
class UserResource(
    private val context : DatabaseContext,
    private val userDao : UserDao
) {

    @GET
    @Path("/db")
    suspend fun getDatabase(): String {
        return context.database.url
    }

    @GET
    @Path("/create")
    suspend fun get(): User {
        val user = userDao.create("John", 30, "password")
        val friend = userDao.create("Doe", 18, "password")
        userDao.createFriendship(user.id, friend.id)
        return user
    }

    @GET
    @Path("/clear")
    suspend fun clear() {
        userDao.clearAll()
    }

    @GET
    @Path("/deleteRandom")
    suspend fun deleteRandom(): User = userDao.deleteRandom()

    @GET
    suspend fun getAll(): List<User> {
        return userDao.findAll().map { it.friends = userDao.findFriends(it.id); it }.toList()
    }
}