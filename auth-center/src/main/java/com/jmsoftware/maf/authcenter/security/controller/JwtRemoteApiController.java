package com.jmsoftware.maf.authcenter.security.controller;

import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.exception.SecurityException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Description: JwtRemoteApiController, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/29/2020 11:04 AM
 **/
@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"JWT Remote API"})
@RequestMapping("/jwt-remote-api")
public class JwtRemoteApiController {
    private final JwtService jwtService;

    /**
     * Parse response body bean.
     *
     * @return the response body bean
     */
    @GetMapping("/parse")
    @ApiOperation(value = "Parse JWT", notes = "Parse JWT (Remote API)")
    public ResponseBodyBean<ParseJwtResponse> parse(HttpServletRequest request) throws SecurityException {
        return ResponseBodyBean.ofSuccess(
                new ParseJwtResponse().setUsername(jwtService.getUsernameFromRequest(request)));
    }
}
