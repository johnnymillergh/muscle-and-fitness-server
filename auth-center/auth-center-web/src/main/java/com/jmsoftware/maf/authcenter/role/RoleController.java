package com.jmsoftware.maf.authcenter.role;

import com.jmsoftware.maf.authcenter.role.service.RoleDomainService;
import com.jmsoftware.maf.springcloudstarter.poi.AbstractExcelDataController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * Description: RoleController, change description here.
 *
 * @author 钟俊（zhongjun）, email: zhongjun@toguide.cn, date: 12/17/2020 4:44 PM
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
public class RoleController extends AbstractExcelDataController<RoleExcelBean> {
    private final RoleDomainService roleDomainService;

    @Override
    public void onExceptionOccurred() {
        log.error("Exception occurred when uploading excel for role.");
        this.fileName.set("role-stat" + Instant.now() + ".xlsx");
    }

    @Override
    protected void beforeDatabaseOperation(List<RoleExcelBean> beanList) {
        log.info("BeforeDatabaseOperation: {}", beanList);
    }

    @Override
    protected void executeDatabaseOperation(List<RoleExcelBean> beanList) {
        log.info("ExecuteDatabaseOperation: {}", beanList);
        this.roleDomainService.save(beanList);
    }

    @Override
    protected String getTemplateFileName() {
        return RoleDomainService.ROLE_TEMPLATE_EXCEL;
    }

    @Override
    protected List<RoleExcelBean> getListForExporting() {
        return this.roleDomainService.getListForExporting();
    }

    @Override
    protected void validateBeforeAddToBeanList(List<RoleExcelBean> beanList, RoleExcelBean bean, int index) throws IllegalArgumentException {
        this.roleDomainService.validateBeforeAddToBeanList(beanList, bean, index);
    }
}
