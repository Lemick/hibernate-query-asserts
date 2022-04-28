package com.mickaelb.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AssertHibernateL2CCount {

    int hits() default 0;

    int misses() default 0;

    int puts() default 0;
}
