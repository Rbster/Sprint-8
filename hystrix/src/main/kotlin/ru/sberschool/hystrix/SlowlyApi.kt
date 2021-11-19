package ru.sberschool.hystrix

import feign.Param
import feign.RequestLine
import ru.sberschool.hystrix.entity.Pokemon

interface SlowlyApi {
    @RequestLine("GET /api/v2/pokemon/{id}")
    fun getPokemon(@Param("id") id: Long): Pokemon
}


