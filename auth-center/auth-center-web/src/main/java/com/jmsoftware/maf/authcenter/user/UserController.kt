package com.jmsoftware.maf.authcenter.user

import com.jmsoftware.maf.authcenter.user.service.UserDomainService
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.user.LoginPayload
import com.jmsoftware.maf.common.domain.authcenter.user.LoginResponse
import com.jmsoftware.maf.common.domain.authcenter.user.SignupPayload
import com.jmsoftware.maf.common.domain.authcenter.user.SignupResponse
import lombok.RequiredArgsConstructor
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

/**
 * # UserController
 *
 * Controller implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 3:10 PM
 */
@Validated
@RestController
@RequiredArgsConstructor
class UserController(
    private val userDomainService: UserDomainService
) {
    @PostMapping("/users/signup")
    fun signup(@Valid @RequestBody payload: SignupPayload): ResponseBodyBean<SignupResponse> =
        ResponseBodyBean.ofSuccess(userDomainService.saveUserForSignup(payload))

    @PostMapping("/users/login")
    fun login(@Valid @RequestBody payload: LoginPayload): ResponseBodyBean<LoginResponse> =
        ResponseBodyBean.ofSuccess(userDomainService.login(payload))

    @PostMapping("/users/logout")
    fun logout(request: HttpServletRequest): ResponseBodyBean<Boolean> =
        ResponseBodyBean.ofSuccess(userDomainService.logout(request))
}
