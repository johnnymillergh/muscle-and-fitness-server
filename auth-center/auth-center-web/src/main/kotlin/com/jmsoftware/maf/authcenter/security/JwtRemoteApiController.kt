package com.jmsoftware.maf.authcenter.security

import com.jmsoftware.maf.authcenter.security.service.JwtService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.security.ParseJwtResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * # JwtRemoteApiController
 *
 * Description: JwtRemoteApiController, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 3:09 PM
 */
@RestController
@RequestMapping("/jwt-remote-api")
class JwtRemoteApiController(
    private val jwtService: JwtService
) {
    /**
     * Parse response body bean.
     *
     * @return the response body bean
     */
    @GetMapping("/parse")
    fun parse(request: HttpServletRequest): ResponseBodyBean<ParseJwtResponse> =
        ResponseBodyBean.ofSuccess(jwtService.parse(request))
}
