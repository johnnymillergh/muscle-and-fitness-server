package com.jmsoftware.maf.springcloudstarter.elasticsearch;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

/**
 * Description: Book, change description here.
 *
 * @author Johnny Miller (鍾俊), e-mail: johnnysviva@outlook.com, date: 3/23/22 8:01 AM
 */
@Data
@Document(indexName = "books")
public class Book {
    /**
     * The Id.
     */
    @Id
    private String id;
    /**
     * The Title.
     */
    @Field(type = FieldType.Text, name = "name")
    private String name;
    /**
     * The Summary.
     */
    @Field(type = FieldType.Text)
    private String summary;
    /**
     * The Publishing date.
     */
    @Field(type = FieldType.Date, name = "publishDate", format = {}, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime publishingDate;
    /**
     * The Price.
     */
    @Field(type = FieldType.Double, name = "price")
    private double price;
}
