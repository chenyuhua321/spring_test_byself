package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * @author Chenyuhua
 * @date 2020/3/6 3:19
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Transactional {
}
