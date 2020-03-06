package com.lagou.edu.factory;

import com.lagou.edu.annotation.Autowired;
import com.lagou.edu.annotation.Component;
import com.lagou.edu.annotation.Transactional;
import com.lagou.edu.utils.TransactionManager;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author 应癫
 * <p>
 * <p>
 * 代理对象工厂：生成代理对象的
 */
@Component("proxyFactory")
public class ProxyFactory {
    private static final Logger logger = LoggerFactory.getLogger(ProxyFactory.class);
    @Autowired
    private TransactionManager transactionManager;

    /**
     * 根据是否实现接口选择代理模式
     *
     * @param obj
     * @return
     */
    public Object getProxy(Object obj) {
        Class<?>[] interfaces = obj.getClass().getInterfaces();
        Annotation[] annotations = obj.getClass().getAnnotations();
        Boolean flag = false;
        for (Annotation annotation : annotations) {
            if (annotation instanceof Transactional) {
                flag = true;
            }
        }

        if (interfaces.length > 0) {
            return getJdkProxy(obj, flag);
        }
        return getCglibProxy(obj, flag);
    }

    /**
     * Jdk动态代理
     *
     * @param obj 委托对象
     * @return 代理对象
     */
    public Object getJdkProxy(Object obj, Boolean flag) {
        if (flag) {
            return getAllJdkProxyTran(obj);
        }
        return getMethodJdkProxyTran(obj);
    }

    public Object getMethodJdkProxyTran(Object obj) {
        // 获取代理对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Annotation[] annotations = method.getAnnotations();
                        for (Annotation annotation : annotations) {
                            if (annotation instanceof Transactional) {
                                return doTran(obj, method, args);
                            }
                        }
                        return method.invoke(obj, args);
                    }
                });
    }

    public Object doTran(Object obj, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            // 开启事务(关闭事务的自动提交)
            transactionManager.beginTransaction();
            result = method.invoke(obj, args);
            // 提交事务
            transactionManager.commit();
        } catch (Exception e) {
            logger.error("tran error:obj: {} method:{},args:{}", obj, method, args, e);
            // 回滚事务
            transactionManager.rollback();
            // 抛出异常便于上层servlet捕获
            throw e;
        }
        return result;
    }

    public Object getAllJdkProxyTran(Object obj) {
        // 获取代理对象
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(), obj.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        return doTran(obj, method, args);
                    }
                });
    }


    /**
     * 使用cglib动态代理生成代理对象
     *
     * @param obj 委托对象
     * @return
     */
    public Object getCglibProxy(Object obj, Boolean flag) {
        if (flag) {
            return getALlCglibProxy(obj);
        }
        return getMethodCglibProxy(obj);
    }

    public Object getMethodCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                Annotation[] annotations = method.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (annotation instanceof Transactional) {
                        return doCglibTran(obj, method, objects);
                    }
                }
                return method.invoke(obj, objects);
            }
        });
    }

    public Object getALlCglibProxy(Object obj) {
        return Enhancer.create(obj.getClass(), new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return doCglibTran(obj, method, objects);
            }
        });
    }

    public Object doCglibTran(Object obj, Method method, Object[] objects) throws Throwable {
        Object result = null;
        try {
            // 开启事务(关闭事务的自动提交)
            transactionManager.beginTransaction();

            result = method.invoke(obj, objects);

            // 提交事务

            transactionManager.commit();
        } catch (Exception e) {
            logger.error("tran error:obj: {} method:{},args:{}", obj, method, objects, e);
            // 回滚事务
            transactionManager.rollback();

            // 抛出异常便于上层servlet捕获
            throw e;

        }
        return result;
    }
}
