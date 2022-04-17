package com.jmsoftware.maf.springcloudstarter.elasticsearch

import org.springframework.data.annotation.Id
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType
import java.time.LocalDateTime

/**
 * # Book
 *
 * Description: Book, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/14/22 12:22 PM
 */
@Document(indexName = Book.INDEX_NAME)
class Book {
    companion object {
        const val INDEX_NAME = "books"
    }

    /**
     * The ID.
     */
    @Id
    var id: String? = null

    /**
     * The Title.
     */
    @Field(type = FieldType.Text, name = "name")
    var name: String? = null

    /**
     * The Summary.
     */
    @Field(type = FieldType.Text)
    var summary: String? = null

    /**
     * The Publishing date.
     */
    @Field(type = FieldType.Date, name = "publishDate", format = [], pattern = ["yyyy-MM-dd'T'HH:mm:ss'Z'"])
    var publishingDate: LocalDateTime? = null

    /**
     * The Price.
     */
    @Field(type = FieldType.Double, name = "price")
    var price = 0.0
}
