package com.jmsoftware.maf.authcenter.user

import com.jmsoftware.maf.authcenter.user.payload.GetUserPageListPayload
import com.jmsoftware.maf.authcenter.user.payload.GetUserStatusPayload
import com.jmsoftware.maf.authcenter.user.persistence.User
import com.jmsoftware.maf.authcenter.user.service.UserDomainService
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.domain.authcenter.user.GetUserByLoginTokenResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

/**
 * # UserRemoteApiController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 3:21 PM
 */
@Validated
@RestController
@RequestMapping("/user-remote-api")
class UserRemoteApiController(
    private val userDomainService: UserDomainService
) {
    @GetMapping("/users/{loginToken}")
    fun getUserByLoginToken(@PathVariable loginToken: String): ResponseBodyBean<GetUserByLoginTokenResponse> =
        ResponseBodyBean.ofSuccess(userDomainService.getUserByLoginToken(loginToken))

    @GetMapping("/users")
    fun getUserPageList(@Valid payload: GetUserPageListPayload): PageResponseBodyBean<User> =
        userDomainService.getUserPageList(payload)

    @GetMapping("/users/status")
    fun getUserStatus(payload: @Valid GetUserStatusPayload): ResponseBodyBean<String> =
        ResponseBodyBean.ofSuccess(userDomainService.getUserStatus(payload), "Correct enum value")
}
