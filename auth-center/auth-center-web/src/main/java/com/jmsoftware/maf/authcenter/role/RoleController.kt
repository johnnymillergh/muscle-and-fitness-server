package com.jmsoftware.maf.authcenter.role

import com.jmsoftware.maf.authcenter.role.service.RoleDomainService
import com.jmsoftware.maf.common.util.logger
import com.jmsoftware.maf.springcloudstarter.poi.AbstractExcelDataController
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

/**
 * Description: RoleController, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/17/2020 4:44 PM
 */
@RestController
@RequestMapping("/roles")
class RoleController(
    private val roleDomainService: RoleDomainService
) : AbstractExcelDataController<RoleExcelBean>() {
    companion object {
        private val log = logger()
    }

    override fun onExceptionOccurred() {
        log.error("Exception occurred when uploading excel for role.")
        fileName.set("role-stat-${Instant.now()}.xlsx")
    }

    override fun executeDatabaseOperation(beanList: List<RoleExcelBean>) {
        log.info("ExecuteDatabaseOperation: {}", beanList)
        roleDomainService.save(beanList)
    }

    override fun validateBeforeAddToBeanList(
        beanList: List<RoleExcelBean>,
        bean: RoleExcelBean,
        index: Int
    ) {
        roleDomainService.validateBeforeAddToBeanList(beanList, bean, index)
    }

    override fun beforeDatabaseOperation(beanList: List<RoleExcelBean>) {
        log.info("BeforeDatabaseOperation: {}", beanList)
    }

    override fun getTemplateFileName(): String {
        return RoleDomainService.ROLE_TEMPLATE_EXCEL
    }

    override fun getListForExporting(): List<RoleExcelBean> {
        return roleDomainService.getListForExporting()
    }
}
