package com.jmsoftware.maf.springcloudstarter.quartz.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ReflectUtil
import com.baomidou.mybatisplus.extension.plugins.pagination.Page
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.jmsoftware.maf.common.bean.PageResponseBodyBean
import com.jmsoftware.maf.common.function.requireTrue
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.property.MafProjectProperties
import com.jmsoftware.maf.springcloudstarter.quartz.constant.QUARTZ_JOB_CONFIGURATION
import com.jmsoftware.maf.springcloudstarter.quartz.converter.QuartzJobConfigurationMapStructMapper
import com.jmsoftware.maf.springcloudstarter.quartz.entity.CreateOrModifyQuartzJobConfigurationPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListItem
import com.jmsoftware.maf.springcloudstarter.quartz.entity.GetQuartzJobConfigurationPageListPayload
import com.jmsoftware.maf.springcloudstarter.quartz.entity.QuartzJobConfigurationExcel
import com.jmsoftware.maf.springcloudstarter.quartz.entity.persistence.QuartzJobConfiguration
import com.jmsoftware.maf.springcloudstarter.quartz.repository.QuartzJobConfigurationMapper
import com.jmsoftware.maf.springcloudstarter.quartz.service.QuartzJobConfigurationService
import com.jmsoftware.maf.springcloudstarter.quartz.util.createScheduleJob
import com.jmsoftware.maf.springcloudstarter.quartz.util.getJobKey
import com.jmsoftware.maf.springcloudstarter.quartz.util.validateCronExp
import jakarta.annotation.PostConstruct
import jakarta.validation.Validator
import org.quartz.JobDataMap
import org.quartz.Scheduler
import org.springframework.scheduling.quartz.SchedulerFactoryBean
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

/**
 * # QuartzJobConfigurationServiceImpl
 *
 * Description: QuartzJobConfigurationServiceImpl
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/15/22 9:11 AM
 */
@Service
class QuartzJobConfigurationServiceImpl(
    private val schedulerFactoryBean: SchedulerFactoryBean,
    private val mafProjectProperties: MafProjectProperties,
    private val validator: Validator
) : ServiceImpl<QuartzJobConfigurationMapper, QuartzJobConfiguration>(), QuartzJobConfigurationService {
    companion object {
        private val logger = logger()
    }

    @PostConstruct
    fun initQuartzJob() {
        val scheduler: Scheduler = schedulerFactoryBean.scheduler
        scheduler.clear()
        val jobList: List<QuartzJobConfiguration> = getQuartzJobConfigurationForInitialization()
        for (quartzJobConfiguration in jobList) {
            createScheduleJob(
                scheduler,
                quartzJobConfiguration,
                mafProjectProperties.projectArtifactId
            ).let {
                logger.info("Created schedule job. JobKey: ${it?.key}")
            }
        }
    }

    private fun getQuartzJobConfigurationForInitialization(): List<QuartzJobConfiguration> {
        return this.ktQuery()
            .eq(QuartzJobConfiguration::serviceName, mafProjectProperties.projectArtifactId)
            .orderByAsc(QuartzJobConfiguration::createdTime)
            .list()
    }

    override fun getPageList(
        payload: GetQuartzJobConfigurationPageListPayload
    ): PageResponseBodyBean<GetQuartzJobConfigurationPageListItem> {
        val page: Page<GetQuartzJobConfigurationPageListItem> =
            Page(payload.currentPage.toLong(), payload.pageSize.toLong())
        this.getBaseMapper().selectPageList(page, payload)
        return PageResponseBodyBean.ofSuccess(page.records, page.total)
    }

    override fun validateBeforeAddToBeanList(
        beanList: List<QuartzJobConfigurationExcel>,
        bean: QuartzJobConfigurationExcel,
        index: Int
    ) {
        val constraintViolations = validator.validate(bean)
        requireTrue(CollUtil.isEmpty(constraintViolations)) { valid: Boolean ->
            logger.info("QuartzJobConfigurationExcel validation result: $valid")
        }.orElseThrow { IllegalStateException("Invalid data. ${constraintViolations.first().message}") }
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun save(beanList: List<QuartzJobConfigurationExcel>) {
        requireTrue(
            this.saveBatch(
                beanList.stream()
                    .map(QuartzJobConfigurationMapStructMapper.INSTANCE::of)
                    .toList()
            )
        ) { saved: Boolean ->
            logger.info("Saved quartzJobConfigurationList, saved: $saved")
        }.orElseThrow { IllegalStateException("Failed to save batch quartzJobConfigurationList") }
    }

    override fun getListForExporting(): List<QuartzJobConfigurationExcel> =
        this.getBaseMapper().selectListForExporting(mafProjectProperties.projectArtifactId)

    override fun create(
        payload: CreateOrModifyQuartzJobConfigurationPayload
    ): Long {
        validateCronExpression(payload.cronExpression)
        val quartzJobConfiguration: QuartzJobConfiguration = payload.asQuartzJobConfiguration()
        quartzJobConfiguration.serviceName = mafProjectProperties.projectArtifactId
        requireTrue(save(quartzJobConfiguration)) { saved: Boolean ->
            logger.info("Quartz job configuration saved: $saved")
        }.orElseThrow { IllegalStateException("Failed to save quartz job configuration") }
        return quartzJobConfiguration.id!!
    }

    private fun validateCronExpression(cronExpression: String) {
        requireTrue(validateCronExp(cronExpression)) { valid: Boolean ->
            logger.warn("Cron validation: $valid, expression: $cronExpression")
        }.orElseThrow { IllegalArgumentException("Cron($cronExpression) invalid") }
    }

    override fun modify(
        id: Long,
        payload: CreateOrModifyQuartzJobConfigurationPayload
    ): Long {
        validateCronExpression(payload.cronExpression)
        val quartzJobConfiguration: QuartzJobConfiguration = payload.asQuartzJobConfiguration()
        quartzJobConfiguration.id = id
        requireTrue(this.updateById(quartzJobConfiguration)) { updated: Boolean ->
            logger.warn("Quartz job configuration updated: $updated")
        }.orElseThrow { IllegalStateException("Failed to update quartz job configuration") }
        return id
    }

    override fun patch(
        id: Long,
        property: String,
        payload: CreateOrModifyQuartzJobConfigurationPayload
    ): Long {
        val value = ReflectUtil.getFieldValue(payload, property)
        Objects.requireNonNull(value, "Property's value($property) must not be null")
        val constraintViolations = validator.validateProperty(payload, property)
        requireTrue(CollUtil.isEmpty(constraintViolations)) { valid: Boolean ->
            logger.warn("Quartz job configuration patched: $valid")
        }.orElseThrow { IllegalStateException("`$property` invalid! Reason: ${constraintViolations.first().message}") }
        val quartzJobConfiguration = QuartzJobConfiguration()
        ReflectUtil.setFieldValue(quartzJobConfiguration, property, value)
        quartzJobConfiguration.id = id
        requireTrue(this.updateById(quartzJobConfiguration)) { updated: Boolean ->
            logger.warn("Quartz job configuration patched: $updated")
        }.orElseThrow { IllegalStateException("Failed to patch $property") }
        return id
    }

    override fun runImmediately(id: Long): Long {
        val quartzJobConfiguration: QuartzJobConfiguration =
            this.getById(id) ?: throw IllegalArgumentException("Quartz job(id:$id) not found!")
        val scheduler: Scheduler = schedulerFactoryBean.scheduler
        val jobDataMap = JobDataMap()
        jobDataMap[QUARTZ_JOB_CONFIGURATION] = quartzJobConfiguration
        scheduler.triggerJob(
            getJobKey(
                quartzJobConfiguration.id!!,
                quartzJobConfiguration.group!!,
                mafProjectProperties.projectArtifactId
            ),
            jobDataMap
        )
        logger.warn("Triggered Quartz job successfully, $quartzJobConfiguration")
        return id
    }

    @Transactional(rollbackFor = [Throwable::class])
    override fun delete(id: Long, group: String): Long {
        requireTrue(this.removeById(id)) { deleted: Boolean ->
            logger.warn("Quartz job configuration deleted: $deleted")
        }.orElseThrow { IllegalStateException("Failed to delete Quartz job configuration. id: $id, group: $group") }
        val scheduler: Scheduler = schedulerFactoryBean.scheduler
        val deletedJob = scheduler.deleteJob(
            getJobKey(id, group, mafProjectProperties.projectArtifactId)
        )
        requireTrue(deletedJob) { deletedJob1: Boolean ->
            logger.warn("Scheduler deleted job and related triggers: $deletedJob1")
        }.orElseThrow { IllegalStateException("Failed to delete job by scheduler") }
        return id
    }
}
