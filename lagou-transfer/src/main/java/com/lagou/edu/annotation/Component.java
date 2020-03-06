package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * @author Chenyuhua
 * @date 2020/3/6 2:48
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Component {
    String value() default "";
}
