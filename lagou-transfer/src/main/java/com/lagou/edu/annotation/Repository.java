package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * @author Chenyuhua
 * @date 2020/3/6 0:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Repository {
    String value() default "";
}
