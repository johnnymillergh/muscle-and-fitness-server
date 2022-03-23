package com.jmsoftware.maf.springcloudstarter.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * Description: BookRepository, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 3/23/22 8:01 AM
 **/
public interface BookRepository extends ElasticsearchRepository<Book, String> {
    /**
     * Find by name and price list.
     *
     * @param name  the name
     * @param price the price
     * @return the list
     */
    List<Book> findByNameAndPrice(String name, Integer price);

    /**
     * Find by name page.
     *
     * @param name     the name
     * @param pageable the pageable
     * @return the page
     */
    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Page<Book> findByName(String name, Pageable pageable);
}
