package ru.sberschool.hystrix

import com.fasterxml.jackson.databind.ObjectMapper
import feign.Request
import feign.httpclient.ApacheHttpClient
import feign.hystrix.HystrixFeign
import feign.jackson.JacksonDecoder
import org.junit.jupiter.api.Test
import org.mockserver.client.server.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import ru.sberschool.hystrix.entity.Ability
import ru.sberschool.hystrix.entity.Element
import ru.sberschool.hystrix.entity.Pokemon
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

class RealApiTest {
    val client = HystrixFeign.builder()
        .client(ApacheHttpClient())
        .decoder(JacksonDecoder())
        // для удобства тестирования задаем таймауты на 1 секунду
        .options(Request.Options(1, TimeUnit.SECONDS, 1, TimeUnit.SECONDS, true))
        .target(SlowlyApi::class.java, "https://pokeapi.co", FallbackSlowlyApi())
//    lateinit var mockServer: ClientAndServer
    val mapper = ObjectMapper()


//    @BeforeEach
//    fun setup() {
//        // запускаем мок сервер для тестирования клиента
//        mockServer = ClientAndServer.startClientAndServer(18080)
//    }
//
//    @AfterEach
//    fun shutdown() {
//        mockServer.stop()
//    }


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

//        val sentPokemonJson = mapper.writeValueAsString(sentByRealServerPokemon)
//
//        MockServerClient("127.0.0.1", 18080)
//            .`when`(
//                // задаем матчер для нашего запроса
//                HttpRequest.request()
//                    .withMethod("GET")
//                    .withPath("/api/v2/pokemon/1")
//            )
//            .respond(
//                // наш запрос попадает на таймаут
//                HttpResponse.response()
//                    .withStatusCode(200)
//                    .withBody(sentPokemonJson)
//            )
        // do
        val receivedByClientPokemon = client.getPokemon(1)
        println("received = $receivedByClientPokemon")
        println("sent = $sentByRealServerPokemon")

        // expect
        assertEquals(sentByRealServerPokemon, receivedByClientPokemon)
    }
}