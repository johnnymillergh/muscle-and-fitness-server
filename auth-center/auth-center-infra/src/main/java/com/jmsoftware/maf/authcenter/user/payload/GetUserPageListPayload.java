package com.jmsoftware.maf.authcenter.user.payload;

import com.jmsoftware.maf.common.bean.PaginationBase;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeConstraints;
import com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeGroup;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import static com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeType.END_TIME;
import static com.jmsoftware.maf.springcloudstarter.validation.annotation.DateTimeRangeType.START_TIME;

/**
 * Description: GetUserPageList, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 4:32 PM
 **/
@Data
@DateTimeRangeConstraints
@EqualsAndHashCode(callSuper = true)
public class GetUserPageListPayload extends PaginationBase {
    private String username;
    @DateTimeRangeGroup(type = START_TIME)
    private LocalDateTime startTime;
    @DateTimeRangeGroup(type = END_TIME)
    private LocalDateTime endTime;
}
