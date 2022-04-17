package com.jmsoftware.maf.springcloudstarter.controller

import com.jmsoftware.maf.common.util.logger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletResponse

/**
 * # RedirectController
 *
 * HTTP Redirect Controller
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 11:22 AM
 */
@RestController
class RedirectController {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    private fun postConstruct() {
        log.info("URL redirect service initialized.")
    }

    @GetMapping("/home")
    fun handleHomeRequest(response: HttpServletResponse) {
        // Redirect to home page
        response.sendRedirect("static/home/index.html")
    }

    @GetMapping("/doc")
    fun handleDocRequest(response: HttpServletResponse) {
        // Redirect to Bootstrap Swagger API documentation
        response.sendRedirect("/doc.html?cache=1&lang=en")
    }

    @GetMapping("/webjars/bycdao-ui/images/api.ico")
    fun handleFaviconRequest(response: HttpServletResponse) {
        // Redirect to a customized favicon
        response.sendRedirect("/static/asset/favicon.ico")
    }
}
