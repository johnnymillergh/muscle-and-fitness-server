package com.jmsoftware.maf.springcloudstarter.elasticsearch

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

/**
 * # BookRepository
 *
 * Description: BookRepository, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:22 PM
 */
interface BookRepository : ElasticsearchRepository<Book, String> {
    /**
     * Find by name and price list.
     *
     * @param name  the name
     * @param price the price
     * @return the list
     */
    fun findByNameAndPrice(name: String, price: Int): List<Book>

    /**
     * Find by name page.
     *
     * @param name     the name
     * @param pageable the pageable
     * @return the page
     */
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    fun findByName(name: String, pageable: Pageable): Page<Book?>
}
