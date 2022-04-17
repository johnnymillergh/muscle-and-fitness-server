package com.jmsoftware.maf.springcloudstarter.validation.validator

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.ReflectUtil
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeConstraints
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeGroup
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeType
import java.lang.reflect.Field
import java.time.LocalDateTime
import java.util.*
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/**
 * # DateTimeRangeValidator
 *
 * Description: DateTimeRangeValidator, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 9:39 AM
 */
class DateTimeRangeValidator : ConstraintValidator<DateTimeRangeConstraints, Any> {
    companion object {
        const val MAX_GROUP_SIZE = 2
        private const val DEFAULT_HASH_MAP_CAPACITY = 8
        val log = logger()
    }

    override fun isValid(value: Any, context: ConstraintValidatorContext): Boolean {
        val annotatedFieldSet = getAnnotatedFieldSet(value)
        if (CollUtil.isEmpty(annotatedFieldSet)) {
            log.warn(
                "There is not fields annotated by {} in the class({})", value.javaClass.name,
                DateTimeRangeGroup::class.java.simpleName
            )
            return true
        }
        val dateTimeRangeGroupMap = HashMap<String, LinkedList<Field>>(DEFAULT_HASH_MAP_CAPACITY)
        for (field in annotatedFieldSet) {
            val annotation = field.getAnnotation(DateTimeRangeGroup::class.java)
            if (!dateTimeRangeGroupMap.containsKey(annotation.groupName)) {
                dateTimeRangeGroupMap[annotation.groupName] = CollUtil.newLinkedList(field)
            } else {
                dateTimeRangeGroupMap[annotation.groupName]!!.add(field)
                if (dateTimeRangeGroupMap[annotation.groupName]!!.size > MAX_GROUP_SIZE) {
                    log.error(
                        "The length of DateTimeRangeGroup({}) cannot exceed {}!", annotation.groupName,
                        MAX_GROUP_SIZE
                    )
                    return false
                }
            }
        }
        return validate(value, dateTimeRangeGroupMap)
    }

    private fun getAnnotatedFieldSet(value: Any): HashSet<Field> {
        val fields = value.javaClass.declaredFields
        val annotatedFieldSet = CollUtil.newHashSet<Field>()
        for (field in fields) {
            val annotation = field.getAnnotation(DateTimeRangeGroup::class.java)
            if (ObjectUtil.isNotNull(annotation)) {
                annotatedFieldSet.add(field)
            }
        }
        return annotatedFieldSet
    }

    private fun validate(value: Any, dateTimeRangeGroupMap: HashMap<String, LinkedList<Field>>): Boolean {
        for ((groupName, fieldList) in dateTimeRangeGroupMap) {
            if (fieldList.size != MAX_GROUP_SIZE) {
                log.error("The length of DateTimeRangeGroup({}) is not correct!", groupName)
                return false
            }
            val dateTimeRangeGroup = fieldList[0].getAnnotation(
                DateTimeRangeGroup::class.java
            )
            var startTime: Any? = null
            var endTime: Any? = null
            when (dateTimeRangeGroup.type) {
                DateTimeRangeType.START_TIME -> {
                    startTime = ReflectUtil.getFieldValue(value, fieldList[0])
                    endTime = ReflectUtil.getFieldValue(value, fieldList[1])
                }
                DateTimeRangeType.END_TIME -> {
                    startTime = ReflectUtil.getFieldValue(value, fieldList[1])
                    endTime = ReflectUtil.getFieldValue(value, fieldList[0])
                }
            }
            if (ObjectUtil.hasNull(startTime, endTime)) {
                return true
            }
            if (startTime is Date && endTime is Date && startTime.after(endTime)) {
                return false
            }
            if (startTime is LocalDateTime && endTime is LocalDateTime && startTime.isAfter(endTime)) {
                return false
            }
        }
        return true
    }
}
