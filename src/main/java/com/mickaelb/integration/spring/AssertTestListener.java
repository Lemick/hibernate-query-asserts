package com.mickaelb.integration.spring;

import org.springframework.test.context.TestContext;

public interface AssertTestListener {

   void beforeTestClass(TestContext testContext);
   void beforeTestMethod(TestContext testContext);
   void afterTestMethod(TestContext testContext);
}
