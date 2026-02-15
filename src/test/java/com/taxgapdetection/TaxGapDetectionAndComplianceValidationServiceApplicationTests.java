package com.taxgapdetection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(TaxGapDetectionAndComplianceValidationServiceApplication.class)
class TaxGapDetectionAndComplianceValidationServiceApplicationTests {

	@Test
	void contextLoads() {
        Assertions.assertDoesNotThrow(()-> TaxGapDetectionAndComplianceValidationServiceApplication.main(new String[]{}));

	}

}
