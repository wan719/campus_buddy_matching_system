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
                                String.class);

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
                                String.class);

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
                                String.class);

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
                                String.class);

                assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
                assertTrue(response.getBody().contains("\"code\":403"));
        }

        @Test
        void testAdminEndpoint_WithAdminToken_Returns200() throws Exception {
                String loginJson = """
                                {
                                  "username":"admin",
                                  "password":"password123"
                                }
                                """;

                HttpHeaders loginHeaders = new HttpHeaders();
                loginHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<String> loginEntity = new HttpEntity<>(loginJson, loginHeaders);

                ResponseEntity<String> loginResponse = restTemplate.postForEntity(
                                "http://localhost:" + port + "/api/auth/login",
                                loginEntity,
                                String.class);

                assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
                assertNotNull(loginResponse.getBody());

                JsonNode loginJsonNode = objectMapper.readTree(loginResponse.getBody());
                assertTrue(loginJsonNode.get("success").asBoolean());
                assertNotNull(loginJsonNode.get("data"));
                assertNotNull(loginJsonNode.get("data").get("token"));

                String token = loginJsonNode.get("data").get("token").asText();

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(token);
                HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

                ResponseEntity<String> response = restTemplate.exchange(
                                "http://localhost:" + port + "/api/admin/hello",
                                HttpMethod.GET,
                                requestEntity,
                                String.class);

                assertEquals(HttpStatus.OK, response.getStatusCode());
                assertNotNull(response.getBody());

                JsonNode responseJson = objectMapper.readTree(response.getBody());
                assertTrue(responseJson.get("success").asBoolean());
                assertEquals("访问成功", responseJson.get("message").asText());
                assertEquals("hello admin", responseJson.get("data").asText());
        }
}
