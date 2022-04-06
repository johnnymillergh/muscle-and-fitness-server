package com.jmsoftware.maf.springcloudstarter.quartz.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.jmsoftware.maf.springcloudstarter.quartz.annotation.QuartzSchedulable;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Quartz job invocation util.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 11:56 AM
 */
@SuppressWarnings("unused")
public class QuartzJobInvocationUtil {
    private QuartzJobInvocationUtil() {
    }

    /**
     * Invoke method.
     *
     * @param quartzJobConfiguration the quartz job configuration
     */
    @SneakyThrows
    public static void invokeMethod(QuartzJobConfiguration quartzJobConfiguration) {
        val invokeTarget = quartzJobConfiguration.getInvokeTarget();
        val beanName = getBeanName(invokeTarget);
        val methodName = getMethodName(invokeTarget);
        val methodParams = getMethodParams(invokeTarget);

        Object bean;
        if (!isValidClassName(beanName)) {
            bean = SpringUtil.getBean(beanName);
        } else {
            bean = Class.forName(beanName).getDeclaredConstructor().newInstance();
        }
        invokeMethod(bean, methodName, methodParams);
    }

    /**
     * Invoke method.
     *
     * @param bean         the bean
     * @param methodName   the method name
     * @param methodParams the method params
     * @throws SecurityException        the security exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    private static void invokeMethod(
            Object bean,
            String methodName,
            List<Object[]> methodParams
    ) {
        val method = ReflectUtil.getMethodByName(bean.getClass(), methodName);
        if (!AnnotationUtil.hasAnnotation(method, QuartzSchedulable.class)) {
            throw new IllegalStateException(
                    "The method is not quartz-schedulable. Unable to invoke the method: " + method);
        }
        if (CollUtil.isNotEmpty(methodParams)) {
            ReflectUtil.invoke(bean, methodName, methodParams.toArray());
        } else {
            ReflectUtil.invoke(bean, methodName);
        }
    }

    /**
     * Is valid class name boolean.
     *
     * @param invokeTarget the invoke target
     * @return the boolean
     */
    public static boolean isValidClassName(String invokeTarget) {
        return StringUtils.countOccurrencesOf(invokeTarget, ".") > 1;
    }

    /**
     * Gets bean name.
     *
     * @param invokeTarget the invoke target
     * @return the bean name
     */
    public static String getBeanName(String invokeTarget) {
        val beanName = StrUtil.subBefore(invokeTarget, "(", false);
        return StrUtil.subBefore(beanName, ".", false);
    }

    /**
     * Gets method name.
     *
     * @param invokeTarget the invoke target
     * @return the method name
     */
    public static String getMethodName(String invokeTarget) {
        val methodName = StrUtil.subBefore(invokeTarget, "(", false);
        return StrUtil.subAfter(methodName, ".", false);
    }

    /**
     * Gets method params.
     *
     * @param invokeTarget the invoke target
     * @return the method params
     */
    public static List<Object[]> getMethodParams(String invokeTarget) {
        val methodStr = StrUtil.subBetween(invokeTarget, "(", ")");
        if (StrUtil.isBlank(methodStr)) {
            return Collections.emptyList();
        }
        val methodParamArray = methodStr.split(",");
        val paramList = new LinkedList<Object[]>();
        for (val s : methodParamArray) {
            val trimmedParam = StrUtil.trimToEmpty(s);
            // String type
            if (StrUtil.contains(trimmedParam, "'")) {
                paramList.add(new Object[]{StrUtil.replace(trimmedParam, "'", ""), String.class});
            }
            // Boolean type
            else if (StrUtil.equals(trimmedParam, "true") || StrUtil.equals(trimmedParam, "false")) {
                paramList.add(new Object[]{Boolean.valueOf(trimmedParam), Boolean.class});
            }
            // Long type
            else if (StrUtil.contains(trimmedParam, "L")) {
                paramList.add(
                        new Object[]{Long.valueOf(StrUtil.subPre(trimmedParam, trimmedParam.length())), Long.class});
            }
            // Double type
            else if (StrUtil.contains(trimmedParam, "D")) {
                paramList.add(new Object[]{Double.valueOf(
                        StrUtil.subPre(trimmedParam, trimmedParam.length())), Double.class});
            }
            // Integer type
            else {
                paramList.add(new Object[]{Integer.valueOf(trimmedParam), Integer.class});
            }
        }
        return paramList;
    }

    /**
     * Get method params type class [ ].
     *
     * @param methodParams the method params
     * @return the class [ ]
     */
    public static Class<?>[] getMethodParamsType(List<Object[]> methodParams) {
        val clazz = new Class<?>[methodParams.size()];
        var index = 0;
        for (val os : methodParams) {
            clazz[index] = (Class<?>) os[1];
            index++;
        }
        return clazz;
    }

    /**
     * Get method params value object [ ].
     *
     * @param methodParams the method params
     * @return the object [ ]
     */
    public static Object[] getMethodParamsValue(List<Object[]> methodParams) {
        val clazz = new Object[methodParams.size()];
        var index = 0;
        for (val os : methodParams) {
            clazz[index] = os[0];
            index++;
        }
        return clazz;
    }
}
