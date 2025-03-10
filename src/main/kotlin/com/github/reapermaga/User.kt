package com.github.reapermaga

import com.github.reapermaga.library.entity.ExposedEntity
import java.time.Instant

class User : ExposedEntity() {

    var id: Long by bind(UserTable.id)
    var name : String by bind(UserTable.name)
    var age : Int by bind(UserTable.age)
    var password : String by bind(UserTable.password)
    var metrics: MetricsEntity by bindEntity()
    var tags : List<String> by bind(UserTable.tags)
    var friends: List<User>? = null
}