package io.chan.productservice.aop.named;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NamedLock {
    String prefix() default "default_";
    int timeout() default 1000;
}
