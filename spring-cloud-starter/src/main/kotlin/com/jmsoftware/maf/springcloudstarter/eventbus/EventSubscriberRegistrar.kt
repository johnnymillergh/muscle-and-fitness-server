package com.jmsoftware.maf.springcloudstarter.eventbus

import com.google.common.eventbus.Subscribe
import com.jmsoftware.maf.common.util.logger
import org.springframework.beans.factory.config.BeanDefinitionHolder
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.ResourceLoaderAware
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.io.ResourceLoader
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter

/**
 * # EventSubscriberRegistrar
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/22/22 10:44 PM
 * @see Subscribe
 **/
class EventSubscriberRegistrar : ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    companion object {
        private val log = logger()
    }

    private lateinit var resourceLoaderMember: ResourceLoader

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        EventSubscriberBeanDefinitionScanner(registry).apply {
            this.resourceLoader = resourceLoaderMember
        }.scan("com.jmsoftware.maf").apply {
            log.warn("Number of beans registered for @${EventSubscriber::class.simpleName}: $this")
        }
    }

    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        resourceLoaderMember = resourceLoader
    }
}

/**
 * # EventSubscriberBeanDefinitionScanner
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/22/22 10:51 PM
 **/
private class EventSubscriberBeanDefinitionScanner(registry: BeanDefinitionRegistry) :
    ClassPathBeanDefinitionScanner(registry, false) {
    companion object {
        private val log = logger()
    }

    override fun doScan(vararg basePackages: String?): MutableSet<BeanDefinitionHolder> {
        addIncludeFilter(AnnotationTypeFilter(EventSubscriber::class.java))
        return super.doScan(*basePackages)
    }

    override fun postProcessBeanDefinition(beanDefinition: AbstractBeanDefinition, beanName: String) {
        log.info("Post process bean definition: $beanName")
        super.postProcessBeanDefinition(beanDefinition, beanName)
    }
}
