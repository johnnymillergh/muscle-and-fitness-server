package com.jmsoftware.maf.common.bean;

import lombok.Data;

import java.util.List;

/**
 * <h1>ExcelImportResult</h1>
 * <p>
 * Change description here.
 *
 * @author 钟俊 (jun.zhong), email: jun.zhong@ucarinc.com
 * @date 4/29/20 8:50 PM
 **/
@Data
public class ExcelImportResult {
    private List<String> messageList;
    private String excelFilePath;
}
