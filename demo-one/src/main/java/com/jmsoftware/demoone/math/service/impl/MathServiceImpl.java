package com.jmsoftware.demoone.math.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.common.exception.BusinessException;
import com.jmsoftware.demoone.math.service.MathService;
import com.jmsoftware.demoone.remote.DemoTwoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * <h1>MathServiceImpl</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/17/20 8:32 AM
 **/
@Slf4j
@Service
@RequiredArgsConstructor
public class MathServiceImpl implements MathService {
    private final DemoTwoService demoTwoService;

    @Override
    public Double add(List<Double> parameterList) {
        if (CollectionUtil.isEmpty(parameterList)) {
            throw new BusinessException("parameterList is empty!");
        }
        var result = parameterList.stream().mapToDouble(parameter -> parameter).sum();
        log.info("Addition operation done. parameterList: {}, result: {}", parameterList, result);
        this.callRemote();
        return result;
    }

    @Override
    public Double subtract(Double parameter1, Double parameter2) {
        if (!ObjectUtil.isAllNotEmpty(parameter1, parameter2)) {
            throw new BusinessException(
                    String.format("Subtraction parameter is invalid! parameter1: %s, parameter2: %s",
                                  parameter1, parameter2));
        }
        return NumberUtil.sub(parameter1, parameter2);
    }

    private void callRemote() {
        var params1 = new HashMap<String, Object>(4);
        params1.put("parameter1", 1D);
        params1.put("parameter2", 2D);
        var response1 = demoTwoService.divide(params1);
        log.info("Called demo-two remote service. response: {}", response1);
    }
}
