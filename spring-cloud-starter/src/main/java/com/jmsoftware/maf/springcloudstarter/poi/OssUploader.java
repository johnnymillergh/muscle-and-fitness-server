package com.jmsoftware.maf.springcloudstarter.poi;

import java.io.IOException;
import java.io.InputStream;

/**
 * Description: OssUploader, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 9/15/2021 11:40 AM
 **/
@FunctionalInterface
public interface OssUploader {
    /**
     * Upload.
     *
     * @param name        the name
     * @param inputStream the input stream
     * @return the string
     * @throws IOException the io exception
     */
    String upload(String name, InputStream inputStream) throws IOException;
}
