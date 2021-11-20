package ru.sberschool.hystrix

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Request
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
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
    val mapper = ObjectMapper()


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
        val sentByMockServerPokemon = Pokemon(listOf(Ability(
            Element("kick", "https://pokemon.co/"),
            false,
            1)),
            100,
            listOf(Element("base", "https://pokemon.co/"))
            )
        val sentPokemonJson = mapper.writeValueAsString(sentByMockServerPokemon)

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
                    .withBody(sentPokemonJson)
            )
        // do
        val receivedByClientPokemon = client.getPokemon(1)

        // expect
        assertEquals(sentByMockServerPokemon, receivedByClientPokemon)
    }
}
