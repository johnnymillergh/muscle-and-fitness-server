package com.jmsoftware.authcenter;

import com.jmsoftware.common.bean.ResponseBodyBean;
import com.jmsoftware.common.domain.LoginPayload;
import com.jmsoftware.common.domain.LoginResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <h1>AuthController</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 3/14/20 9:00 AM
 **/
@RestController
public class AuthController {
    @PostMapping("/login")
    public ResponseBodyBean<LoginResult> login(@Valid @RequestBody LoginPayload payload) {
        LoginResult result = new LoginResult();
        result.setUid("ijohnnymiller");
        result.setToken("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
                        ".eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG5ueSBNaWxsZXIiLCJpYXQiOjE1MTYyMzkwMjIsImlkIjoxMTEyfQ.wLZClT_dntoiB3-HYH1wWav0S_nBmHn7xunxaDjPM1Q");
        return ResponseBodyBean.ofSuccess(result);
    }
}
