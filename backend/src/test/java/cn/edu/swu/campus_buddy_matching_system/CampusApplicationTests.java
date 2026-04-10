package cn.edu.swu.campus_buddy_matching_system;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class CampusApplicationTests {

	@Test
	void contextLoads() {
		// 测试 Spring 上下文能否成功加载
		System.out.println("Spring context loaded successfully!");
	}
}
