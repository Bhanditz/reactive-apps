package com.example.integration

import com.example.domain.Event
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.client.bodyToMono
import org.springframework.web.reactive.function.client.bodyToFlux
import reactor.core.publisher.test


class EventIntegrationTests : AbstractIntegrationTests() {

    @Test
    fun `Find MiXiT 2016 event`() {
        client.get().uri("/api/events/mixit16").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToMono<Event>()
                .test()
                .consumeNextWith {
                    assertEquals(2016, it.year)
                    assertFalse(it.current)
                }
                .verifyComplete()
    }

    @Test
    fun `Find all events`() {
        client.get().uri("/api/events/").accept(APPLICATION_JSON)
                .retrieve()
                .bodyToFlux<Event>()
                .test()
                .consumeNextWith { assertEquals(2012, it.year) }
                .consumeNextWith { assertEquals(2013, it.year) }
                .consumeNextWith { assertEquals(2014, it.year) }
                .consumeNextWith { assertEquals(2015, it.year) }
                .consumeNextWith { assertEquals(2016, it.year) }
                .consumeNextWith { assertEquals(2017, it.year) }
                .verifyComplete()
    }
    
}
