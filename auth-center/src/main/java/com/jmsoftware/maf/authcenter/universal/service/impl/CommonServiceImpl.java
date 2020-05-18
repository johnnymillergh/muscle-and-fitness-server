package com.jmsoftware.maf.authcenter.universal.service.impl;

import com.jmsoftware.maf.authcenter.universal.aspect.ValidateArgument;
import com.jmsoftware.maf.authcenter.universal.configuration.ProjectProperty;
import com.jmsoftware.maf.authcenter.universal.domain.ValidationTestPayload;
import com.jmsoftware.maf.authcenter.universal.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <h1>CommonServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/4/20 11:16 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final ProjectProperty projectProperty;

    @Override
    public Map<String, Object> getApplicationInfo() {
        var map = new HashMap<String, Object>(16);
        var fieldsInfo = getFieldsInfo(projectProperty);
        fieldsInfo.forEach(fieldInfo -> {
            var type = fieldInfo.get("type");
            if ("class java.lang.String".equals(type)) {
                map.put((String) fieldInfo.get("name"), fieldInfo.get("value"));
            }
        });
        return map;
    }

    @Override
    @ValidateArgument
    public void validateObject(@Valid ValidationTestPayload payload) {
        log.info("Validation passed! {}", payload);
    }

    /**
     * Gets field value by name.
     *
     * @param fieldName the field name
     * @param object    the object
     * @return the field value by name
     * @see <a href='https://blog.csdn.net/sfhinsc/article/details/83790741'>Java 中遍历一个对象的所有属性</a>
     */
    private Object getFieldValueByName(String fieldName, Object object) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter);
            return method.invoke(object);
        } catch (Exception e) {
            log.error("Can't get field's value by name! Cause: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get filed name string [ ].
     *
     * @param o the o
     * @return the string [ ]
     * @see <a href='https://blog.csdn.net/sfhinsc/article/details/83790741'>Java 中遍历一个对象的所有属性</a>
     */
    private String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            log.info("fields[i].getType(): {}", fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * Get Fields Info
     *
     * @param o the o
     * @return the fields info
     * @see <a href='https://blog.csdn.net/sfhinsc/article/details/83790741'>Java 中遍历一个对象的所有属性</a>
     */
    private List<Map<String, Object>> getFieldsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        var arrayList = new ArrayList<Map<String, Object>>();
        for (Field field : fields) {
            var infoMap = new HashMap<String, Object>(16);
            infoMap.put("type", field.getType().toString());
            infoMap.put("name", field.getName());
            infoMap.put("value", getFieldValueByName(field.getName(), o));
            arrayList.add(infoMap);
        }
        return arrayList;
    }

    /**
     * Get Filed Values
     *
     * @param o the o
     * @return the object [ ]
     * @see <a href='https://blog.csdn.net/sfhinsc/article/details/83790741'>Java 中遍历一个对象的所有属性</a>
     */
    public Object[] getFiledValues(Object o) {
        String[] fieldNames = this.getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = this.getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }
}
