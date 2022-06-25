package com.jmsoftware.maf.springcloudstarter.eventbus

import com.google.common.eventbus.Subscribe
import com.jmsoftware.maf.common.util.logger
import org.springframework.beans.factory.config.BeanDefinitionHolder
import org.springframework.beans.factory.support.AbstractBeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.EnvironmentAware
import org.springframework.context.ResourceLoaderAware
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.annotation.AnnotationAttributes
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.StandardAnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter

/**
 * # EventSubscriberRegistrar
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/22/22 10:44 PM
 * @see Subscribe
 * @see <a href='https://stackoverflow.com/q/50808941/9728243'>How to get basePackages of @ComponentScan programatically at runtime?</a>
 **/
class EventSubscriberRegistrar : ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {
    companion object {
        private val log = logger()

        private fun getBasePackages(
            metadata: StandardAnnotationMetadata,
            attributes: AnnotationAttributes
        ): Array<String> {
            // keep the original order of the attributes in the user of LinkedHashSet, and remove the duplicates
            return LinkedHashSet(attributes.getStringArray("basePackages").toList()).ifEmpty {
                // If value attribute is not set, fallback to the package of the annotated class
                val basePackage = metadata.introspectedClass.getPackage().name
                log.warn("Returning the package of the underlying class: ${metadata.introspectedClass.name}, basePackage: $basePackage")
                setOf(basePackage)
            }.toTypedArray()
        }
    }

    private lateinit var environment: Environment
    private lateinit var resourceLoader: ResourceLoader

    override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {
        EventSubscriberBeanDefinitionScanner(registry, environment, resourceLoader)
            .apply {
                log.warn("Searching event subscribers annotated with @${EventSubscriber::class.java.simpleName}")
            }
            .scan(
                *getBasePackages(
                    importingClassMetadata as StandardAnnotationMetadata,
                    AnnotationAttributes(
                        importingClassMetadata.getAnnotationAttributes(EnableEventBus::class.java.canonicalName)!!
                    )
                )
            ).apply {
                log.warn("Number of beans registered for @${EventSubscriber::class.simpleName}: $this")
            }
    }

    override fun setEnvironment(environment: Environment) {
        this.environment = environment
    }

    override fun setResourceLoader(resourceLoader: ResourceLoader) {
        this.resourceLoader = resourceLoader
    }
}

/**
 * # EventSubscriberBeanDefinitionScanner
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com, 6/22/22 10:51 PM
 **/
private class EventSubscriberBeanDefinitionScanner(
    registry: BeanDefinitionRegistry,
    environment: Environment,
    resourceLoader: ResourceLoader
) : ClassPathBeanDefinitionScanner(registry, false, environment, resourceLoader) {
    companion object {
        private val log = logger()
    }

    override fun doScan(vararg basePackages: String): MutableSet<BeanDefinitionHolder> {
        addIncludeFilter(AnnotationTypeFilter(EventSubscriber::class.java))
        return super.doScan(*basePackages)
    }

    override fun postProcessBeanDefinition(beanDefinition: AbstractBeanDefinition, beanName: String) {
        log.info("Post process bean definition: `$beanName`")
        super.postProcessBeanDefinition(beanDefinition, beanName)
    }
}
