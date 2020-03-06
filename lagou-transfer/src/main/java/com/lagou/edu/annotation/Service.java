package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * Service层注入注解
 *
 * @author Chenyuhua
 * @date 2020/3/6 0:32
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {

    String value() default "";
}
