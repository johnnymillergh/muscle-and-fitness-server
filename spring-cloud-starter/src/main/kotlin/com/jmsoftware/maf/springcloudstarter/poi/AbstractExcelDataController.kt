package com.jmsoftware.maf.springcloudstarter.poi

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.poi.excel.ExcelReader
import cn.hutool.poi.excel.ExcelWriter
import cn.hutool.poi.excel.WorkbookUtil
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import com.jmsoftware.maf.common.bean.ExcelImportResult
import com.jmsoftware.maf.common.bean.ResponseBodyBean
import com.jmsoftware.maf.common.util.logger
import org.apache.poi.ss.usermodel.Workbook
import org.springframework.http.ContentDisposition
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.util.unit.DataSize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody
import java.io.*
import java.lang.reflect.ParameterizedType
import java.util.*
import javax.annotation.Resource

/**
 * # AbstractExcelDataController
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 4/14/22 4:00 PM
 **/
abstract class AbstractExcelDataController<T> {
    companion object {
        private val log = logger()
    }

    private val beanList: ThreadLocal<List<T>?> = ThreadLocal.withInitial { null }
    private val workbook: ThreadLocal<Workbook?> = ThreadLocal.withInitial<Workbook?> { null }
    private val excelFilePath: ThreadLocal<String?> = ThreadLocal.withInitial<String?> { null }
    private val exceptionOccurred: ThreadLocal<Boolean?> = ThreadLocal.withInitial { false }
    private val errorMessageList: ThreadLocal<MutableList<String>?> = ThreadLocal.withInitial { null }
    private val returnMessageList: ThreadLocal<MutableList<String>?> = ThreadLocal.withInitial { null }

    /**
     * `fileName` stores the name to upload the workbook when exception occurred
     */
    protected val fileName: ThreadLocal<String?> = ThreadLocal.withInitial<String?> { null }

    /**
     * | Key               | Value           |
     * | ----------------- | --------------- |
     * | Excel Column Name | excelColumnName |
     * | Title Name        | titleName       |
     */
    private val importingFieldAliasMap: MutableMap<String, String> = Maps.newLinkedHashMap()

    /**
     * | Key             | Value             |
     * | --------------- | ----------------- |
     * | excelColumnName | Excel Column Name |
     * | titleName       | Title Name        |
     */
    private val exportingFieldAliasMap: MutableMap<String, String> = Maps.newLinkedHashMap()

    @Resource
    protected lateinit var excelImportConfigurationProperties: ExcelImportConfigurationProperties

    @Resource
    protected lateinit var ossUploader: OssUploader

    /**
     * Deny all data when data validation fails. Default value is true.
     */
    private var denyAll = true
    private lateinit var beanClass: Class<T>
    private lateinit var fieldNameArray: Array<String>

    /**
     * Constructor of AbstractExcelDataController
     *
     *  1. Init context
     *  2. Register bind handler methods
     *  3. Register handler methods
     */
    init{
        initContext()
    }

    /**
     * Init context.
     *
     *  1. Call `getGenericClass()` to set Excel import type；
     *  2. Set fieldNameArray
     */
    private fun initContext() {
        beanClass = getGenericClass()
        val declaredFields = beanClass.declaredFields
        val fieldNames = mutableListOf<String>()
        for (index in declaredFields.indices) {
            val declaredField = declaredFields[index]
            fieldNames.add(declaredField.name)
            val excelColumn = declaredField.getAnnotation(ExcelColumn::class.java)
            var columnName = excelColumn?.name
            if (StrUtil.isBlank(columnName)) {
                columnName = StrUtil.toSymbolCase(declaredField.name, ' ').uppercase(Locale.getDefault())
            }
            importingFieldAliasMap[columnName!!] = declaredField.name
            exportingFieldAliasMap[declaredField.name] = columnName
        }
        fieldNameArray = fieldNames.toTypedArray()
        log.info("Generated ${beanClass.simpleName} field name array by reflection, fieldNames: $fieldNameArray")
    }

    private fun getGenericClass(): Class<T> {
        val type = this.javaClass.genericSuperclass
        log.info("Got type by reflection, typeName: ${type.typeName}")
        if (type is ParameterizedType) {
            val typeName: String = type.actualTypeArguments[0].typeName
            return try {
                @Suppress("UNCHECKED_CAST")
                Class.forName(typeName) as Class<T>
            } catch (e: ClassNotFoundException) {
                log.error("Exception occurred when looking for class!", e)
                throw IllegalArgumentException(e.message)
            }
        }
        throw IllegalArgumentException("Cannot find the type from the generic class!")
    }


    /**
     * Init locale context.
     */
    private fun initLocaleContext() {
        exceptionOccurred.set(false)
        errorMessageList.set(Lists.newLinkedList())
        returnMessageList.set(Lists.newLinkedList())
        log.debug("Initialized locale context: ${threadLocalToString()}")
    }

    /**
     * Destroy locale context.
     */
    private fun destroyLocaleContext() {
        beanList.remove()
        closeWorkbook(workbook.get())
        workbook.remove()
        excelFilePath.remove()
        exceptionOccurred.remove()
        errorMessageList.remove()
        returnMessageList.remove()
        fileName.remove()
        log.debug("Destroyed locale context and invalidate thread local variables: ${threadLocalToString()}")
    }


    private fun threadLocalToString(): String {
        val map = LinkedHashMap<String, Any?>()
        map["beanList"] = beanList.get()
        map["workbook"] = workbook.get()
        map["excelFilePath"] = excelFilePath.get()
        map["exceptionOccurred"] = exceptionOccurred.get()
        map["errorMessageList"] = errorMessageList.get()
        map["returnMessageList"] = returnMessageList.get()
        map["fileName"] = fileName.get()
        return map.toString()
    }

    /**
     * Before execute.
     */
    protected open fun beforeExecute() = Unit

    /**
     * On exception occurred.
     */
    protected abstract fun onExceptionOccurred()


    /**
     * Validate before adding to bean list boolean.
     *
     * @param beanList the bean list that contains validated bean
     * @param bean     the bean that needs to be validated
     * @param index    the index that is the reference to the row number of the Excel file
     * @throws IllegalArgumentException the illegal argument exception
     */
    protected abstract fun validateBeforeAddToBeanList(beanList: List<T>, bean: T, index: Int)

    /**
     * Before database operation.
     *
     * @param beanList the bean list
     */
    protected open fun beforeDatabaseOperation(beanList: List<T>) = Unit

    /**
     * Execute database operation.
     *
     * @param beanList the bean list that can be used for DB operations
     */
    protected abstract fun executeDatabaseOperation(beanList: List<T>)

    /**
     * After execute. Delete file.
     */
    protected open fun afterExecute() = Unit

    /**
     * Gets template file name.
     *
     * @return the template file name
     */
    protected abstract fun getTemplateFileName(): String

    /**
     * Gets list for exporting.
     *
     * @return the list for exporting
     */
    protected abstract fun getListForExporting(): List<T>

    /**
     * Before download to configure ExcelWriter. Such as hiding column.
     *
     * @param excelWriter the excel writer
     */
    protected open fun beforeDownload(excelWriter: ExcelWriter) = Unit

    @GetMapping("/stat/excel-template")
    open fun downloadExcelTemplate(): ResponseEntity<StreamingResponseBody> {
        val excelWriter = ExcelWriter(true)
        excelWriter.setHeaderAlias(exportingFieldAliasMap)
        excelWriter.write(getListForExporting())
        excelWriter.setFreezePane(1)
        excelWriter.autoSizeColumnAll()
        beforeDownload(excelWriter)
        return ResponseEntity.ok()
            .header(
                HttpHeaders.CONTENT_DISPOSITION,
                ContentDisposition.builder("attachment").filename(getTemplateFileName()).build().toString()
            )
            .body(StreamingResponseBody { outputStream: OutputStream ->
                excelWriter.flush(
                    outputStream,
                    true
                )
            })
    }

    /**
     * Upload Excel file. Any exceptions happened in any lifecycle will not interrupt the whole process.
     *
     * @param multipartFile the multipart file
     * @return the response body bean
     */
    @PostMapping(value = ["/upload/excel"])
    open fun upload(@RequestParam("file") multipartFile: MultipartFile): ResponseBodyBean<ExcelImportResult> {
        beforeExecute()
        initLocaleContext()
        readMultipartFile(multipartFile)
        bindData()
        beforeDatabaseOperation(beanList.get()!!)
        exeDatabaseOperation()
        handlePreviousException()
        val excelImportResult: ExcelImportResult = buildResult()
        afterExecute()
        destroyLocaleContext()
        return ResponseBodyBean.ofSuccess(excelImportResult)
    }


    private fun buildResult(): ExcelImportResult {
        val excelImportResult = ExcelImportResult()
        excelImportResult.messageList = returnMessageList.get()
        excelImportResult.excelFilePath = excelFilePath.get()
        return excelImportResult
    }

    private fun handlePreviousException() {
        if (exceptionOccurred.get() == true) {
            onExceptionOccurred()
            uploadWorkbook()
        }
    }

    private fun exeDatabaseOperation() {
        // if no error message (errorMessageList if empty) or not denyAll, then execute DB operation
        if (CollUtil.isEmpty(errorMessageList.get()) || !denyAll) {
            if (CollUtil.isNotEmpty(beanList.get())) {
                setReturnMessageList("Starting - Import data…")
                try {
                    executeDatabaseOperation(beanList.get()!!)
                    setReturnMessageList("Finished - Import data. Imported count: ${beanList.get()!!.size}")
                } catch (e: Exception) {
                    exceptionOccurred.set(true)
                    log.error("Exception occurred when executing DB operation!", e)
                    setErrorMessage("Exception occurred when executing DB operation! Exception message: ${e.message}")
                }
            } else {
                setReturnMessageList("Finished - Import data. Empty list. Imported count: ${beanList.get()!!.size}")
            }
        } else {
            setReturnMessageList("[Warning] Found not valid data. Data import all failed!")
        }
    }

    private fun bindData() {
        setReturnMessageList("Starting - Validate and bind data…")
        try {
            bindData(workbook.get()!!)
            setReturnMessageList("Finished - Validate and bind data")
        } catch (e: java.lang.Exception) {
            log.error("Exception occurred when validating and binding data!", e)
            setErrorMessage("Exception occurred when validating and binding data! Exception message: ${e.message}")
        }
    }

    private fun readMultipartFile(multipartFile: MultipartFile) {
        try {
            setReturnMessageList("Starting - Read Excel file…")
            workbook.set(readFile(multipartFile))
            setReturnMessageList("Finished - Read Excel file")
        } catch (e: IOException) {
            log.error("Exception occurred when reading Excel file!", e)
            setErrorMessage("Exception occurred when reading Excel file! Exception message: ${e.message}")
        }
    }


    /**
     * Sets field name array.
     *
     * @param fieldNameArray the field name array
     */
    fun setFieldNameArray(fieldNameArray: Array<String>) {
        this.fieldNameArray = fieldNameArray
    }

    /**
     * Read the file into workbook.
     *
     * @param multipartFile the multipart file
     * @return the workbook
     * @throws IOException the io exception
     */
    private fun readFile(multipartFile: MultipartFile): Workbook {
        return WorkbookUtil.createBook(multipartFile.inputStream)
    }

    private fun uploadWorkbook() {
        // FIXME: might cause OOM
        ByteArrayOutputStream(DataSize.ofBytes(512).toBytes().toInt()).use { it1 ->
            workbook.get()?.write(it1)
            BufferedInputStream(ByteArrayInputStream(it1.toByteArray())).use { it2 ->
                val filePath = ossUploader.upload(fileName.get()!!, it2)
                log.info("Uploaded excel with exception message. filePath: $filePath")
                excelFilePath.set(filePath)
            }
        }
    }

    /**
     * Bind data from the workbook. Processes below:
     *
     *  * Bind data to the instance of bindClass, according to the fieldNameArray.
     *  * Validate the field by callback method
     *
     * @param workbook the workbook
     */
    private fun bindData(workbook: Workbook) {
        for (index in 0 until workbook.numberOfSheets) {
            val excelReader = ExcelReader(workbook, index)
            excelReader.headerAlias = importingFieldAliasMap
            beanList.set(excelReader.readAll(beanClass))
            // The reason why validating bean by row is to generate error message behind the last column
            for (beanIndex in beanList.get()!!.indices) {
                validateBeanByRow(index, beanIndex)
            }
        }
    }

    private fun validateBeanByRow(sheetIndex: Int, beanIndex: Int) {
        try {
            validateBeforeAddToBeanList(beanList.get()!!, beanList.get()!![beanIndex], beanIndex)
        } catch (e: Exception) {
            log.error("bindRowToBean method has encountered a problem!", e)
            exceptionOccurred.set(true)
            val errorMessage =
                "Exception occurred when binding and validating the data of row number ${beanIndex + 1}. Exception message: ${e.message}"
            setErrorMessage(errorMessage)
            val errorRow = workbook.get()?.getSheetAt(sheetIndex)!!.getRow(beanIndex + 1)
            val errorInformationCell = errorRow.createCell(fieldNameArray.size)
            errorInformationCell.setCellValue(errorMessage)
        }
    }

    /**
     * Sets deny all. When exception occurred, deny all data or not
     *
     * @param denyAll true is deny all or vice versa
     */
    open fun setDenyAll(denyAll: Boolean) {
        this.denyAll = denyAll
    }

    /**
     * Sets error message.
     *
     * @param errorInfo the error info
     */
    protected open fun setErrorMessage(errorInfo: String) {
        errorMessageList.get()?.add(errorInfo)
        setReturnMessageList(errorInfo)
    }

    /**
     * Sets return message list.
     *
     * @param message the message
     */
    protected open fun setReturnMessageList(message: String) {
        returnMessageList.get()?.add(message)
    }

    /**
     * Close workbook.
     *
     * @param workbook the workbook
     */
    private fun closeWorkbook(workbook: Workbook?) {
        try {
            workbook?.close()
        } catch (e: IOException) {
            log.error("Exception occurred when closing workbook! Exception message: ${e.message}, workbook: $workbook")
        }
    }
}
