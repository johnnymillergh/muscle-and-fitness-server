package com.jmsoftware.maf.springcloudstarter.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.jmsoftware.maf.common.bean.ExcelImportResult;
import com.jmsoftware.maf.common.bean.ResponseBodyBean;
import com.jmsoftware.maf.springcloudstarter.util.PoiUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.utils.DateUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <h1>AbstractExcelImportController</h1>
 * <p>Excel 表格数据导入抽象类。</p>
 * <h1>亮点功能</h1>
 * <p>经过 <code>钟俊 (jun.zhong)</code> 改造，给该类增添许多令人激动的新特性：</p>
 * <ol>
 * <li>将校验有错误、不通过的 Excel 的行，复制到一个新的 Excel 表中，将校验有误信息放在最后一列，通过将这个新的 Excel 表上传到 UDFS，提供文件 URL 给前端进行下载；</li>
 * <li>范型的加持  - <code>ExcelImportBeanType</code>。在 <code>ExcelImportBeanType</code> 中定义 Excel 表的中各个列数据（即表头）；</li>
 * <li>在自定义表头类 <code>ExcelImportBeanType</code> 中使用 <code>@ExcelColumn</code> 来对表头对中文名称进行标注，可以达到事半功倍对效果。还增加 Excel
 * 单元格类型的支持，默认为 <code>Cell.CELL_TYPE_STRING</code>；</li>
 * <li>支持部分导入。</li>
 *
 * </ol>
 * <h1>理解生命周期（生命钩子）</h1>
 * <p>理解生命周期有助于减少编写代码时出现意想不到的错误。以下是该类的生命周期的解释，参考方法引用：<code>com.ucarinc.inner.asset.common.base
 * .AbstractExcelImportController#AbstractExcelImportController</code>、<code>com.ucarinc.inner.asset.common.base
 * .AbstractExcelImportController#upload</code>。</p>
 * <h2>I. initContext()</h2>
 * <p>初始化上下文。通过反射生成 ExcelImportBeanType 字段名称数组。</p>
 * <h2>II. registerBindHandlerMethods()</h2>
 * <p>注册绑定方法。</p>
 * <h2>III. registerHandlerMethods()</h2>
 * <p>注册默认匹配规则的校验方法。</p>
 * <h2>IV. beforeExecute()</h2>
 * <p>默认不执行操作。可以被子类重写覆盖。</p>
 * <h2>V. initLocaleContext()</h2>
 * <p>初始化当前线程的现场上下文，主要的工作有：</p>
 * <ol>
 * <li>对各个当前线程持有的变量进行初始化，如：<code>errorMessageList</code> 错误信息列表、<code>beanList</code>
 * 实体类列表、<code>returnMessageList</code> 需要返回的信息列表、<code>rowLocation</code> 当前行位置、<code>columnLocation</code>
 * 当前列位置、<code>sheetLocation</code> 当前 Excel 表单位置、<code>readingRowCount</code> 总读取行数…</li>
 * <li>在完成初始化现场上下文后，开始对 Excel 表进行上传、读取、绑定数据、校验数据。</li>
 *
 * </ol>
 * <h2>VI. beforeDatabaseOperation()</h2>
 * <p>执行插入数据前操作，默认不执行任何操作。可以被子类重写覆盖。</p>
 * <h2>VII. executeDatabaseOperation()</h2>
 * <p>执行自定义的数据库操作。本方法为抽象方法，必须由子类实现。</p>
 * <h2>VIII. onExceptionOccurred()</h2>
 * <p>当前面当生命周期中，发生了任何异常，该方法会被回调。本方法为抽象方法，必须由子类实现。</p>
 * <h2>Ⅸ. afterExecute()</h2>
 * <p>执行后钩子方法。删除文件。</p>
 * <h2>X. destroyLocaleContext()</h2>
 * <p>销毁当前线程的现场环境。与 <code>initLocaleContext()</code> 相对应。</p>
 *
 * @param <ExcelImportBeanType> 导入 Excel 表的 bean 的类型
 * @author 钟俊 (jun.zhong), email: jun.zhong@ucarinc.com
 * @date 4/28/20 2:32 PM
 */
@Slf4j
@SuppressWarnings({"unused"})
public abstract class AbstractExcelImportController<ExcelImportBeanType> {
    /**
     * 本地临时文件存放地址
     */
    private static final String TEMP_FILE_PATH =
            new ApplicationHome(AbstractExcelImportController.class).getSource()
                    .getParent() + "/temp-file/";
    /**
     * FTP 上传文件路径文件夹名称
     */
    private static final String FTP_DIR = "excel/";
    /**
     * 默认的访问请求中对应上传文件的键值对的 key 为 upload_file
     */
    private static final String FILE_KEY = "upload_file";
    /**
     * The constant HTTP.
     */
    private static final String HTTP = "http://";
    /**
     * 默认校验方法前缀
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
     * 导入总条数
     */
    private static final Integer MAXIMUM_ROW_COUNT = 2000;
    /**
     * The User defined message.
     */
    protected ThreadLocal<String> userDefinedMessage = new ThreadLocal<>();
    /**
     * 当出现数据校验失败时是否全部回滚。默认为 true
     */
    private Boolean denyAll = Boolean.TRUE;
    /**
     * Excel 导入的数据绑定的类型
     */
    private Class<ExcelImportBeanType> bindClass;
    /**
     * Excel 导入的数据依据数据类型不同指定的不同绑定方法
     */
    private final Map<Class<?>, Method> bindMethodMap = new HashMap<>();
    /**
     * Excel 导入的列按从左到右顺序对应的 bingClass 中的属性名称数组
     */
    private String[] fieldNameArray;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 自定义校验方法，默认匹配原则：
     * 以 check 方法开头，接需要校验的属性字段名称，参数为 List list，返回值为 String 或 Boolean。
     * <p>
     * e.g public String checkName(List list)
     */
    private final Map<String, Method> checkMethodMap = new HashMap<>();
    /**
     * 绑定数据时当前扫描的 Sheet，-1 时为用户自定义数据绑定。
     */
    private final ThreadLocal<Integer> sheetLocation = new ThreadLocal<>();
    /**
     * 绑定数据时当前扫描位置坐标 X，-1时为用户自定义数据绑定。
     */
    private final ThreadLocal<Integer> rowLocation = new ThreadLocal<>();
    /**
     * 绑定数据时当前扫描位置坐标 Y，-1时为用户自定义数据绑定。
     */
    private final ThreadLocal<Integer> columnLocation = new ThreadLocal<>();
    /**
     * 总读取记录条数。
     */
    private final ThreadLocal<Integer> readingRowCount = new ThreadLocal<>();
    /**
     * 错误消息列表。最后将在前台展示的校验错误信息。
     */
    protected ThreadLocal<List<String>> errorMessageList = new ThreadLocal<>();
    /**
     * 最终解析完成的可操作数据列表
     */
    protected final ThreadLocal<List<ExcelImportBeanType>> beanList = new ThreadLocal<>();
    /**
     * 最终返回的消息集合
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

    /**
     * Gets fill name.
     *
     * @return the fill name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets fill name.
     *
     * @param fileName the file name
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * <p>AbstractExcelImportController 的构造方法</p>
     * <ol>
     * <li><p>执行默认的校验方法注册</p>
     * </li>
     * <li><p>执行默认的环境初始化</p>
     * </li>
     * </ol>
     */
    public AbstractExcelImportController() {
        initContext();
        registerBindHandlerMethods();
        registerHandlerMethods();
    }

    /**
     * 初始化当前线程的现场环境
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
     * 销毁当前线程的现场环境
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
        workbook.remove();
        workbookWithErrorMessage.remove();
        excelFilePath.remove();
        exceptionOccurred.remove();
        file.remove();
    }

    /**
     * 上传文件
     *
     * @param request  the request
     * @param response the response
     * @return String result
     */
    @PostMapping(value = "/upload", headers = "content-type=multipart/form-data")
    @ApiOperation(value = "上传 Excel 文件", notes = "上传 Excel 文件")
    public ResponseBodyBean<ExcelImportResult> upload(HttpServletRequest request, HttpServletResponse response) {
        beforeExecute();
        initLocaleContext();
        try {
            setReturnMessageList("开始 - 上传 Excel 文件……");
            file.set(uploadFile((MultipartHttpServletRequest) request));
            setReturnMessageList("完成 - 上传 Excel 文件……");
        } catch (IOException e) {
            log.error("上传 Excel 文件出现异常！", e);
            setErrorMessage("上传 Excel 文件出现异常！异常信息：" + e.getMessage());
        }
        try {
            setReturnMessageList("开始 - 读取 Excel 文件……");
            workbook.set(readFile(file.get()));
            setReturnMessageList("完成 - 读取 Excel 文件……");
        } catch (IOException e) {
            log.error("读取 Excel 文件时发生异常！", e);
            setErrorMessage("读取 Excel 文件时发生异常！异常信息：" + e.getMessage());
        }
        try {
            setReturnMessageList("开始 - 绑定数据、校验数据……");
            bindData(workbook.get());
            setReturnMessageList("完成 - 绑定数据、校验数据……");
        } catch (Exception e) {
            log.error("绑定数据、校验数据时发生异常！", e);
            setErrorMessage("绑定数据、校验数据时发生异常！异常信息：" + e.getMessage());
        }
        beforeDatabaseOperation(this.beanList.get());
        // 如果没有错误信息（errorMessageList 为空）或者不是 denyAll，则进行数据库操作
        if (CollectionUtil.isEmpty(this.errorMessageList.get()) || !denyAll) {
            if (CollectionUtil.isNotEmpty(this.beanList.get())) {
                setReturnMessageList("开始 - 导入数据……");
                try {
                    executeDatabaseOperation(this.beanList.get());
                    setReturnMessageList(String.format("完成 - 导入数据。本次成功导入 %d 条数据！", beanList.get().size()));
                } catch (Exception e) {
                    exceptionOccurred.set(true);
                    log.error("执行数据库操作时发生异常！", e);
                    setErrorMessage("执行数据库操作时发生异常！异常信息：" + e.getMessage());
                }
            } else {
                setReturnMessageList(String.format("完成 - 导入数据。本次成功导入 %d 条数据！", beanList.get().size()));
            }
        } else {
            setReturnMessageList("【警告】存在不符合要求的数据，数据全部导入失败！");
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
     * 执行前钩子方法，默认不执行任何操作
     */
    protected void beforeExecute() {
    }

    /**
     * 执行后钩子方法。删除文件。
     */
    protected void afterExecute() {
        Optional<File> optionalFile = Optional.ofNullable(file.get());
        optionalFile.ifPresent(file1 -> {
            boolean delete = file1.delete();
            if (delete) {
                log.warn("删除文件！{}", file1.getAbsolutePath());
            } else {
                log.warn("文件不能删除！{}", file1.getAbsolutePath());
            }
        });
        if (optionalFile.isEmpty()) {
            log.warn("文件不存在！");
        }
    }

    /**
     * 执行插入数据前操作，默认不执行任何操作
     *
     * @param beanList the bean list
     */
    protected void beforeDatabaseOperation(List<ExcelImportBeanType> beanList) {
    }

    /**
     * <p>注册默认匹配规则的校验方法。</p>
     * <ul>
     * <li><p>e.g: private Boolean checkXXX(String value,Integer index)</p>
     * </li>
     * <li><p>e.g: private String checkXXX(String value,Integer index)</p>
     * </li>
     * </ul>
     */
    private void registerHandlerMethods() {
        // 清除数据
        this.checkMethodMap.clear();
        // 注册符合默认匹配原则的方法
        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (isCheckMethod(method)) {
                registerCheckMethod(method);
            }
        }
    }

    /**
     * <p>检查是否符合默认匹配原则。</p>
     * <ol>
     * <li><p>以 check 方法开头，接需要校验的属性字段名称；</p>
     * </li>
     * <li><p>参数为 String value；</p>
     * </li>
     * <li><p>返回值为 Boolean</p>
     * </li>
     * </ol>
     */
    private Boolean isCheckMethod(Method method) {
        Class<?> returnType = method.getReturnType();
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
     * 注册校验方法
     *
     * @param method 将要被注册的方法对象
     */
    private void registerCheckMethod(Method method) {
        String fieldName = method.getName().
                substring(DEFAULT_CHECK_METHOD_PREFIX.length());
        fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
        this.checkMethodMap.put(fieldName, method);
        log.info("Register check method [" + method.getName() + "]");
    }

    /**
     * <p>初始化上下文。</p>
     * <ol>
     * <li><p>调用 <code>setBindClass(Class&amp;lt;?&amp;gt; bindClass)</code> 设置数据绑定类；</p>
     * </li>
     * <li><p>调用 <code>setFieldNamesArray(String[] fieldNameArray)</code> 设置数据绑定列(列名为绑定类中的属性名,顺序需和Excel导入的列顺序一致)；</p>
     * </li>
     * <li><p>调用 <code>registCustomCheckMethod(String fieldName,String methodName)</code> 添加自定义名称校验方法(默认会注册特定名称的校验方法)
     * ；</p>
     * </li>
     * <li><p>调用 <code>setIsDenyAll()</code> 配置修改当校验出现失败情况时的操作。true 为全部回退，false 为校验失败的回退。默认为 true。</p>
     * </li>
     * </ol>
     */
    protected void initContext() {
        bindClass = this.getGenericClass();
        Field[] declaredFields = bindClass.getDeclaredFields();
        String[] fieldNameArray = new String[declaredFields.length];
        for (int index = 0; index < declaredFields.length; index++) {
            Field declaredField = declaredFields[index];
            fieldNameArray[index] = declaredField.getName();
        }
        log.info("通过反射生成 {} 字段名称数组：{}", bindClass.getSimpleName(), fieldNameArray);
        this.setFieldNameArray(fieldNameArray);
    }

    @SuppressWarnings("unchecked")
    private Class<ExcelImportBeanType> getGenericClass() {
        Type type = getClass().getGenericSuperclass();
        log.info("通过反射获取范型的类型: {}", type.getTypeName());
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            String typeName = parameterizedType.getActualTypeArguments()[0].getTypeName();
            Class<ExcelImportBeanType> aClass;
            try {
                aClass = (Class<ExcelImportBeanType>) Class.forName(typeName);
            } catch (ClassNotFoundException e) {
                log.error("获取类的时候发生异常！", e);
                throw new RuntimeException("获取类的时候发生异常！异常原因：" + e.getMessage());
            }
            return aClass;
        }
        throw new RuntimeException("未能找到范型的类型！");
    }

    /**
     * 执行自定义的数据库操作。
     *
     * @param beanList 校验完成的所有可用于执行数据库操作的 bean 列表
     * @throws Exception the exception
     */
    protected abstract void executeDatabaseOperation(List<ExcelImportBeanType> beanList) throws Exception;

    /**
     * 整体校验，校验即将加入 beanList 的 bean 是否与之前的bean有冲突。
     *
     * @param beanList 已经校验通过的 bean 列表
     * @param bean     待校验的 bean
     * @param index    当前带校验 bean 实际指代的 Excel 文件数据所在的行索引
     * @return boolean boolean
     * @throws Exception the exception
     */
    protected abstract boolean validateBeforeAddToBeanList(List<ExcelImportBeanType> beanList,
                                                           ExcelImportBeanType bean,
                                                           int index) throws Exception;

    /**
     * 设置 fieldNameArray
     *
     * @param fieldNameArray bindClass 中需要绑定的列数组
     */
    public void setFieldNameArray(String[] fieldNameArray) {
        this.fieldNameArray = fieldNameArray;
    }

    /**
     * 上传文件到本地（暂时）
     *
     * @param itemBytes the item bytes
     * @param fileName  the file name
     * @return File file
     */
    private File uploadTempFile(byte[] itemBytes, String fileName) {
        String fileFullPath = TEMP_FILE_PATH + fileName;
        log.info("上传文件到本地（暂时）。文件绝对路径：{}", fileFullPath);
        // 新建文件
        File file = new File(fileFullPath);
        if (!file.getParentFile().exists()) {
            //noinspection ResultOfMethodCallIgnored
            file.getParentFile().mkdir();
        }
        // 上传
        // FileCopyUtils.copy(itemBytes, file);
        return file;
    }

    /**
     * 上传文件。
     *
     * @param request the request
     * @return File file
     * @throws IOException the io exception
     * @see
     * <a href='https://blog.sayem.dev/2017/07/upload-large-files-spring-boot-html/'>Upload large files : Spring Boot</a>
     */
    private File uploadFile(MultipartHttpServletRequest request) throws IOException {
        // 获得字节流
        MultipartFile multipartFile = request.getFileMap().get(FILE_KEY);
        // Don't do this.
        // it loads all of the bytes in java heap memory that leads to OutOfMemoryError. We'll use stream instead.
        // byte[] fileBytes = multipartFile.getBytes();
        InputStream fileStream = multipartFile.getInputStream();
        String fileName = DateUtils.formatDate(new Date(), "yyyyMMddHHmmssSSS") + multipartFile.getOriginalFilename();
        File targetFile = new File(TEMP_FILE_PATH + fileName);
        FileUtils.copyInputStreamToFile(fileStream, targetFile);
        // byte[] itemBytes = multipartFile.getBytes();
        // File excelFile = uploadTempFile(itemBytes, fileName);
        // uploadFileToFtp(excelFile, FTP_DIR);
        return targetFile;
    }

    /**
     * 读取 Excel 文件置入 Workbook 实例对象中。
     *
     * @param file 需要读取的文件对象
     * @return Workbook workbook
     * @throws IOException the io exception
     */
    private Workbook readFile(File file) throws IOException {
        Assert.notNull(file, "file must not be null!");
        Workbook workbook = null;
        String extension = FilenameUtils.getExtension(file.getName());
        if (XLS.equals(extension)) {
            FileInputStream fileInputStream = new FileInputStream(file);
            POIFSFileSystem poifsFileSystem = new POIFSFileSystem(fileInputStream);
            workbook = new HSSFWorkbook(poifsFileSystem);
            fileInputStream.close();
        } else if (XLSX.equals(extension)) {
            workbook = new XSSFWorkbook(new FileInputStream(file));
        }
        return workbook;
    }

    /**
     * Upload file to ftp.
     *
     * @param file the file
     * @param path the path
     */
    private void uploadFileToFtp(File file, String path) {
//        FtpUtil ftp = FtpUtil.getInstance();
//        try {
//            InputStream in = new FileInputStream(file);
//            ftp.connectServer();
//            ftp.upload(path + DateUtils.formatDate(new Date(), "yyyyMM"), new String(file.getName().getBytes("GBK"),
//                                                                                     StandardCharsets.ISO_8859_1), in);
//        } catch (IOException e) {
//            log.error("上传文件至 FTP 时发生异常！异常信息：{}", e.getMessage());
//        } finally {
//            ftp.closeConnect();
//        }
    }

    /**
     * 过滤不需要处理的 sheet 页。默认所有 sheet 都处理。
     *
     * @param sheet the sheet
     * @return the boolean
     */
    protected boolean sheetFilter(Sheet sheet) {
        return true;
    }

    /**
     * <p>绑定数据到对象上。所做的工作有：</p>
     * <ul>
     * <li><p>依据 fieldNameArray 绑定数据至 bindClass 实例上</p>
     * </li>
     * <li><p>通过回调，校验字段的有效性</p>
     * </li>
     * </ul>
     *
     * @param workbook the workbook
     */
    @SuppressWarnings("RedundantThrows")
    private void bindData(Workbook workbook) throws Exception {
        for (int k = 0; k < workbook.getNumberOfSheets(); k++) {
            Sheet sheet = workbook.getSheetAt(k);
            // 过滤掉不需要处理的 sheet 页
            if (!sheetFilter(sheet)) {
                continue;
            }
            // 指定当前 sheet 位置
            sheetLocation.set(k + 1);
            sheet.getSheetName();
            // 计算总记录数
            readingRowCount.set(readingRowCount.get() + sheet.getLastRowNum() - sheet.getFirstRowNum());
            // 验证导入数量是否大于最大值
            if (readingRowCount.get() > MAXIMUM_ROW_COUNT) {
                setErrorMessage(String.format("导入条数不能大于 %d 条（表头也计入行数）！当前总条数：%d", MAXIMUM_ROW_COUNT,
                                              readingRowCount.get()));
                continue;
            }
            // 验证导入数量是否大于最大值
            if (readingRowCount.get() == 0) {
                setErrorMessage("没有可导入的数据，请检查。");
                continue;
            }
            // 获取开始结束位置
            if (sheet.getRow(0) == null) {
                // sheet 页格式不合法，没有标题，本页不解析
                setErrorMessage(String.format("第 %d 页表单格式不合法，没有标题。", sheetLocation.get()));
                continue;
            }
            int startIndex = sheet.getRow(0).getFirstCellNum();
            int endIndex = sheet.getRow(0).getLastCellNum();
            // 从第二行开始解析
            for (int i = sheet.getFirstRowNum() + 1; i <= sheet.getLastRowNum(); i++) {
                // 指定当前坐标X
                rowLocation.set(i + 1);
                Row row = sheet.getRow(i);
                if (isBlankRow(row)) {
                    // 空行不计入总行数
                    readingRowCount.set(readingRowCount.get() - 1);
                    continue;
                }
                // 绑定到实例 bean
                bindRowToBean(row, startIndex, endIndex);
            }
        }
    }

    /**
     * 判断是否为空行
     *
     * @param row the row
     * @return the boolean
     */
    private boolean isBlankRow(Row row) {
        if (row == null) {
            return true;
        }
        boolean result = true;
        for (int i = row.getFirstCellNum(); i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
            String value = "";
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        value = cell.getStringCellValue();
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
                if (!"".equals(value.trim())) {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 绑定行到数据对象上
     *
     * @param row        Row
     * @param startIndex the start index
     * @param endIndex   the end index
     */
    private void bindRowToBean(Row row, int startIndex, int endIndex) {
        Assert.notNull(this.bindClass, "bindClass must not be null!");
        Assert.notNull(this.fieldNameArray, "fieldNameArray must not be null!");
        boolean bindingResult = false;
        try {
            // 新建数据实例
            ExcelImportBeanType bean = this.bindClass.getDeclaredConstructor().newInstance();
            Field[] fields = this.bindClass.getDeclaredFields();
            for (int i = startIndex; i < endIndex; i++) {
                // 出现多余数据，则当前行绑定失效
                if (i >= fieldNameArray.length) {
                    bindingResult = false;
                    setErrorMessage(String.format("第 %d 行存在多余数据，请检查。", rowLocation.get()));
                    break;
                }
                // 获取需要绑定的属性名称
                Field field = null;
                String fieldName = fieldNameArray[i];
                // 获取需要绑定的Field实例
                for (Field f : fields) {
                    if (f.getName().equals(fieldName)) {
                        field = f;
                        break;
                    }
                }
                if (field != null) {
                    String value = getCellValue2String(row.getCell(i));
                    // 绑定开始
                    // 指定当前坐标Y
                    columnLocation.set(i + 1);
                    // 执行自定义校验
                    Method method = this.checkMethodMap.get(fieldName);
                    Object returnObj = null;
                    if (method != null) {
                        method.setAccessible(true);
                        returnObj = method.invoke(this, value, rowLocation.get(), bean);
                        method.setAccessible(false);
                    }
                    String returnStr = returnObj == null ? null : returnObj.toString();
                    if (null == returnStr || "".equals(returnStr) || "true".equals(returnStr)) {
                        // 执行数据绑定
                        bindingResult = bind(value, field, bean);
                    } else {
                        bindingResult = false;
                        // 添加自定义校验错误信息
                        if (!"false".equals(returnStr)) {
                            setErrorMessage(returnStr);
                        }
                    }
                    // 绑定结束
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
            String errorMessage = String.format("绑定并校验第 %d 行数据时发生异常，异常信息：%s", rowLocation.get(), e.getMessage());
            setErrorMessage(errorMessage);
            short lastCellNum = row.getLastCellNum();
            Cell errorInformationCell = row.createCell(fieldNameArray.length);
            errorInformationCell.setCellValue(errorMessage);
            Sheet firstSheet = workbookWithErrorMessage.get().getSheetAt(0);
            Row row1 = firstSheet.createRow(firstSheet.getLastRowNum() + 1);
            PoiUtil.copyRow(true, workbookWithErrorMessage.get(), row, row1);
        }
    }

    /**
     * 转换单元格信息为 String 字符创
     *
     * @param cell the cell
     * @return the cell value 2 string
     */
    private String getCellValue2String(Cell cell) {
        String returnString = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case BLANK:
                    return "";
                case NUMERIC:
                    // 如果为输入为日期格式
                    if (DateUtil.isCellDateFormatted(cell)) {
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        returnString = dateFormat.format(date);
                    } else {
                        BigDecimal bg = new BigDecimal(String.valueOf(cell.getNumericCellValue()).trim());
                        // 若小数部分为 0 则仅保留整数部分
                        String tempStr = bg.toPlainString();
                        int dotIndex = tempStr.indexOf(".");
                        if ((bg.doubleValue() - bg.longValue()) == 0 && dotIndex > 0) {
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
     * 注册绑定方法
     */
    protected void registerBindHandlerMethods() {
        try {
            Method bindStringField = ReflectionUtils.findMethod(this.getClass(),
                                                                "bindStringField",
                                                                String.class,
                                                                Field.class,
                                                                Object.class);
            Method bindIntField = ReflectionUtils.findMethod(this.getClass(),
                                                             "bindIntField",
                                                             String.class,
                                                             Field.class,
                                                             Object.class);
            Method bindLongField = ReflectionUtils.findMethod(this.getClass(),
                                                              "bindLongField",
                                                              String.class,
                                                              Field.class,
                                                              Object.class);
            Method bindFloatField = ReflectionUtils.findMethod(this.getClass(),
                                                               "bindFloatField",
                                                               String.class,
                                                               Field.class,
                                                               Object.class);
            Method bindDoubleField = ReflectionUtils.findMethod(this.getClass(),
                                                                "bindDoubleField",
                                                                String.class,
                                                                Field.class,
                                                                Object.class);
            Method bindDateField = ReflectionUtils.findMethod(this.getClass(),
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
     * 实际绑定动作
     *
     * @param value Excel 文件对应单元格数据的字符串形式
     * @param field 将要绑定到的 bean 的对应属性对象
     * @param bean  将要绑定到的 bean
     * @return the boolean
     * @throws RuntimeException the runtime exception
     */
    private Boolean bind(String value, Field field, Object bean) throws RuntimeException {
        boolean result;
        Class<?> fieldType = field.getType();
        if (this.bindMethodMap.containsKey(fieldType)) {
            Method bindMethod = this.bindMethodMap.get(fieldType);
            // 转换空字符串为 null
            value = StrUtil.isBlank(value) ? null : value;
            try {
                result = (Boolean) bindMethod.invoke(this, value, field, bean);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                StringBuilder exceptionMessage = new StringBuilder("绑定数据时发生异常！");
                if (!StrUtil.isBlank(e.getMessage())) {
                    log.error("Exception occurred when invoking method!", e);
                    exceptionMessage.append(e.getMessage());
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
     * 绑定 int，Integer 属性
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindIntField(String value, Field field, Object bean) throws IllegalAccessException {
        try {

            Integer realValue = value == null ? null : Integer.parseInt(value);
            if (realValue == null && field.getType() == int.class) {
                realValue = 0;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("绑定 int，Integer 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行，第 %d 列数据格式有误，须为整数，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }


    /**
     * 绑定 long，Long 属性
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindLongField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            Long realValue = value == null ? null : (long) Double.parseDouble(value);
            if (realValue == null && field.getType() == long.class) {
                realValue = 0L;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("绑定 long，Long 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行，第 %d 列数据格式有误，须为整数，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * 绑定 float，Float 属性
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindFloatField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            Float realValue = value == null ? null : Float.parseFloat(value);
            if (realValue == null && field.getType() == float.class) {
                realValue = 0F;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("绑定 float，Float 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行，第 %d 列数据格式有误，须为单精度浮点数，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }


    /**
     * 绑定 double，Double 属性
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindDoubleField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            Double realValue = value == null ? null : Double.parseDouble(value);
            if (realValue == null && field.getType() == double.class) {
                realValue = 0D;
            }
            field.setAccessible(true);
            field.set(bean, realValue);
            field.setAccessible(false);
        } catch (NumberFormatException e) {
            log.error("绑定 double，Double 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行，第 %d 列数据格式有误，须为双精度浮点数，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * 绑定 String 属性
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
            log.error("绑定 String 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行,第 %d 列数据格式有误，须为字符串，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * 绑定 Long 属性
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
            log.error("绑定 Long 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行，第 %d 列数据格式有误，须为字符串，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * 绑定 Date 属性
     *
     * @param value the value
     * @param field the field
     * @param bean  the bean
     * @return the boolean
     * @throws IllegalAccessException the illegal access exception
     */
    private Boolean bindDateField(String value, Field field, Object bean) throws IllegalAccessException {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = value == null ? null : simpleDateFormat.parse(value);
            field.setAccessible(true);
            field.set(bean, date);
            field.setAccessible(false);
        } catch (ParseException e) {
            log.error("绑定 Date 属性时发生异常！异常信息：{}。Value：{}，field：{}", e.getMessage(), value, field.getName());
            String formattedMessage = String.format("第 %d 行，第 %d 列数据格式有误，须为 yyyy-MM-dd HH:mm 日期形式，请校对后重新导入。",
                                                    rowLocation.get(), columnLocation.get());
            setErrorMessage(formattedMessage);
            throw new IllegalArgumentException(formattedMessage);
        }
        return true;
    }

    /**
     * 允许通过配置修改当校验出现失败情况时的操作
     *
     * @param denyAll 出现异常时是否需要拒绝所有已经校验成功的 bean，true 为拒绝，false为不拒绝
     */
    public void setDenyAll(Boolean denyAll) {
        this.denyAll = denyAll;
    }

    /**
     * 可以使用该方法添加自定义校验错误信息
     *
     * @param errorInfo 错误信息
     */
    protected void setErrorMessage(String errorInfo) {
        this.errorMessageList.get().add(errorInfo);
        setReturnMessageList(errorInfo);
    }

    /**
     * 可以使用该方法添加自定义返回页面消息错误信息
     *
     * @param msg the msg
     */
    protected void setReturnMessageList(String msg) {
        this.returnMessageList.get().add(msg);
    }
}
