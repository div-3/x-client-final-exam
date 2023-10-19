package ru.inno.xclient;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class XClientApplicationTests {

	@Test
	void contextLoads() {
		assertEquals(4, 2 + 2);
	}

}
