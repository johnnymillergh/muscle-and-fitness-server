package com.jmsoftware.demoone.math;

import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.demoone.math.domain.AddPayload;
import com.jmsoftware.demoone.math.domain.SubtractPayload;
import com.jmsoftware.demoone.math.service.MathService;
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

    @GetMapping("/add")
    @ApiOperation(value = "/add", notes = "Addition operation")
    public ResponseBodyBean<Double> add(AddPayload payload) {
        return ResponseBodyBean.ofDataAndMessage(mathService.add(payload.getParameterList()), "Done");
    }

    @GetMapping("/subtract")
    @ApiOperation(value = "/subtract", notes = "Subtraction operation")
    public ResponseBodyBean<Double> subtract(@Valid SubtractPayload payload) {
        return ResponseBodyBean.ofDataAndMessage(mathService.subtract(payload.getParameter1(), payload.getParameter2()),
                                                 "Done");
    }
}
