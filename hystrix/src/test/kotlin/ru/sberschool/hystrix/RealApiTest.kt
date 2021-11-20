package ru.sberschool.hystrix

import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.Test
import ru.sberschool.hystrix.entity.Ability
import ru.sberschool.hystrix.entity.Element
import ru.sberschool.hystrix.entity.Pokemon
import kotlin.test.assertEquals

// time-out is possible, but unlikely with good internet
class RealApiTest {
    val client = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
        .target(SlowlyApi::class.java, "https://pokeapi.co")

    @Test
    fun `getSomething() should return actual data`() {
        // given
        val sentByRealServerPokemon = Pokemon(listOf(
            Ability(
            Element("overgrow", "https://pokeapi.co/api/v2/ability/65/"),
            false,
            1),
            Ability(
                Element("chlorophyll", "https://pokeapi.co/api/v2/ability/34/"),
                true,
                3)
        ),
            64,
            listOf(Element("bulbasaur", "https://pokeapi.co/api/v2/pokemon-form/1/"))
        )

        // do
        val receivedByClientPokemon = client.getPokemon(1)

        // expect
        assertEquals(sentByRealServerPokemon, receivedByClientPokemon)
    }
}