package com.jmsoftware.maf.authcenter.role.controller;

import com.jmsoftware.maf.authcenter.role.entity.RoleExcelImport;
import com.jmsoftware.maf.authcenter.role.service.RoleService;
import com.jmsoftware.maf.springcloudstarter.controller.AbstractExcelImportController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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
public class RoleController extends AbstractExcelImportController<RoleExcelImport> {
    private final RoleService roleService;

    @GetMapping("/stat/excel")
    public ResponseEntity<StreamingResponseBody> downloadRoleStat() {
        return this.roleService.downloadRoleStat();
    }

    @Override
    public void onExceptionOccurred() {
        log.error("Exception occurred when uploading excel for role.");
        this.fileName.set("role-stat" + Instant.now() + ".xlsx");
    }

    @Override
    protected void beforeDatabaseOperation(List<RoleExcelImport> beanList) {
        log.info("BeforeDatabaseOperation: {}", beanList);
    }

    @Override
    protected void executeDatabaseOperation(List<RoleExcelImport> beanList) throws Exception {
        log.info("ExecuteDatabaseOperation: {}", beanList);
        this.roleService.save(beanList);
    }

    @Override
    protected boolean validateBeforeAddToBeanList(List<RoleExcelImport> beanList, RoleExcelImport bean, int index) throws IllegalArgumentException {
        return this.roleService.validateBeforeAddToBeanList(beanList, bean, index);
    }
}
