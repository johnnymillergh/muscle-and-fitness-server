package com.jmsoftware.demoone.remote;

import com.jmsoftware.common.bean.ResponseBodyBean;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * <h1>DemoTwoService</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2/17/20 9:42 AM
 */
@FeignClient(name = "demo-two")
//@Qualifier(value = "demoTwoService")
public interface DemoTwoService {
    /**
     * Multiply response body bean.
     *
     * @param params the params
     * @return the response body bean
     */
    @RequestMapping(value = "/math/multiply", method = GET)
    @Headers("Content-Type: application/json")
    ResponseBodyBean<Double> multiply(@RequestParam Map<String, Object> params);

    /**
     * Divide response body bean.
     *
     * @param params the params
     * @return the response body bean
     */
    @RequestMapping(value = "/math/divide", method = GET)
    @Headers("Content-Type: application/json")
    ResponseBodyBean<Double> divide(@RequestParam Map<String, Object> params);
}
