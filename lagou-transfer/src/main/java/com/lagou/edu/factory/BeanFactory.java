package com.lagou.edu.factory;

import com.lagou.edu.annotation.*;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * @author 应癫
 * <p>
 * 工厂类，生产对象（使用反射技术）
 */
public class BeanFactory {

    /**
     * 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
     * 任务二：对外提供获取实例对象的接口（根据id获取）
     */

    private static Map<String, Object> map = new HashMap<>();  // 存储对象


    static {
        // 任务一：读取解析xml，通过反射技术实例化对象并且存储待用（map集合）
        // 加载xml
        Reflections reflections = new org.reflections.Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage("com.lagou.edu"))
                        .addScanners(new FieldAnnotationsScanner()));
        //扫描需要加入容器管理的类
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(Service.class);
        Set<Class<?>> rclasses = reflections.getTypesAnnotatedWith(Repository.class);
        Set<Class<?>> cTclasses = reflections.getTypesAnnotatedWith(Controller.class);
        Set<Class<?>> coTclasses = reflections.getTypesAnnotatedWith(Component.class);
        try {
            for (Class clazz : rclasses) {
                Repository annotation = (Repository) clazz.getAnnotation(Repository.class);
                map.put("".equalsIgnoreCase(annotation.value()) ? toLowerCaseFirstOne(clazz.getSimpleName()) : annotation.value(), clazz.newInstance());
            }

            for (Class clazz : classes) {
                Service annotation = (Service) clazz.getAnnotation(Service.class);
                map.put("".equalsIgnoreCase(annotation.value()) ? toLowerCaseFirstOne(clazz.getSimpleName()) : annotation.value(), clazz.newInstance());
            }

            for (Class clazz : cTclasses) {
                Controller annotation = (Controller) clazz.getAnnotation(Controller.class);
                map.put("".equalsIgnoreCase(annotation.value()) ? toLowerCaseFirstOne(clazz.getSimpleName()) : annotation.value(), clazz.newInstance());
            }

            for (Class clazz : coTclasses) {
                Component annotation = (Component) clazz.getAnnotation(Component.class);
                map.put("".equalsIgnoreCase(annotation.value()) ? toLowerCaseFirstOne(clazz.getSimpleName()) : annotation.value(), clazz.newInstance());
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (Object o : map.values()) {
            Field[] declaredFields = o.getClass().getDeclaredFields();

            for (Field field : declaredFields) {
                Annotation[] annotations = field.getAnnotations();
                if (Objects.isNull(annotations) || annotations.length == 0) {
                    continue;
                }
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Autowired) {
                        try {
                            field.setAccessible(true);
                            field.set(o, map.get("".equalsIgnoreCase(field.getAnnotation(Autowired.class).value()) ? field.getName():field.getAnnotation(Autowired.class).value()));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }}
                }
            }
        }

    }


    // 任务二：对外提供获取实例对象的接口（根据id获取）
    public static Object getBean(String id) {
        return map.get(id);
    }

    private static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0))) {
            return s;
        }
        return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
    }

}
