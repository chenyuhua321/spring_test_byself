package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * Controller层注解
 *
 * @author Chenyuhua
 * @date 2020/3/6 0:52
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface  Controller {
    String value() default "";
}
