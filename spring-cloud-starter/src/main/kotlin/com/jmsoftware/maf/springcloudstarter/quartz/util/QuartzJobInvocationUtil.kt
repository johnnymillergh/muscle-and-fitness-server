package com.jmsoftware.maf.springcloudstarter.quartz.util

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ReflectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.jmsoftware.maf.common.function.requireTrue
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.quartz.annotation.QuartzSchedulable
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import com.jmsoftware.maf.springcloudstarter.quartz.util.QuartzJobInvocationUtil.log
import org.springframework.util.StringUtils
import java.util.*

/**
 * The type Quartz job invocation util.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/24/2021 11:56 AM
 */
internal object QuartzJobInvocationUtil {
    val log = logger()
}

/**
 * Invoke method.
 *
 * @param quartzJobConfiguration the quartz job configuration
 */
fun invokeMethod(quartzJobConfiguration: QuartzJobConfiguration) {
    val invokeTarget = quartzJobConfiguration.invokeTarget
    val beanName = getBeanName(invokeTarget)
    val methodName = getMethodName(invokeTarget)
    val methodParams = getMethodParams(invokeTarget!!)
    if (!isValidClassName(beanName)) {
        log.atDebug().log { "Getting the bean from Spring IoC container by bean name: `$beanName`" }
        SpringUtil.getBean(beanName)
    } else {
        log.atDebug().log { "Initialize a new object by class name: `$beanName`" }
        Class.forName(beanName).getDeclaredConstructor().newInstance()
    }.let {
        log.atDebug().log { "Found the bean (`$beanName`) from Spring IoC container, $it" }
        invokeMethod(it, methodName, methodParams)
    }
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
private fun invokeMethod(
    bean: Any,
    methodName: String,
    methodParams: List<Array<Any>>
) {
    val method = ReflectUtil.getMethodByName(bean.javaClass, methodName)
    log.atDebug().log { "Got method by reflection: $method, params: $methodParams" }
    val quartzSchedulable = method.getAnnotation(QuartzSchedulable::class.java)
    requireTrue(quartzSchedulable != null) { annotated: Boolean ->
        log.atDebug().log { "The method is annotated by `@QuartzSchedulable`: $annotated" }
    }.orElseThrow { IllegalAccessError("The method is NOT quartz-schedulable. Unable to invoke the method: $method") }
    if (CollUtil.isNotEmpty(methodParams)) {
        log.atDebug().log { "Invoking method: $method, with params: $methodParams" }
        ReflectUtil.invoke<Any>(bean, methodName, *methodParams.toTypedArray())
    } else {
        log.atDebug().log { "Invoking method: $method, WITHOUT params" }
        ReflectUtil.invoke<Any>(bean, methodName)
    }
}

/**
 * Is valid class name boolean.
 *
 * @param invokeTarget the invoke target
 * @return the boolean
 */
fun isValidClassName(invokeTarget: String): Boolean {
    return StringUtils.countOccurrencesOf(invokeTarget, ".") > 1
}

/**
 * Gets bean name. For example:
 *  * `greetingBean.hello()`
 *  * `com.jmsoftware.maf.springcloudstarter.quartz.GreetingBean.hello()`
 *
 * @param invokeTarget the invoke target
 * @return the bean name
 */
fun getBeanName(invokeTarget: String?): String {
    val beanName = StrUtil.subBefore(invokeTarget, "(", false)
    return StrUtil.subBefore(beanName, ".", false)
}

/**
 * Gets method name.
 *
 * @param invokeTarget the invoke target
 * @return the method name
 */
fun getMethodName(invokeTarget: String?): String {
    val methodName = StrUtil.subBefore(invokeTarget, "(", false)
    return StrUtil.subAfter(methodName, ".", false)
}

/**
 * Gets method params.
 *
 * @param invokeTarget the invoke target
 * @return the method params
 */
fun getMethodParams(invokeTarget: String): List<Array<Any>> {
    val methodStr = StrUtil.subBetween(invokeTarget, "(", ")")
    if (StrUtil.isBlank(methodStr)) {
        return emptyList()
    }
    val methodParamArray = methodStr.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
    val paramList = LinkedList<Array<Any>>()
    for (s in methodParamArray) {
        val trimmedParam = StrUtil.trimToEmpty(s)
        // String type
        if (StrUtil.contains(trimmedParam, "'")) {
            paramList.add(arrayOf(StrUtil.replace(trimmedParam, "'", ""), String::class.java))
        } else if (StrUtil.equals(trimmedParam, "true") || StrUtil.equals(trimmedParam, "false")) {
            paramList.add(arrayOf(java.lang.Boolean.valueOf(trimmedParam), Boolean::class.java))
        } else if (StrUtil.contains(trimmedParam, "L")) {
            paramList.add(
                arrayOf(
                    java.lang.Long.valueOf(StrUtil.subPre(trimmedParam, trimmedParam.length)),
                    Long::class.java
                )
            )
        } else if (StrUtil.contains(trimmedParam, "D")) {
            paramList.add(
                arrayOf(
                    java.lang.Double.valueOf(
                        StrUtil.subPre(trimmedParam, trimmedParam.length)
                    ), Double::class.java
                )
            )
        } else {
            paramList.add(arrayOf(Integer.valueOf(trimmedParam), Int::class.java))
        }
    }
    return paramList
}
