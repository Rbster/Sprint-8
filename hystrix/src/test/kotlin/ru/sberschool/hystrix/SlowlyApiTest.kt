package ru.sberschool.hystrix

import feign.Request
import feign.RequestTemplate
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.integration.ClientAndServer
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import ru.sberschool.hystrix.entity.Ability
import ru.sberschool.hystrix.entity.Element
import ru.sberschool.hystrix.entity.Pokemon
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class SlowlyApiTest {
    val client = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
        // для удобства тестирования задаем таймауты на 1 секунду
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "http://127.0.0.1:18080", FallbackSlowlyApi())
    lateinit var mockServer: ClientAndServer


    @BeforeEach
    fun setup() {
        // запускаем мок сервер для тестирования клиента
        mockServer = ClientAndServer.startClientAndServer(18080)
    }

    @AfterEach
    fun shutdown() {
        mockServer.stop()
    }

    @Test
    fun `getSomething() should return predefined data`() {
        // given
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/api/v2/pokemon/1")
            )
            .respond(
                // наш запрос попадает на таймаут
                HttpResponse.response()
                    .withStatusCode(400)
                    .withDelay(TimeUnit.SECONDS, 30) //
            )
        // expect
        assertEquals(Pokemon(listOf(), 0, listOf()), client.getPokemon(0))
    }

    @Test
    fun `getSomething() should return actual data`() {
        // given
        val responsePokemon = Pokemon(listOf(Ability(
            Element("kick", "https://pokemon.co/"),
            false,
            1)),
            100,
            listOf(Element("base", "https://pokemon.co/"))
            )
        val pokemonRequest = RequestTemplate()
//        JacksonEncoder().mapper
            JacksonEncoder().encode(responsePokemon, Pokemon::class.java, pokemonRequest)
        MockServerClient("127.0.0.1", 18080)
            .`when`(
                // задаем матчер для нашего запроса
                HttpRequest.request()
                    .withMethod("GET")
                    .withPath("/api/v2/pokemon/1")
            )
            .respond(
                // наш запрос попадает на таймаут
                HttpResponse.response()
                    .withStatusCode(200)
                    .withBody(pokemonRequest.body())
//                    .withDelay(TimeUnit.SECONDS, 30) //

            )
        // do
        val receivedPokemon = client.getPokemon(1)
        println(responsePokemon)
        println(receivedPokemon)

        // expect
        assertEquals(responsePokemon.baseExperience, receivedPokemon.baseExperience)
        assertEquals(responsePokemon.abilities.size, receivedPokemon.abilities.size)
        for (i in 0 until responsePokemon.abilities.size) {
            assertEquals(responsePokemon.abilities[i].ability, receivedPokemon.abilities[i].ability)
            assertEquals(responsePokemon.abilities[i].slot, receivedPokemon.abilities[i].slot)
            assertEquals(responsePokemon.abilities[i].hidden, receivedPokemon.abilities[i].hidden)
        }
        assertEquals(responsePokemon.forms.size, receivedPokemon.forms.size)
        for (i in 0 until responsePokemon.forms.size) {
            assertEquals(responsePokemon.forms[i].name, receivedPokemon.forms[i].name)
            assertEquals(responsePokemon.forms[i].url, receivedPokemon.forms[i].url)
        }

    }
}
