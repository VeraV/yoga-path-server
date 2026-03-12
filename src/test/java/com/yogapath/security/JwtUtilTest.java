package com.yogapath.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // HMAC-SHA256 requires at least 32 characters (256 bits)
    private static final String TEST_SECRET = "test-secret-key-minimum-32-chars!!";
    private static final long ONE_HOUR_MS = 3_600_000L;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", ONE_HOUR_MS);
    }

    @Test
    void generateToken_returnsNonNullToken() {
        String token = jwtUtil.generateToken("vera@example.com", 1L);

        assertThat(token).isNotNull();
        assertThat(token).isNotBlank();
    }

    @Test
    void getEmailFromToken_returnsCorrectEmail() {
        String token = jwtUtil.generateToken("vera@example.com", 1L);

        String email = jwtUtil.getEmailFromToken(token);

        assertThat(email).isEqualTo("vera@example.com");
    }

    @Test
    void getUserIdFromToken_returnsCorrectUserId() {
        String token = jwtUtil.generateToken("vera@example.com", 1L);

        Long userId = jwtUtil.getUserIdFromToken(token);

        assertThat(userId).isEqualTo(1L);
    }

    @Test
    void validateToken_returnsTrue_forValidToken() {
        String token = jwtUtil.generateToken("vera@example.com", 1L);

        assertThat(jwtUtil.validateToken(token)).isTrue();
    }

    @Test
    void validateToken_returnsFalse_forTamperedToken() {
        String token = jwtUtil.generateToken("vera@example.com", 1L);
        String tampered = token + "corrupted";

        assertThat(jwtUtil.validateToken(tampered)).isFalse();
    }

    @Test
    void validateToken_returnsFalse_forTokenSignedWithDifferentSecret() {
        JwtUtil otherJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(otherJwtUtil, "secret", "completely-different-secret-key!!");
        ReflectionTestUtils.setField(otherJwtUtil, "expiration", ONE_HOUR_MS);

        String tokenFromOtherSecret = otherJwtUtil.generateToken("vera@example.com", 1L);

        assertThat(jwtUtil.validateToken(tokenFromOtherSecret)).isFalse();
    }

    @Test
    void validateToken_returnsFalse_forExpiredToken() {
        ReflectionTestUtils.setField(jwtUtil, "expiration", -1000L);

        String expiredToken = jwtUtil.generateToken("vera@example.com", 1L);

        assertThat(jwtUtil.validateToken(expiredToken)).isFalse();
    }

    @Test
    void generateToken_roundTrip_emailAndUserIdMatchAfterExtraction() {
        String token = jwtUtil.generateToken("vera@example.com", 42L);

        assertThat(jwtUtil.getEmailFromToken(token)).isEqualTo("vera@example.com");
        assertThat(jwtUtil.getUserIdFromToken(token)).isEqualTo(42L);
    }
}
