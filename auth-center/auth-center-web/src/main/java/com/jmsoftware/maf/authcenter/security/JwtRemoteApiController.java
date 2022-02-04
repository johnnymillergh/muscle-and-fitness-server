package com.jmsoftware.maf.authcenter.security;

import com.jmsoftware.maf.authcenter.security.service.JwtService;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse;
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
@RequestMapping("/jwt-remote-api")
public class JwtRemoteApiController {
    private final JwtService jwtService;

    /**
     * Parse response body bean.
     *
     * @return the response body bean
     */
    @GetMapping("/parse")
    public ResponseBodyBean<ParseJwtResponse> parse(HttpServletRequest request) {
        return ResponseBodyBean.ofSuccess(this.jwtService.parse(request));
    }
}
