package com.jmsoftware.demotwo.math;

import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.demotwo.math.domain.DividePayload;
import com.jmsoftware.demotwo.math.domain.MultiplyPayload;
import com.jmsoftware.demotwo.math.service.MathService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>MathController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/17/20 8:19 AM
 **/
@RestController
@RequestMapping("/math")
@RequiredArgsConstructor
@Api(tags = {"Math Controller"})
public class MathController {
    private final MathService mathService;

    @GetMapping("/multiply")
    @ApiOperation(value = "/multiply", notes = "Multiplication operation")
    public ResponseBodyBean<Double> multiply(@Valid MultiplyPayload payload) {
        return ResponseBodyBean.ofDataAndMessage(mathService.multiply(payload.getParameterList()), "Done");
    }

    @GetMapping("/divide")
    @ApiOperation(value = "/divide", notes = "Division operation")
    public ResponseBodyBean<Double> divide(@Valid DividePayload payload) {
        return ResponseBodyBean.ofDataAndMessage(mathService.divide(payload.getParameter1(), payload.getParameter2()),
                                                 "Done");
    }
}
