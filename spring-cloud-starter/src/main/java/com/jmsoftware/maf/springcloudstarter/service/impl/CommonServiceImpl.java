package com.jmsoftware.maf.springcloudstarter.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.maf.common.domain.ValidationTestPayload;
import com.jmsoftware.maf.springcloudstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springcloudstarter.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * <h1>CommonServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 12/29/2020 1:47 PM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final MafProjectProperty mafProjectProperty;
    @Value("${greeting:Hello, World! (Embedded in Java)}")
    private String greeting;

    @Override
    public JSON getApplicationInfo() {
        return JSONUtil.parseObj(this.mafProjectProperty).set("greeting", this.greeting);
    }

    @Override
    public void validateObject(@Valid ValidationTestPayload payload) {
        log.info("Validation passed! {}", payload);
    }
}
