package com.jmsoftware.maf.reactivespringbootstarter.service.impl;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.jmsoftware.maf.common.domain.ValidationTestPayload;
import com.jmsoftware.maf.reactivespringbootstarter.configuration.MafProjectProperty;
import com.jmsoftware.maf.reactivespringbootstarter.service.CommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

/**
 * <h1>CommonServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2/4/20 11:16 AM
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CommonServiceImpl implements CommonService {
    private final MafProjectProperty mafProjectProperty;

    @Override
    public JSON getApplicationInfo() {
        return JSONUtil.parse(this.mafProjectProperty);
    }

    @Override
    public void validateObject(@Valid ValidationTestPayload payload) {
        log.info("Validation passed! {}", payload);
    }
}
