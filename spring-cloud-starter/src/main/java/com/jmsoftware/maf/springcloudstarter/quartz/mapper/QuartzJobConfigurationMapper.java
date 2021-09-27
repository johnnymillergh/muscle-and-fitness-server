package com.jmsoftware.maf.springcloudstarter.quartz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel;
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Description: QuartzJobConfigurationMapper
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/23/2021 8:23 AM
 */
@Mapper
public interface QuartzJobConfigurationMapper extends BaseMapper<QuartzJobConfiguration> {
    /**
     * Select page list page.
     *
     * @param page    the page
     * @param payload the payload
     * @return the page
     */
    Page<GetQuartzJobConfigurationPageListItem> selectPageList(
            Page<GetQuartzJobConfigurationPageListItem> page,
            @Param("payload") GetQuartzJobConfigurationPageListPayload payload
    );

    /**
     * Select list for exporting list.
     *
     * @param serviceName the service name
     * @return the list
     */
    List<QuartzJobConfigurationExcel> selectListForExporting(String serviceName);
}
