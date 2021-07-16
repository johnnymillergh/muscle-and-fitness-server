package com.jmsoftware.maf.springcloudstarter.controller;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description: ControllerBase, change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, date: 6/27/2021 9:20 PM
 **/
public class ControllerBase {
//    @InitBinder
//    public void initBinder(WebDataBinder binder) {
//        final SimpleDateFormat dateFormat = new SimpleDateFormat(DateTimeUniversalFormat.DATE_TIME_PATTERN);
//        dateFormat.setLenient(false);
//        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
//    }
}
