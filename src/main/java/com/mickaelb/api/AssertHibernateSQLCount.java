package com.mickaelb.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertHibernateSQLCount {

    int selects() default 0;

    int inserts() default 0;

    int updates() default 0;

    int deletes() default 0;
}
