package com.jmsoftware.maf.authcenter.user.payload;

import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus;
import com.jmsoftware.maf.common.domain.authcenter.user.UserStatus2;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.ValidEnumValue;
import lombok.Data;

/**
 * <h1>GetUserStatusPayload</h1>
 * <p>
 * Change description here.
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 6/6/21 4:31 PM
 **/
@Data
public class GetUserStatusPayload {
    @ValidEnumValue(targetEnum = UserStatus.class)
    private Byte status;
    @ValidEnumValue(targetEnum = UserStatus2.class)
    private Byte status2;
}
