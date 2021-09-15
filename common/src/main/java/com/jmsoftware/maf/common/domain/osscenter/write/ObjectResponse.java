package com.jmsoftware.maf.common.domain.osscenter.write;

import lombok.Data;

/**
 * Description: ObjectResponse, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 8/12/2021 9:27 PM
 **/
@Data
public class ObjectResponse {
    private String bucket;
    private String object;
    private String etag;
}
