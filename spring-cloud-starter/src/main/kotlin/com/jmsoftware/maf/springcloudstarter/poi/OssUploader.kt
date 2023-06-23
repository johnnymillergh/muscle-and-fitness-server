package com.jmsoftware.maf.springcloudstarter.poi

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.io.IOException
import java.io.InputStream

/**
 * # OssUploader
 *
 * Description: OssUploader, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 2:51 PM
 */
fun interface OssUploader {
    /**
     * Upload.
     *
     * @param name        the name
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    fun upload(@NotBlank name: String, @NotNull inputStream: InputStream): String
}
