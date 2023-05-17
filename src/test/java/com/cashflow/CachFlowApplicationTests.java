package com.cashflow;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CachFlowApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> CachFlowApplication.main(new String[0]));
	}

}
