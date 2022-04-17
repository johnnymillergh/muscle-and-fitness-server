package com.jmsoftware.maf.common.domain

/**
 * # DeletedField
 *
 * Description: DeletedField, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/16/22 8:17 PM
 */
enum class DeletedField(
    /**
     * The Value.
     */
    val value: String,
    /**
     * The Description.
     */
    val description: String
) {
    /**
     * Not deleted
     */
    NOT_DELETED("N", "not deleted"),

    /**
     * Deleted
     */
    DELETED("Y", "deleted");
}
