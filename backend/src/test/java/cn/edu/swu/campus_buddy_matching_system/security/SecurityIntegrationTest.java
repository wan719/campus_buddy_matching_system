package cn.edu.swu.campus_buddy_matching_system.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SecurityIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAdminEndpoint_WithoutToken_Returns401() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/admin/hello",
                String.class
        );

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertTrue(response.getBody().contains("\"code\":401"));
    }

    @Test
    void testAdminEndpoint_WithUserToken_Returns403() throws Exception {
        String registerJson = """
                {
                  "studentId":"20259999",
                  "username":"test_user_auth",
                  "password":"123456",
                  "confirmPassword":"123456",
                  "email":"test_user_auth@swu.edu.cn",
                  "nickname":"测试用户"
                }
                """;

        HttpHeaders registerHeaders = new HttpHeaders();
        registerHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> registerEntity = new HttpEntity<>(registerJson, registerHeaders);

        ResponseEntity<String> registerResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/register",
                registerEntity,
                String.class
        );

        assertEquals(HttpStatus.OK, registerResponse.getStatusCode());

        String loginJson = """
                {
                  "username":"test_user_auth",
                  "password":"123456"
                }
                """;

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> loginEntity = new HttpEntity<>(loginJson, loginHeaders);

        ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/auth/login",
                loginEntity,
                String.class
        );

        JsonNode jsonNode = objectMapper.readTree(loginResponse.getBody());

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertTrue(jsonNode.get("success").asBoolean());
        assertNotNull(jsonNode.get("data"));
        assertNotNull(jsonNode.get("data").get("token"));

        String token = jsonNode.get("data").get("token").asText();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/admin/hello",
                HttpMethod.GET,
                requestEntity,
                String.class
        );

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().contains("\"code\":403"));
    }
}