package com.jmsoftware.maf.authcenter.user.entity;

import com.jmsoftware.maf.common.bean.PaginationBase;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * Description: GetUserPageList, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 4:32 PM
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class GetUserPageListPayload extends PaginationBase {
    private String username;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
