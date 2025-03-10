package com.github.reapermaga

import com.fasterxml.jackson.databind.MapperFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import io.agroal.api.AgroalDataSource
import io.quarkus.arc.All
import io.quarkus.jackson.ObjectMapperCustomizer
import io.quarkus.runtime.Startup
import jakarta.enterprise.context.ApplicationScoped
import jakarta.inject.Singleton
import org.jetbrains.exposed.sql.Database

@ApplicationScoped
class DatabaseContext(
    private val dataSource : AgroalDataSource,
) {

    lateinit var database : Database

    @Startup
    fun init() {
        database = Database.connect(dataSource)
    }

}

class ObjectMapperConfiguration {

    @Singleton
    fun objectMapper(@All customizers : MutableList<ObjectMapperCustomizer>) : ObjectMapper {
        val mapper = JsonMapper.builder()
            .configure(MapperFeature.PROPAGATE_TRANSIENT_MARKER, true)
            .build()
        for (customizer in customizers) {
            customizer.customize(mapper)
        }
        return mapper
    }

}