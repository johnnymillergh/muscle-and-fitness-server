package com.jmsoftware.maf.springcloudstarter.elasticsearch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import javax.annotation.PostConstruct;

/**
 * Description: ElasticsearchConfiguration, change description here.
 *
 * @author 钟俊 (za-zhongjun), email: jun.zhong@zatech.com, date: 2/19/2022 11:38 PM
 * @see
 * <a href='https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/'>Spring Data Elasticsearch - Reference Documentation</a>
 * @see
 * <a href='https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#elasticsearch.clients.logging'>Client Logging</a>
 **/
@Slf4j
@RequiredArgsConstructor
@EnableElasticsearchRepositories
@ConditionalOnClass({AbstractElasticsearchConfiguration.class, RestHighLevelClient.class})
public class ElasticsearchConfiguration {
    private final ElasticsearchProperties elasticsearchProperties;

    @PostConstruct
    public void init() {
        log.warn("ElasticsearchConfiguration init. URIs: {}", elasticsearchProperties.getUris());
    }
}
