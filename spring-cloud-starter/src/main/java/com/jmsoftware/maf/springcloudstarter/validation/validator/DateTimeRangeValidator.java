package com.jmsoftware.maf.springcloudstarter.validation.validator;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeConstraints;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeGroup;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

/**
 * Description: DateTimeRangeValidator, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 6/3/2021 2:10 PM
 **/
@Slf4j
public class DateTimeRangeValidator implements ConstraintValidator<DateTimeRangeConstraints, Object> {
    public static final int MAX_GROUP_SIZE = 2;
    private static final int DEFAULT_HASH_MAP_CAPACITY = 8;

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        val annotatedFieldSet = this.getAnnotatedFieldSet(value);
        if (CollUtil.isEmpty(annotatedFieldSet)) {
            log.warn("There is not fields annotated by {} in the class({})", value.getClass().getName(),
                     DateTimeRangeGroup.class.getSimpleName());
            return true;
        }
        val dateTimeRangeGroupMap = new HashMap<String, LinkedList<Field>>(DEFAULT_HASH_MAP_CAPACITY);
        for (val field : annotatedFieldSet) {
            val annotation = field.getAnnotation(DateTimeRangeGroup.class);
            if (!dateTimeRangeGroupMap.containsKey(annotation.groupName())) {
                dateTimeRangeGroupMap.put(annotation.groupName(), CollUtil.newLinkedList(field));
            } else {
                dateTimeRangeGroupMap.get(annotation.groupName()).add(field);
                if (dateTimeRangeGroupMap.get(annotation.groupName()).size() > MAX_GROUP_SIZE) {
                    log.error("The length of DateTimeRangeGroup({}) cannot exceed {}!", annotation.groupName(),
                              MAX_GROUP_SIZE);
                    return false;
                }
            }
        }
        return this.validate(value, dateTimeRangeGroupMap);
    }

    private HashSet<Field> getAnnotatedFieldSet(Object value) {
        val fields = value.getClass().getDeclaredFields();
        final HashSet<Field> annotatedFieldSet = CollUtil.newHashSet();
        for (val field : fields) {
            val annotation = field.getAnnotation(DateTimeRangeGroup.class);
            if (ObjectUtil.isNotNull(annotation)) {
                annotatedFieldSet.add(field);
            }
        }
        return annotatedFieldSet;
    }

    private boolean validate(Object value, HashMap<String, LinkedList<Field>> dateTimeRangeGroupMap) {
        for (val entry : dateTimeRangeGroupMap.entrySet()) {
            val groupName = entry.getKey();
            val fieldList = entry.getValue();
            if (fieldList.size() != MAX_GROUP_SIZE) {
                log.error("The length of DateTimeRangeGroup({}) is not correct!", groupName);
                return false;
            }
            val dateTimeRangeGroup = fieldList.get(0).getAnnotation(DateTimeRangeGroup.class);
            Object startTime = null;
            Object endTime = null;
            switch (dateTimeRangeGroup.type()) {
                case START_TIME:
                    startTime = ReflectUtil.getFieldValue(value, fieldList.get(0));
                    endTime = ReflectUtil.getFieldValue(value, fieldList.get(1));
                    break;
                case END_TIME:
                    startTime = ReflectUtil.getFieldValue(value, fieldList.get(1));
                    endTime = ReflectUtil.getFieldValue(value, fieldList.get(0));
                    break;
                default:
            }
            if (ObjectUtil.hasNull(startTime, endTime)) {
                return true;
            }
            if (startTime instanceof Date && endTime instanceof Date
                    && ((Date) startTime).after((Date) endTime)) {
                return false;
            }
            if (startTime instanceof LocalDateTime && endTime instanceof LocalDateTime
                    && ((LocalDateTime) startTime).isAfter((LocalDateTime) endTime)) {
                return false;
            }
        }
        return true;
    }
}
