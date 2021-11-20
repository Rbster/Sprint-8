package ru.sberschool.hystrix

import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import ru.sberschool.hystrix.entity.Ability
import ru.sberschool.hystrix.entity.Element
import ru.sberschool.hystrix.entity.Pokemon
import kotlin.test.assertEquals

// sometimes time-out
class RealApiTest {
    val client = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
//        .options(Request.Options(0, TimeUnit.SECONDS, 0, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "https://pokeapi.co")

    @BeforeEach
    fun setup() {
        // violates isolation principle of tests
        // but it's the easiest method I found
//        HystrixPlugins.reset()
//        HystrixPlugins.getInstance().registerPropertiesStrategy(object : HystrixPropertiesStrategy() {
//            override fun getCommandProperties(
//                commandKey: HystrixCommandKey,
//                builder: HystrixCommandProperties.Setter
//            ): HystrixCommandProperties {
//                val timeout = HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(30000)
//                return super.getCommandProperties(commandKey, timeout)
//            }
//        })
    }

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