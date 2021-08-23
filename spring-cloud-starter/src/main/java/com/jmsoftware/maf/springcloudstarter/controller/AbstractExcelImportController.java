package com.jmsoftware.maf.springcloudstarter.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.WorkbookUtil;
import com.google.common.collect.Lists;
import com.jmsoftware.maf.common.bean.ExcelImportResult;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.annotation.ExcelColumn;
import com.jmsoftware.maf.springcloudstarter.configuration.ExcelImportConfiguration;
import com.jmsoftware.maf.springcloudstarter.minio.MinioHelper;
import io.swagger.annotations.ApiOperation;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * <h1>AbstractExcelImportController</h1>
 * <p>Abstract controller for excel data import.</p>
 * <h2>Features</h2>
 * <ol>
 * <li>Row validation. If not valid, will generate error message at the last cell.</li>
 * <li>Generalization support. <code>AbstractExcelImportController&lt;ExcelImportBeanType&gt;</code></li>
 * <li>Using annotation <code>@ExcelColumn</code> for fields can be generating Excel easier than ever.</li>
 * <li>Support partial data import.</li>
 *
 * </ol>
 * <h2>Understanding of the Lifecycle</h2>
 * <p>Understanding of the lifecycle will help us make less mistakes during programming.</p>
 * <h3>I. initContext()</h3>
 * <p>Initialize context. Generate the field name array of ExcelImportBeanType by reflection.</p>
 * <h3>II. registerBindHandlerMethods()</h3>
 * <p>Register bind handler methods (starts with ‘“check”, like <code>checkString()</code>).</p>
 * <h3>III. registerHandlerMethods()</h3>
 * <p>Register default handler methods (starts with ‘“check”, like <code>checkString()</code>).</p>
 * <h3>IV. beforeExecute()</h3>
 * <p>Left black. Can be overridden by sub class.</p>
 * <h3>V. initLocaleContext()</h3>
 * <p>Initialize locale context for current thread. Main work below:</p>
 * <ul>
 * <li>errorMessageList</li>
 * <li>beanList</li>
 * <li>returnMessageList</li>
 * <li>rowLocation</li>
 * <li>columnLocation</li>
 * <li>sheetLocation</li>
 *
 * </ul>
 * <p>After initializing, will upload Excel, read and bind data, validate data.</p>
 * <h3>VI. beforeDatabaseOperation()</h3>
 * <p>Left black. Can be overridden by sub class.</p>
 * <h3>VII. executeDatabaseOperation()</h3>
 * <p>Must be implemented by sub class.</p>
 * <h3>VIII. onExceptionOccurred()</h3>
 * <p>If any exceptions occur, this method will be called. Must be implemented by sub class.</p>
 * <h3>Ⅸ. afterExecute()</h3>
 * <p>Delete temporary file.</p>
 * <h3>X. destroyLocaleContext()</h3>
 * <p>Destroy locale context, in opposite of <code>initLocaleContext()</code>.</p>
 *
 * @param <T> Excel import bean type
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/19/2021 10:06 AM
 */
@Slf4j
@SuppressWarnings({"unused"})
public abstract class AbstractExcelImportController<T> {
    protected final ThreadLocal<List<T>> beanList = ThreadLocal.withInitial(() -> null);
    protected final ThreadLocal<Workbook> workbook = ThreadLocal.withInitial(() -> null);
    protected final ThreadLocal<String> excelFilePath = ThreadLocal.withInitial(() -> null);
    protected final ThreadLocal<Boolean> exceptionOccurred = ThreadLocal.withInitial(() -> false);
    protected final ThreadLocal<List<String>> errorMessageList = ThreadLocal.withInitial(Lists::newLinkedList);
    protected final ThreadLocal<List<String>> returnMessageList = ThreadLocal.withInitial(Lists::newLinkedList);
    protected final ThreadLocal<String> fileName = ThreadLocal.withInitial(() -> null);

    @Resource
    protected ExcelImportConfiguration excelImportConfiguration;
    @Resource
    protected MinioHelper minioHelper;
    /**
     * Deny all data when data validation fails. Default value is true.
     */
    private boolean denyAll = true;
    /**
     * The class of ExcelImportBeanType
     */
    private Class<T> bindClass;
    /**
     * The field names arrays of the ExcelImportBeanType
     */
    private String[] fieldNameArray;

    /**
     * <p>Constructor of AbstractExcelImportController</p>
     * <ol>
     * <li><p>Init context</p>
     * </li>
     * <li><p>Register bind handler methods</p>
     * </li>
     * <li><p>Register handler methods</p>
     * </li>
     * </ol>
     */
    protected AbstractExcelImportController() {
        this.initContext();
    }

    /**
     * <p>Init context.</p>
     * <ol>
     * <li><p>Call <code>getGenericClass()</code> to set Excel import type；</p>
     * </li>
     * <li><p>Set fieldNameArray</p>
     * </li>
     * </ol>
     */
    protected void initContext() {
        this.bindClass = this.getGenericClass();
        val declaredFields = this.bindClass.getDeclaredFields();
        val fieldNames = new String[declaredFields.length];
        for (var index = 0; index < declaredFields.length; index++) {
            val declaredField = declaredFields[index];
            fieldNames[index] = declaredField.getName();
        }
        log.info("Generated {} field name array by reflection, fieldNames: {}", this.bindClass.getSimpleName(),
                 fieldNames);
        this.fieldNameArray = fieldNames;
    }

    /**
     * Init locale context.
     */
    private void initLocaleContext() {
        this.beanList.set(Lists.newLinkedList());
        this.errorMessageList.set(Lists.newLinkedList());
        this.returnMessageList.set(Lists.newLinkedList());
        this.exceptionOccurred.set(false);
    }

    /**
     * Destroy locale context.
     */
    private void destroyLocaleContext() {
        this.beanList.remove();
        this.closeWorkbook(this.workbook.get());
        this.workbook.remove();
        this.excelFilePath.remove();
        this.exceptionOccurred.remove();
        this.errorMessageList.remove();
        this.returnMessageList.remove();
        this.fileName.remove();
    }

    /**
     * Before execute.
     */
    protected void beforeExecute() {
    }

    /**
     * On exception occurred.
     */
    protected abstract void onExceptionOccurred();

    /**
     * Validate before adding to bean list boolean.
     *
     * @param beanList the bean list that contains validated bean
     * @param bean     the bean that needs to be validated
     * @param index    the index that is the reference to the row number of the Excel file
     * @throws IllegalArgumentException the illegal argument exception
     */
    protected abstract void validateBeforeAddToBeanList(List<T> beanList, T bean, int index) throws IllegalArgumentException;

    /**
     * Before database operation.
     *
     * @param beanList the bean list
     */
    protected void beforeDatabaseOperation(List<T> beanList) {
    }

    /**
     * Execute database operation.
     *
     * @param beanList the bean list that can be used for DB operations
     * @throws Exception the exception
     */
    protected abstract void executeDatabaseOperation(List<T> beanList) throws Exception;

    /**
     * After execute. Delete file.
     */
    protected void afterExecute() {
    }

    /**
     * Upload excel file. Any exceptions happened in any lifecycle will not interrupt the whole process.
     *
     * @param multipartFile the multipart file
     * @return the response body bean
     */
    @PostMapping(value = "/upload")
    @ApiOperation(value = "Upload Excel file", notes = "Upload and import excel data")
    public ResponseBodyBean<ExcelImportResult> upload(@RequestParam("file") MultipartFile multipartFile) {
        this.beforeExecute();
        this.initLocaleContext();
        try {
            this.setReturnMessageList("Starting - Read Excel file…");
            this.workbook.set(this.readFile(multipartFile));
            this.setReturnMessageList("Finished - Read Excel file");
        } catch (IOException e) {
            log.error("Exception occurred when reading Excel file!", e);
            this.setErrorMessage(
                    String.format("Exception occurred when reading Excel file! Exception message: %s", e.getMessage()));
        }
        try {
            this.setReturnMessageList("Starting - Validate and bind data…");
            this.bindData(this.workbook.get());
            this.setReturnMessageList("Finished - Validate and bind data");
        } catch (Exception e) {
            log.error("Exception occurred when validating and binding data!", e);
            this.setErrorMessage(
                    String.format("Exception occurred when validating and binding data! Exception message: %s",
                                  e.getMessage()));
        }
        this.beforeDatabaseOperation(this.beanList.get());
        // if no error message (errorMessageList if empty) or not denyAll, then execute DB operation
        if (CollectionUtil.isEmpty(this.errorMessageList.get()) || !this.denyAll) {
            if (CollectionUtil.isNotEmpty(this.beanList.get())) {
                this.setReturnMessageList("Starting - Import data…");
                try {
                    this.executeDatabaseOperation(this.beanList.get());
                    this.setReturnMessageList(
                            String.format("Finished - Import data. Imported count: %d", this.beanList.get().size()));
                } catch (Exception e) {
                    this.exceptionOccurred.set(true);
                    log.error("Exception occurred when executing DB operation!", e);
                    this.setErrorMessage(
                            String.format("Exception occurred when executing DB operation! Exception message: %s",
                                          e.getMessage()));
                }
            } else {
                this.setReturnMessageList(
                        String.format("Finished - Import data. Empty list. Imported count: %d",
                                      this.beanList.get().size()));
            }
        } else {
            this.setReturnMessageList("[Warning] Found not valid data. Data import all failed!");
        }
        if (Boolean.TRUE.equals(this.exceptionOccurred.get())) {
            this.onExceptionOccurred();
            this.uploadWorkbook();
        }
        val excelImportResult = new ExcelImportResult();
        excelImportResult.setMessageList(this.returnMessageList.get());
        excelImportResult.setExcelFilePath(this.excelFilePath.get());
        this.afterExecute();
        this.destroyLocaleContext();
        return ResponseBodyBean.ofSuccess(excelImportResult);
    }

    /**
     * Gets generic class.
     *
     * @return the generic class
     */
    private Class<T> getGenericClass() {
        val type = this.getClass().getGenericSuperclass();
        log.info("Got type by reflection, typeName: {}", type.getTypeName());
        if (type instanceof ParameterizedType) {
            val parameterizedType = (ParameterizedType) type;
            val typeName = parameterizedType.getActualTypeArguments()[0].getTypeName();
            Class<T> aClass;
            try {
                //noinspection unchecked
                aClass = (Class<T>) Class.forName(typeName);
            } catch (ClassNotFoundException e) {
                log.error("Exception occurred when looking for class!", e);
                throw new IllegalArgumentException(e.getMessage());
            }
            return aClass;
        }
        throw new IllegalArgumentException("Cannot find the type from the generic class!");
    }

    /**
     * Sets field name array.
     *
     * @param fieldNameArray the field name array
     */
    public void setFieldNameArray(String[] fieldNameArray) {
        this.fieldNameArray = fieldNameArray;
    }

    /**
     * Read the file into workbook.
     *
     * @param multipartFile the multipart file
     * @return the workbook
     * @throws IOException the io exception
     */
    private Workbook readFile(@NonNull MultipartFile multipartFile) throws IOException {
        return WorkbookUtil.createBook(multipartFile.getInputStream());
    }

    @SneakyThrows
    private void uploadWorkbook() {
        @Cleanup val outputStream = new ByteArrayOutputStream();
        this.workbook.get().write(outputStream);
        final var bufferedInputStream = new BufferedInputStream(new ByteArrayInputStream(outputStream.toByteArray()));
        this.minioHelper.makeBucket("temp");
        this.minioHelper.putObject("temp", this.fileName.get(), bufferedInputStream,
                                   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        this.excelFilePath.set("temp/temp.xlsx");
    }

    /**
     * <p>Bind data from the workbook. Processes below: </p>
     * <ul>
     * <li><p>Bind data to the instance of bindClass, according to the fieldNameArray.</p>
     * </li>
     * <li><p>Validate the field by callback method</p>
     * </li>
     * </ul>
     *
     * @param workbook the workbook
     * @throws Exception the exception
     */
    @SuppressWarnings("RedundantThrows")
    private void bindData(Workbook workbook) throws Exception {
        for (int index = 0; index < workbook.getNumberOfSheets(); index++) {
            val sheet = workbook.getSheetAt(index);
            val excelReader = new ExcelReader(workbook, index);
            Field[] declaredFields = this.bindClass.getDeclaredFields();
            for (Field declaredField : declaredFields) {
                ExcelColumn annotation = declaredField.getAnnotation(ExcelColumn.class);
                excelReader.addHeaderAlias(annotation.name(), declaredField.getName());
            }
            this.beanList.set(excelReader.readAll(this.bindClass));
            for (var beanIndex = 0; beanIndex < this.beanList.get().size(); beanIndex++) {
                this.validateBeanByRow(index, beanIndex);
            }
        }
    }

    private void validateBeanByRow(int sheetIndex, int beanIndex) {
        try {
            this.validateBeforeAddToBeanList(this.beanList.get(), this.beanList.get().get(beanIndex), beanIndex);
        } catch (Exception e) {
            log.error("bindRowToBean method has encountered a problem!", e);
            this.exceptionOccurred.set(true);
            val errorMessage = String.format(
                    "Exception occurred when binding and validating the data of row number %d. " +
                            "Exception message: %s", beanIndex + 1, e.getMessage());
            this.setErrorMessage(errorMessage);
            val errorRow = this.workbook.get().getSheetAt(sheetIndex).getRow(beanIndex + 1);
            val errorInformationCell = errorRow.createCell(this.fieldNameArray.length);
            errorInformationCell.setCellValue(errorMessage);
        }
    }

    /**
     * Sets deny all. When exception occurred, deny all data or not
     *
     * @param denyAll the deny all
     */
    public void setDenyAll(Boolean denyAll) {
        this.denyAll = denyAll;
    }

    /**
     * Sets error message.
     *
     * @param errorInfo the error info
     */
    protected void setErrorMessage(String errorInfo) {
        this.errorMessageList.get().add(errorInfo);
        this.setReturnMessageList(errorInfo);
    }

    /**
     * Sets return message list.
     *
     * @param message the message
     */
    protected void setReturnMessageList(String message) {
        this.returnMessageList.get().add(message);
    }

    /**
     * Close workbook.
     *
     * @param workbook the workbook
     */
    private void closeWorkbook(Workbook workbook) {
        if (workbook != null) {
            try {
                workbook.close();
            } catch (IOException e) {
                log.error("Exception occurred when closing workbook! Exception message: {}, workbook: {}",
                          e.getMessage(), workbook);
            }
        }
    }
}
