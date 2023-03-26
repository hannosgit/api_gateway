package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HeaderServiceTest {

    private final HeaderService headerService = new HeaderService();
    @Test
    void test_parseAuthorizationHeader() {
        final ApiCredentials apiCredentials = headerService.parseAuthorizationHeader("Basic SGFuczpEYW1wZg==");

        assertEquals("Hans",apiCredentials.username());
        assertEquals("Dampf",apiCredentials.password());
    }
}
