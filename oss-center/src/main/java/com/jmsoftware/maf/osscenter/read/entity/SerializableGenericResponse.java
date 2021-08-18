package com.jmsoftware.maf.osscenter.read.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * Description: SerializableGenericResponse, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/12/2021 5:07 PM
 **/
@Data
@Setter(AccessLevel.PROTECTED)
public class SerializableGenericResponse {
    private Map<String, List<String>> headers;
    private String bucket;
    private String region;
    private String object;
}
