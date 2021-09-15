package com.jmsoftware.maf.authcenter.role.controller;

import com.jmsoftware.maf.authcenter.role.entity.RoleExcelBean;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
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
    private final RoleService roleService;

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
    protected void executeDatabaseOperation(List<RoleExcelBean> beanList) throws Exception {
        log.info("ExecuteDatabaseOperation: {}", beanList);
        this.roleService.save(beanList);
    }

    @Override
    protected String getTemplateFileName() {
        return RoleService.ROLE_TEMPLATE_EXCEL;
    }

    @Override
    protected List<RoleExcelBean> getListForExporting() {
        return this.roleService.getListForExporting();
    }

    @Override
    protected void validateBeforeAddToBeanList(List<RoleExcelBean> beanList, RoleExcelBean bean, int index) throws IllegalArgumentException {
        this.roleService.validateBeforeAddToBeanList(beanList, bean, index);
    }
}
