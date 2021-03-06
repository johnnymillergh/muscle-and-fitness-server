package com.jmsoftware.maf.springcloudstarter.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <h1>RedirectController</h1>
 * <p>
 * HTTP Redirect Controller
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 1/21/20 1:18 PM
 **/
@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = {"Redirect Controller"})
public class RedirectController {
    @PostConstruct
    private void postConstruct() {
        log.info("URL redirect service initialized.");
    }

    @GetMapping("/home")
    @ApiOperation(value = "/home", notes = "Home page")
    public void handleHomeRequest(HttpServletResponse response) throws IOException {
        // Redirect to home page
        response.sendRedirect("static/home.html");
    }

    @GetMapping("/doc")
    @ApiOperation(value = "/doc", notes = "Swagger API Documentation")
    public void handleDocRequest(HttpServletResponse response) throws IOException {
        // Redirect to Bootstrap Swagger API documentation
        response.sendRedirect( "/doc.html?cache=1&lang=en");
    }

    @GetMapping("/webjars/bycdao-ui/images/api.ico")
    @ApiOperation(value = "/webjars/bycdao-ui/images/api.ico", notes = "Favicon redirection")
    public void handleFaviconRequest(HttpServletResponse response) throws IOException {
        // Redirect to a customized favicon
        response.sendRedirect("/static/icon/favicon.ico");
    }
}
