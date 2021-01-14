package com.jmsoftware.maf.authcenter.universal.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.jmsoftware.maf.springbootstarter.configuration.MafConfiguration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * <h1>DruidConfiguration</h1>
 * <p>
 * Druid Configuration
 * <a href="http://localhost:8080/springboottemplate/druid/index.html">Click me to view Druid Monitor</a>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-24 13:31
 **/
@Slf4j
@Configuration
@RequiredArgsConstructor
public class DruidConfiguration {
    private final MafConfiguration mafConfiguration;

    @Bean
    public ServletRegistrationBean<StatViewServlet> druidServlet() {
        val servletRegistrationBean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        servletRegistrationBean.addInitParameter("loginUsername", mafConfiguration.getSuperUserRole());
        servletRegistrationBean.addInitParameter("loginPassword", mafConfiguration.getSuperUserRole());
        servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean<WebStatFilter> filterRegistrationBean() {
        val filterRegistrationBean = new FilterRegistrationBean<WebStatFilter>();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        // Ignored resources
        filterRegistrationBean.addInitParameter("exclusions",
                                                "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        return filterRegistrationBean;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        log.warn("Initial data source");
        return new DruidDataSource();
    }
}
