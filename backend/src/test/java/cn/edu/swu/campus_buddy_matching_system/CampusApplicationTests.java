package cn.edu.swu.campus_buddy_matching_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import cn.edu.swu.campus_buddy_matching_system.service.UserService;

@SpringBootTest
@ActiveProfiles("test")
class CampusApplicationTests {

	@MockBean
	private UserService userService;

	@Test
	void contextLoads() {
		// 测试 Spring 上下文能否成功加载
		System.out.println("Spring context loaded successfully!");
	}
}
