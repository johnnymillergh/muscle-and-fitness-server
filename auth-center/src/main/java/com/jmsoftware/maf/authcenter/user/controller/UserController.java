package com.jmsoftware.maf.authcenter.user.controller;

import com.jmsoftware.maf.authcenter.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

 /**
 * <h1>UserController</h1>
 * <p>
 * Controller implementation of UserPersistence.(UserPersistence)
 *
 * @author Johnny Miller (鍾俊)
 * @date 2020-05-10 12:08:28
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
}
