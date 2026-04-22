package cn.edu.swu.campus_buddy_matching_system.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Swagger / OpenAPI 集成测试
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SwaggerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testApiDocsEndpoint_Accessible() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/v3/api-docs",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        JsonNode root = objectMapper.readTree(response.getBody());

        assertNotNull(root.get("openapi"));
        assertNotNull(root.get("info"));
        assertNotNull(root.get("paths"));
    }

    @Test
    void testSwaggerUiEndpoint_Accessible() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/swagger-ui/index.html",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Swagger UI")
                || response.getBody().contains("swagger-ui"));
    }

    @Test
    void testApiDocs_ContainsAuthEndpoints() throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/v3/api-docs",
                String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode paths = root.get("paths");

        assertNotNull(paths);
        assertTrue(paths.has("/api/auth/login"), "OpenAPI 文档中应包含 /api/auth/login");
        assertTrue(paths.has("/api/auth/register"), "OpenAPI 文档中应包含 /api/auth/register");
        assertTrue(paths.has("/api/auth/me"), "OpenAPI 文档中应包含 /api/auth/me");
    }
}