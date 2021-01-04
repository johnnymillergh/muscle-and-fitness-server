package com.jmsoftware.maf.authcenter.security.controller;

import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtPayload;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
import com.jmsoftware.maf.common.exception.SecurityException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Description: JwtRemoteApiController, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/29/2020 11:04 AM
 **/
@Validated
@RestController
@RequiredArgsConstructor
@Api(tags = {"JWT Remote API"})
@RequestMapping("/jwt-remote-api")
public class JwtRemoteApiController {
    private final JwtService jwtService;

    /**
     * Parse response body bean.
     * <p>
     * TODO: remove ParseJwtPayload, get JWT from HTTP header
     *
     * @param payload the payload
     * @return the response body bean
     * @throws SecurityException the security exception
     */
    @PostMapping("/parse")
    @ApiOperation(value = "Parse JWT", notes = "Parse JWT (Remote API)")
    public ResponseBodyBean<ParseJwtResponse> parse(@Valid @RequestBody ParseJwtPayload payload) throws SecurityException {
        return ResponseBodyBean.ofSuccess(jwtService.parse(payload));
    }
}
