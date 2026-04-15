package cn.edu.swu.campus_buddy_matching_system.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtTokenProvider, "secret",
                "mySecretKeyForJWTTokenGenerationMustBeAtLeast256BitsLongForSecurity123");
        ReflectionTestUtils.setField(jwtTokenProvider, "expiration", 86400000L);
        jwtTokenProvider.init();
    }

    @Test
    void testGenerateToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("user:read")
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);

        assertNotNull(token);
        assertEquals(3, token.split("\\.").length);
    }

    @Test
    void testValidateToken_ValidToken_Success() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        String token = jwtTokenProvider.generateToken(authentication);

        assertTrue(jwtTokenProvider.validateToken(token));
    }

    @Test
    void testGetAuthentication_FromToken() {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                "admin",
                null,
                List.of(
                        new SimpleGrantedAuthority("ROLE_ADMIN"),
                        new SimpleGrantedAuthority("user:read")
                )
        );

        String token = jwtTokenProvider.generateToken(authentication);
        Authentication auth = jwtTokenProvider.getAuthentication(token);

        assertNotNull(auth);
        assertEquals("admin", auth.getName());
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("user:read")));
    }
}