package com.jmsoftware.maf.springcloudstarter.elasticsearch

import com.jmsoftware.maf.common.util.logger
import org.elasticsearch.client.RestHighLevelClient
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import javax.annotation.PostConstruct

/**
 * # ElasticsearchConfiguration
 *
 * Description: ElasticsearchConfiguration, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:27 PM
 * @see
 * <a href='https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/'>Spring Data Elasticsearch - Reference Documentation</a>
 * @see
 * <a href='https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.clients.logging'>Client Logging</a>
 */
@EnableElasticsearchRepositories
@ConditionalOnClass(AbstractElasticsearchConfiguration::class, RestHighLevelClient::class)
class ElasticsearchConfiguration(
    private val elasticsearchProperties: ElasticsearchProperties
) {
    companion object {
        private val log = logger()
    }

    @PostConstruct
    fun init() {
        log.warn("ElasticsearchConfiguration init. URIs: ${elasticsearchProperties.uris}")
    }
}
