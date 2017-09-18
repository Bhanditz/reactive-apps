package com.example.integration

import com.example.domain.Role
import com.example.domain.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.client.bodyToFlux
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.test.test

class UserIntegrationTests : AbstractIntegrationTests() {

    @Test
    fun `Create a new user`() {
        client.post().uri("/api/users/").accept(APPLICATION_JSON).contentType(APPLICATION_JSON)
                .syncBody(User("brian", "Brian", "Clozel", "bc@gm.com"))
                .retrieve()
                .bodyToMono<User>()
                .test()
                .consumeNextWith { assertEquals("brian", it.login) }
                .verifyComplete()
    }

    @Test
    fun `Find Dan North`() {
        client.get().uri("/api/users/tastapod").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono<User>()
                .test()
                .consumeNextWith {
                    assertEquals("North", it.lastname)
                    assertEquals("Dan", it.firstname)
                    assertTrue(it.role == Role.USER)
                }
                .verifyComplete()
    }

    @Test
    fun `Find Guillaume Ehret staff member`() {
        client.get().uri("/api/staff/guillaumeehret").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux<User>()
                .test()
                .consumeNextWith {
                    assertEquals("Ehret", it.lastname)
                    assertEquals("Guillaume", it.firstname)
                    assertTrue(it.role == Role.STAFF)
                }
                .verifyComplete()
    }

    @Test
    fun `Find all staff members`() {
        client.get().uri("/api/staff/").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux<User>()
                .test()
                .expectNextCount(15)
                .verifyComplete()
    }

    @Test
    fun `Find Zenika Lyon`() {
        client.get().uri("/api/users/Zenika Lyon").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux<User>()
                .test()
                .consumeNextWith {
                    assertEquals("Jacob", it.lastname)
                    assertEquals("Hervé", it.firstname)
                    assertTrue(it.role == Role.USER)
                }
                .verifyComplete()
    }

}
