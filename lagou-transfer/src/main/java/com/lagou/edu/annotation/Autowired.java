package com.lagou.edu.annotation;

import java.lang.annotation.*;

/**
 * 自动注入注解
 *
 * @author Chenyuhua
 * @date 2020/3/6 0:30
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {
    /**
     * 综合Qualifier 按name注入
     *
     * @return
     */
    String value() default "";
}
