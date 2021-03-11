package com.jmsoftware.maf.springcloudstarter.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.bean.ExcelImportResult;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.configuration.ExcelImportConfiguration;
import com.jmsoftware.maf.springcloudstarter.sftp.SftpHelper;
import com.jmsoftware.maf.springcloudstarter.sftp.SftpUploadFile;
import com.jmsoftware.maf.springcloudstarter.util.PoiUtil;
import io.swagger.annotations.ApiOperation;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
 * <li>readingRowCount</li>
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
 * @param <ExcelImportBeanType> Excel import bean type
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 2/19/2021 10:06 AM
 */
@Slf4j
@SuppressWarnings({"unused"})
public abstract class AbstractExcelImportController<ExcelImportBeanType> {
    /**
     * Temporary file path
     */
    private static final String TEMP_FILE_PATH =
            new ApplicationHome(AbstractExcelImportController.class).getSource()
                    .getParent() + "/temp-file/";
    /**
     * SFTP upload directory
     */
    private static final String SFTP_DIR = "excels/";
    /**
     * File key in MultipartFile
     * TODO: check if it's necessary
     */
    private static final String FILE_KEY = "upload_file";
    /**
     * Default check method prefix
     */
    private static final String DEFAULT_CHECK_METHOD_PREFIX = "check";
    /**
     * The constant XLS.
     */
    private static final String XLS = "xls";
    /**
     * The constant XLSX.
     */
    private static final String XLSX = "xlsx";
    /**
     * The User defined message.
     */
    protected ThreadLocal<String> userDefinedMessage = new ThreadLocal<>();
    /**
     * Deny all data when data validation fails. Default value is true.
     */
    private Boolean denyAll = Boolean.TRUE;
    /**
     * The class of ExcelImportBeanType
     */
    private Class<ExcelImportBeanType> bindClass;
    /**
     * Data type and bind method
     */
    private final Map<Class<?>, Method> bindMethodMap = new HashMap<>();
    /**
     * The field names arrays of the ExcelImportBeanType
     */
    private String[] fieldNameArray;
    /**
     * File name
     */
    @Getter
    @Setter
    private String fileName;
    /**
     * Custom validation method
     * <p>
     * Start with `check`, parameter is List list, return value is String or Boolean.
     * <p>
     * e.g public String checkName(List list)
     */
    private final Map<String, Method> checkMethodMap = new HashMap<>();
    /**
     * Sheet location when binging data.
     */
    private final ThreadLocal<Integer> sheetLocation = new ThreadLocal<>();
    /**
     * Row location
     */
    private final ThreadLocal<Integer> rowLocation = new ThreadLocal<>();
    /**
     * Column location
     */
    private final ThreadLocal<Integer> columnLocation = new ThreadLocal<>();
    /**
     * Reading row count
     */
    private final ThreadLocal<Integer> readingRowCount = new ThreadLocal<>();
    /**
     * Error message list
     */
    protected ThreadLocal<List<String>> errorMessageList = new ThreadLocal<>();
    /**
     * Bean list. After reading Excel
     */
    protected final ThreadLocal<List<ExcelImportBeanType>> beanList = new ThreadLocal<>();
    /**
     * Return message list
     */
    private final ThreadLocal<List<String>> returnMessageList = new ThreadLocal<>();
    /**
     * The Workbook.
     */
    protected final ThreadLocal<Workbook> workbook = new ThreadLocal<>();
    /**
     * The Workbook with error message.
     */
    protected final ThreadLocal<Workbook> workbookWithErrorMessage = new ThreadLocal<>();
    /**
     * The Excel file path.
     */
    protected final ThreadLocal<String> excelFilePath = new ThreadLocal<>();
    /**
     * The Exception occurred.
     */
    protected final ThreadLocal<Boolean> exceptionOccurred = new ThreadLocal<>();
    /**
     * The File.
     */
    protected final ThreadLocal<File> file = new ThreadLocal<>();

    @Resource
    protected SftpHelper sftpHelper;
    @Resource
    protected ExcelImportConfiguration excelImportConfiguration;

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
    public AbstractExcelImportController() {
        initContext();
        registerBindHandlerMethods();
        registerHandlerMethods();
    }

    /**
     * Init locale context.
     */
    private void initLocaleContext() {
        errorMessageList.set(new LinkedList<>());
        beanList.set(new LinkedList<>());
        returnMessageList.set(new LinkedList<>());
        rowLocation.set(0);
        columnLocation.set(0);
        sheetLocation.set(0);
        readingRowCount.set(0);
        userDefinedMessage.set("");
        exceptionOccurred.set(false);
    }

    /**
     * On exception occurred.
     */
    protected abstract void onExceptionOccurred();

    /**
     * Destroy locale context.
     */
    private void destroyLocaleContext() {
        errorMessageList.remove();
        beanList.remove();
        returnMessageList.remove();
        rowLocation.remove();
        columnLocation.remove();
        sheetLocation.remove();
        readingRowCount.remove();
        userDefinedMessage.remove();
        closeWorkbook(workbook.get());
        workbook.remove();
        closeWorkbook(workbookWithErrorMessage.get());
        workbookWithErrorMessage.remove();
        excelFilePath.remove();
        exceptionOccurred.remove();
        file.remove();
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

    /**
     * Upload excel file. Any exceptions happened in any lifecycle will not interrupt the whole process.
     *
     * @param request the request
     * @return the response body bean
     */
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "Upload Excel file", notes = "Upload Excel file")
    public ResponseBodyBean<ExcelImportResult> upload(HttpServletRequest request) {
        beforeExecute();
        initLocaleContext();
        try {
            setReturnMessageList("Starting - Upload Excel file…");
            file.set(uploadFile((MultipartHttpServletRequest) request));
            setReturnMessageList("Finished - Upload Excel file");
        } catch (IOException e) {
            log.error("Exception occurred when uploading file!", e);
            setErrorMessage(
                    String.format("Exception occurred when uploading file! Exception message: %s", e.getMessage()));
        }
        try {
            setReturnMessageList("Starting - Read Excel file…");
            workbook.set(readFile(file.get()));
            setReturnMessageList("Finished - Read Excel file");
        } catch (IOException e) {
            log.error("Exception occurred when reading Excel file!", e);
            setErrorMessage(
                    String.format("Exception occurred when reading Excel file! Exception message: %s", e.getMessage()));
        }
        try {
            setReturnMessageList("Starting - Validate and bind data…");
            bindData(workbook.get());
            setReturnMessageList("Finished - Validate and bind data");
        } catch (Exception e) {
            log.error("Exception occurred when validating and binding data!", e);
            setErrorMessage(String.format("Exception occurred when validating and binding data! Exception message: %s",
                                          e.getMessage()));
        }
        beforeDatabaseOperation(this.beanList.get());
        // if no error message (errorMessageList if empty) or not denyAll, then execute DB operation
        if (CollectionUtil.isEmpty(this.errorMessageList.get()) || !denyAll) {
            if (CollectionUtil.isNotEmpty(this.beanList.get())) {
                setReturnMessageList("Starting - Import data…");
                try {
                    executeDatabaseOperation(this.beanList.get());
                    setReturnMessageList(
                            String.format("Finished - Import data. Imported count: %d", beanList.get().size()));
                } catch (Exception e) {
                    exceptionOccurred.set(true);
                    log.error("Exception occurred when executing DB operation!", e);
                    setErrorMessage(
                            String.format("Exception occurred when executing DB operation! Exception message: %s",
                                          e.getMessage()));
                }
            } else {
                setReturnMessageList(
                        String.format("Finished - Import data. Empty list. Imported count: %d", beanList.get().size()));
            }
        } else {
            setReturnMessageList("[Warning] Found not valid data. Data import all failed!");
        }
        if (exceptionOccurred.get()) {
            onExceptionOccurred();
        }
        ExcelImportResult excelImportResult = new ExcelImportResult();
        excelImportResult.setMessageList(this.returnMessageList.get());
        excelImportResult.setExcelFilePath(this.excelFilePath.get());
        afterExecute();
        destroyLocaleContext();
        return ResponseBodyBean.ofSuccess(excelImportResult);
    }

    /**
     * Before execute.
     */
    protected void beforeExecute() {
    }

    /**
     * After execute. Delete file.
     */
    protected void afterExecute() {
        Optional<File> optionalFile = Optional.ofNullable(file.get());
        optionalFile.ifPresent(file1 -> {
            boolean deleted = file1.delete();
            if (deleted) {
                log.warn("Deleted file. File absolute path: {}", file1.getAbsolutePath());
            } else {
                log.warn("The file cannot be deleted！File absolute path: {}", file1.getAbsolutePath());
            }
        });
        if (optionalFile.isEmpty()) {
            log.warn("The file does not exist!");
        }
    }

    /**
     * Before database operation.
     *
     * @param beanList the bean list
     */
    protected void beforeDatabaseOperation(List<ExcelImportBeanType> beanList) {
    }

    /**
     * <p>Register handler methods</p>
     * <ul>
     * <li>e.g: <code>private Boolean checkXXX(String value,Integer index)</code></li>
     * <li>e.g: <code>private String checkXXX(String value,Integer index)</code>, the return value can be empty or
     * “true”, or other error message which will be added to  errorMessage</li>
     * </ul>
     */
    private void registerHandlerMethods() {
        this.checkMethodMap.clear();
        // Register valid methods for validation
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (isCheckMethod(method)) {
                registerCheckMethod(method);
            }
        }
    }

    /**
     * <p>Check if method is valid for validation.</p>
     * <ol>
     * <li><p>Method name starts with `check`</p>
     * </li>
     * <li><p>Parameter is String value</p>
     * </li>
     * <li><p>Return value is Boolean</p>
     * </li>
     * </ol>
     *
     * @param method the method
     * @return the boolean
     */
    private Boolean isCheckMethod(Method method) {
        val returnType = method.getReturnType();
        if (method.getName().startsWith(DEFAULT_CHECK_METHOD_PREFIX)) {
            if (String.class.equals(returnType) || Boolean.class.equals(returnType) || boolean.class.equals(
                    returnType)) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                return parameterTypes.length >= 2 && String.class.equals(parameterTypes[0])
                        && (int.class.equals(parameterTypes[1]) || Integer.class.equals(parameterTypes[1]));
            }
        }
        return false;
    }

    /**
     * Register check method.
     *
     * @param method the method
     */
    private void registerCheckMethod(Method method) {
        var fieldName = method.getName().
                substring(DEFAULT_CHECK_METHOD_PREFIX.length());
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        this.checkMethodMap.put(fieldName, method);
        log.info("Registered check method [" + method.getName() + "]");
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
        bindClass = this.getGenericClass();
        val declaredFields = bindClass.getDeclaredFields();
        val fieldNameArray = new String[declaredFields.length];
        for (var index = 0; index < declaredFields.length; index++) {
            val declaredField = declaredFields[index];
            fieldNameArray[index] = declaredField.getName();
        }
        log.info("Generated {} field name array by reflection, fieldNameArray: {}", bindClass.getSimpleName(),
                 fieldNameArray);
        this.setFieldNameArray(fieldNameArray);
    }

    /**
     * Gets generic class.
     *
     * @return the generic class
     */
    private Class<ExcelImportBeanType> getGenericClass() {
        val type = getClass().getGenericSuperclass();
        log.info("Got type by reflection, typeName: {}", type.getTypeName());
        if (type instanceof ParameterizedType) {
            val parameterizedType = (ParameterizedType) type;
            val typeName = parameterizedType.getActualTypeArguments()[0].getTypeName();
            Class<ExcelImportBeanType> aClass;
            try {
                //noinspection unchecked
                aClass = (Class<ExcelImportBeanType>) Class.forName(typeName);
            } catch (ClassNotFoundException e) {
                log.error("Exception occurred when looking for class!", e);
                throw new RuntimeException(
                        "Exception occurred when looking for class! Exception message：" + e.getMessage());
            }
            return aClass;
        }
        throw new RuntimeException("Cannot find the type from the generic class!");
    }

    /**
     * Execute database operation.
     *
     * @param beanList the bean list that can be used for DB operations
     * @throws Exception the exception
     */
    protected abstract void executeDatabaseOperation(List<ExcelImportBeanType> beanList) throws Exception;

    /**
     * Validate before adding to bean list boolean.
     *
     * @param beanList the bean list that contains validated bean
     * @param bean     the bean that needs to be validated
     * @param index    the index that is the reference to the row number of the Excel file
     * @return the boolean
     * @throws Exception the exception
     */
    protected abstract boolean validateBeforeAddToBeanList(List<ExcelImportBeanType> beanList,
                                                           ExcelImportBeanType bean,
                                                           int index) throws Exception;

    /**
     * Sets field name array.
     *
     * @param fieldNameArray the field name array
     */
    public void setFieldNameArray(String[] fieldNameArray) {
        this.fieldNameArray = fieldNameArray;
    }

    /**
     * 上传文件到本地（暂时）
     * <p>
     * TODO: check the method if is necessary or not
     *
     * @param itemBytes the item bytes
     * @param fileName  the file name
     * @return File file
     */
    private File uploadTempFile(byte[] itemBytes, String fileName) {
        val fileFullPath = TEMP_FILE_PATH + fileName;
        log.info("上传文件到本地（暂时）。文件绝对路径：{}", fileFullPath);
        // 新建文件
        val file = new File(fileFullPath);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdir();
        }
        // 上传
        // FileCopyUtils.copy(itemBytes, file);
        return file;
    }

    /**
     * Upload file.
     *
     * @param request the request
     * @return File file
     * @throws IOException the io exception
     * @see
     * <a href='https://blog.sayem.dev/2017/07/upload-large-files-spring-boot-html/'>Upload large files : Spring Boot</a>
     */
    private File uploadFile(MultipartHttpServletRequest request) throws IOException {
        val multipartFile = request.getFileMap().get(FILE_KEY);
        // Don't do this.
        // it loads all of the bytes in java heap memory that leads to OutOfMemoryError. We'll use stream instead.
        // byte[] fileBytes = multipartFile.getBytes();
        @Cleanup val fileStream = new BufferedInputStream(multipartFile.getInputStream());
        val fileName = DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS") + multipartFile.getOriginalFilename();
        val targetFile = new File(TEMP_FILE_PATH + fileName);
        FileUtils.copyInputStreamToFile(fileStream, targetFile);
        uploadFileToSftp(targetFile);
        return targetFile;
    }

    /**
     * Read the file into workbook.
     *
     * @param file the file
     * @return the workbook
     * @throws IOException the io exception
     */
    private Workbook readFile(@NonNull File file) throws IOException {
        Workbook workbook = null;
        val extension = FilenameUtils.getExtension(file.getName());
        val bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        if (XLS.equals(extension)) {
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(bufferedInputStream);
            workbook = new HSSFWorkbook(poifsFileSystem);
            bufferedInputStream.close();
        } else if (XLSX.equals(extension)) {
            workbook = new XSSFWorkbook(bufferedInputStream);
        }
        return workbook;
    }

    /**
     * Upload file to SFTP
     *
     * @param file the file
     */
    private void uploadFileToSftp(File file) {
        val sftpUploadFile = SftpUploadFile.builder()
                .fileToBeUploaded(file)
                .fileExistsMode(FileExistsMode.REPLACE)
                .subDirectory(SFTP_DIR).build();
        try {
            sftpHelper.upload(sftpUploadFile);
        } catch (Exception e) {
            log.error("Exception occurred when uploading file to SFTP! Exception message：{}", e.getMessage());
        }
    }

    /**
     * Filter sheet. In default, will proceed all sheets.
     *
     * @param sheet the sheet
     * @return the boolean
     */
    protected boolean filterSheet(Sheet sheet) {
        return true;
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
            if (!filterSheet(sheet)) {
                continue;
            }
            // Specify current sheet location
            sheetLocation.set(index + 1);
            sheet.getSheetName();
            // Set Reading row count
            readingRowCount.set(readingRowCount.get() + sheet.getLastRowNum() - sheet.getFirstRowNum());
            // Check if exceeding the MAXIMUM_ROW_COUNT
            if (readingRowCount.get() > excelImportConfiguration.getMaximumRowCount()) {
                setErrorMessage(String.format(
                        "The amount of importing data cannot be greater than %d (Table head included)! " +
                                "Current reading row count: %d", excelImportConfiguration.getMaximumRowCount(),
                        readingRowCount.get()));
                continue;
            }
            // Check if the readingRowCount is equal to zero
            if (readingRowCount.get() == 0) {
                setErrorMessage("Not available data to import. Check Excel again.");
                continue;
            }
            // Check if the first row is equal to null
            if (sheet.getRow(0) == null) {
                setErrorMessage(String.format("Sheet [%s] (No. %d) format is invalid: no title! ", sheet.getSheetName(),
                                              sheetLocation.get()));
                // If the sheet is not valid, then skip it
                continue;
            }
            val startIndex = sheet.getRow(0).getFirstCellNum();
            val endIndex = sheet.getRow(0).getLastCellNum();
            // Parse from the second row
            for (var i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                // Specify current row location
                rowLocation.set(i + 1);
                val row = sheet.getRow(i);
                if (isBlankRow(row)) {
                    // Blank row will not be considered as a effective row, not be included in `readingRowCount`
                    readingRowCount.set(readingRowCount.get() - 1);
                    continue;
                }
                // Bind row to bean
                bindRowToBean(row, startIndex, endIndex);
            }
        }
    }

    /**
     * Check if the row is blank. If there is one cell of the row is not blank, then the row is not blank.
     *
     * @param row the row
     * @return true if the row is blank; or vice versa.
     */
    private boolean isBlankRow(Row row) {
        if (row == null) {
            return true;
        }
        Cell cell;
        var value = "";
        for (var i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = cell.getStringCellValue().trim();
                        break;
                    case NUMERIC:
                        value = String.valueOf((int) cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        value = String.valueOf(cell.getBooleanCellValue());
                        break;
                    case FORMULA:
                        value = String.valueOf(cell.getCellFormula());
                        break;
                    default:
                        break;
                }
                if (StrUtil.isNotBlank(value)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Bind row to bean.
     *
     * @param row        the row
     * @param startIndex the start index
     * @param endIndex   the end index
     */
    private void bindRowToBean(Row row, int startIndex, int endIndex) {
        Assert.notNull(this.bindClass, "bindClass must not be null!");
        Assert.notNull(this.fieldNameArray, "fieldNameArray must not be null!");
        var bindingResult = false;
        try {
            // New bean instance
            val bean = this.bindClass.getDeclaredConstructor().newInstance();
            val fields = this.bindClass.getDeclaredFields();
            for (var index = startIndex; index < endIndex; index++) {
                // If found more data, then binding failed
                if (index >= fieldNameArray.length) {
                    bindingResult = false;
                    setErrorMessage(
                            String.format("Found redundant data on row number %d. Check again.", rowLocation.get()));
                    break;
                }
                // Get the field that needs binding
                Field field = null;
                val fieldName = fieldNameArray[index];
                for (Field f : fields) {
                    if (f.getName().equals(fieldName)) {
                        field = f;
                        break;
                    }
                }
                if (field != null) {
                    val value = getCellValue2String(row.getCell(index));
                    // Start to bind
                    // Specify current column location
                    columnLocation.set(index + 1);
                    // Execute custom validation method
                    val method = this.checkMethodMap.get(fieldName);
                    Object returnObj = null;
                    if (method != null) {
                        method.setAccessible(true);
                        returnObj = method.invoke(this, value, rowLocation.get(), bean);
                        method.setAccessible(false);
                    }
                    val validationResult = returnObj == null ? null : returnObj.toString();
                    // If `validationResult` is blank or equal to "true"
                    if (StrUtil.isBlank(validationResult) || Boolean.TRUE.toString().equals(validationResult)) {
                        bindingResult = bind(value, field, bean);
                    } else {
                        bindingResult = false;
                        // If `validationResult` is not equal to "false" then add to error message list
                        if (!Boolean.FALSE.toString().equals(validationResult)) {
                            setErrorMessage(validationResult);
                        }
                    }
                    // Finished binding
                    if (!bindingResult) {
                        break;
                    }
                } else {
                    throw new RuntimeException("Cannot find the field in the specify class!");
                }
            }
            if (bindingResult) {
                bindingResult = validateBeforeAddToBeanList(this.beanList.get(), bean, rowLocation.get());
            }
            if (bindingResult) {
                this.beanList.get().add(bean);
            }
        } catch (Exception e) {
            log.error("bindRowToBean method has encountered a problem!", e);
            exceptionOccurred.set(true);
            val errorMessage = String.format(
                    "Exception occurred when binding and validating the data of row number %d. " +
                            "Exception message: %s", rowLocation.get(), e.getMessage());
            setErrorMessage(errorMessage);
            val lastCellNum = row.getLastCellNum();
            val errorInformationCell = row.createCell(fieldNameArray.length);
            errorInformationCell.setCellValue(errorMessage);
            val firstSheet = workbookWithErrorMessage.get().getSheetAt(0);
            val row1 = firstSheet.createRow(firstSheet.getLastRowNum() + 1);
            PoiUtil.copyRow(true, workbookWithErrorMessage.get(), row, row1);
        }
    }

    /**
     * Gets cell value 2 string.
     *
     * @param cell the cell
     * @return the string
     */
    private String getCellValue2String(Cell cell) {
        var returnString = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case BLANK:
                    return "";
                case NUMERIC:
                    // If it's date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        val date = cell.getDateCellValue();
                        val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        returnString = dateFormat.format(date);
                    } else {
                        val bigDecimal = new BigDecimal(String.valueOf(cell.getNumericCellValue()).trim());
                        // Keep decimal fraction parts which are not zero
                        val tempStr = bigDecimal.toPlainString();
                        val dotIndex = tempStr.indexOf(".");
                        if ((bigDecimal.doubleValue() - bigDecimal.longValue()) == 0 && dotIndex > 0) {
                            returnString = tempStr.substring(0, dotIndex);
                        } else {
                            returnString = tempStr;
                        }
                    }
                    break;
                case STRING:
                    if (cell.getStringCellValue() != null) {
                        returnString = cell.getStringCellValue().trim();
                    }
                    break;
                default:
            }
        }
        return returnString;
    }

    /**
     * Register bind handler methods.
     */
    private void registerBindHandlerMethods() {
        try {
            val bindStringField = ReflectionUtils.findMethod(this.getClass(),
                                                             "bindStringField",
                                                             String.class,
                                                             Field.class,
                                                             Object.class);
            val bindIntField = ReflectionUtils.findMethod(this.getClass(),
                                                          "bindIntField",
                                                          String.class,
                                                          Field.class,
                                                          Object.class);
            val bindLongField = ReflectionUtils.findMethod(this.getClass(),
                                                           "bindLongField",
                                                           String.class,
                                                           Field.class,
                                                           Object.class);
            val bindFloatField = ReflectionUtils.findMethod(this.getClass(),
                                                            "bindFloatField",
                                                            String.class,
                                                            Field.class,
                                                            Object.class);
            val bindDoubleField = ReflectionUtils.findMethod(this.getClass(),
                                                             "bindDoubleField",
                                                             String.class,
                                                             Field.class,
                                                             Object.class);
            val bindDateField = ReflectionUtils.findMethod(this.getClass(),
                                                           "bindDateField",
                                                           String.class,
                                                           Field.class,
                                                           Object.class);
            this.bindMethodMap.put(String.class, bindStringField);
            this.bindMethodMap.put(Integer.class, bindIntField);
            this.bindMethodMap.put(Long.class, bindLongField);
            this.bindMethodMap.put(Float.class, bindFloatField);
            this.bindMethodMap.put(Double.class, bindDoubleField);
            this.bindMethodMap.put(Date.class, bindDateField);
        } catch (Exception e) {
            log.error("The bindMethod required was not found in this class!", e);
        }
    }

    /**
     * Real binding value to the bean's field.
     *
     * @param value the string value of the excel cell
     * @param field the the field of the bean
     * @param bean  the bean
     * @return true if the binding succeeded, or vice versa.
     * @throws RuntimeException the runtime exception
     */
    private Boolean bind(String value, Field field, Object bean) throws RuntimeException {
        boolean result;
        val fieldType = field.getType();
        if (this.bindMethodMap.containsKey(fieldType)) {
            val bindMethod = this.bindMethodMap.get(fieldType);
            value = StrUtil.isBlank(value) ? null : value;
            try {
                result = (Boolean) bindMethod.invoke(this, value, field, bean);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                val exceptionMessage = new StringBuilder("Exception occurred when binding! ");
                if (!StrUtil.isBlank(e.getMessage())) {
                    log.error("Exception occurred when invoking method!", e);
                    exceptionMessage.append(e.getMessage()).append(" ");
                }
                if (ObjectUtil.isNotNull(e.getCause()) && !StrUtil.isBlank(e.getCause().getMessage())) {
                    log.error("Exception occurred when invoking method!", e.getCause());
                    exceptionMessage.append(e.getCause().getMessage());
                }
                throw new RuntimeException(exceptionMessage.toString());
            }
        } else {
            throw new RuntimeException("The bindMethod required was not found in bindMethodMap!");
        }
        return result;
    }

    /**
     * Bind int field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindIntField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            var realValue = value == null ? null : Integer.parseInt(value);
            if (realValue == null && field.getType() == int.class) {
                realValue = 0;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("Exception occurred when binding int/Integer field! Exception message: {}, value: {}, field: {}",
                      e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be integer",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * Bind long field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindLongField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            var realValue = value == null ? null : (long) Double.parseDouble(value);
            if (realValue == null && field.getType() == long.class) {
                realValue = 0L;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("Exception occurred when binding long/Long field! Exception message: {}, value: {}, field: {}",
                      e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be long integer",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * Bind long field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindLongField(Long value, Field field, Object bean) throws IllegalAccessException {
        try {
            field.setAccessible(true);
            field.set(bean, value);
            field.setAccessible(false);
        } catch (IllegalArgumentException e) {
            log.error("Exception occurred when binding Long field! Exception message: {}, value: {}, field: {}",
                      e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be Long integer",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * Bind float field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindFloatField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            var realValue = value == null ? null : Float.parseFloat(value);
            if (realValue == null && field.getType() == float.class) {
                realValue = 0F;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("Exception occurred when binding float/Float field! Exception message: {}, value: {}, field: {}",
                      e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be float",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * Bind double field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindDoubleField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            var realValue = value == null ? null : Double.parseDouble(value);
            if (realValue == null && field.getType() == double.class) {
                realValue = 0D;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error(
                    "Exception occurred when binding double/Double field! Exception message: {}, value: {}, field: {}",
                    e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be double",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * Bind string field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindStringField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            field.setAccessible(true);
            field.set(bean, value);
            field.setAccessible(false);
        } catch (IllegalArgumentException e) {
            log.error("Exception occurred when binding String field! Exception message: {}, value: {}, field: {}",
                      e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be string",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * Bind date field boolean.
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindDateField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            val date = value == null ? null : simpleDateFormat.parse(value);
            field.setAccessible(true);
            field.set(bean, date);
            field.setAccessible(false);
        } catch (ParseException e) {
            log.error("Exception occurred when binding Date field! Exception message: {}, value: {}, field: {}",
                      e.getMessage(), value, field.getName());
            val formattedMessage = String.format("Invalid data of the row %d, col %d, must be date",
                                                 rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
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
        setReturnMessageList(errorInfo);
    }

    /**
     * Sets return message list.
     *
     * @param message the message
     */
    protected void setReturnMessageList(String message) {
        this.returnMessageList.get().add(message);
    }
}
