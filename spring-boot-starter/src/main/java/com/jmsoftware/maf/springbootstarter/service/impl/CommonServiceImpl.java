package com.jmsoftware.maf.springbootstarter.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.maf.common.domain.ValidationTestPayload;
import com.jmsoftware.maf.springbootstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.springbootstarter.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public JSON getApplicationInfo() {
        return JSONUtil.parse(mafProjectProperty);
    }

    @Override
    public void validateObject(@Valid ValidationTestPayload payload) {
        log.info("Validation passed! {}", payload);
    }
}
