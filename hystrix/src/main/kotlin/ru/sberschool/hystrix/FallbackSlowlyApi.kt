package ru.sberschool.hystrix

import feign.Param
import ru.sberschool.hystrix.entity.Pokemon

class FallbackSlowlyApi : SlowlyApi {
    override fun getPokemon(id: Long): Pokemon = Pokemon(listOf(), 0, listOf())
}


