package com.jmsoftware.demotwo.math.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import com.jmsoftware.common.exception.BusinessException;
import com.jmsoftware.demotwo.math.service.MathService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
public class MathServiceImpl implements MathService {
    @Override
    public Double multiply(List<Double> parameterList) {
        if (CollectionUtil.isEmpty(parameterList)) {
            throw new BusinessException("Multiplication's parameterList is empty!");
        }
        var result = parameterList.stream().mapToDouble(parameter -> parameter).reduce(1, (a, b) -> a * b);
        log.info("Multiplication operation done. parameterList: {}, result: {}", parameterList, result);
        return result;
    }

    @Override
    public Double divide(Double parameter1, Double parameter2) {
        if (!ObjectUtil.isAllNotEmpty(parameter1, parameter2)) {
            throw new BusinessException(
                    String.format("Division parameter(s) is invalid! parameter1: %s, parameter2: %s",
                                  parameter1, parameter2));
        }
        var result = NumberUtil.div(parameter1, parameter2);
        log.info("Division operation done. parameter1: {}, parameter2: {}, result: {}", parameter1, parameter2, result);
        return result;
    }
}
