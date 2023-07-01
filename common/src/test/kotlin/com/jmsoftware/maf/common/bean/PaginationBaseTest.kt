package com.jmsoftware.maf.common.bean

import com.jmsoftware.maf.common.util.Slf4j
import com.jmsoftware.maf.common.util.Slf4j.Companion.log
import jakarta.validation.Validation
import jakarta.validation.Validator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * # PaginationBaseTest
 *
 * Description: PaginationBaseTest, change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 6/23/2023 7:35 PM
 */
@Slf4j
class PaginationBaseTest {
    private val validator: Validator = Validation.buildDefaultValidatorFactory().validator

    @Test
    fun testPaginationBase_whenAllFieldsAreValid_thenViolationsAreEmpty() {
        val pagination = PaginationBase().apply {
            currentPage = 1
            pageSize = 100
        }
        val constraintViolations = validator.validate(pagination)
        assertTrue(constraintViolations.isEmpty())
        log.info("Constraint violations: $constraintViolations")
    }

    @Test
    fun testPaginationBase_whenFieldsAreInvalid_thenViolationsAreNotEmpty() {
        val pagination = PaginationBase().apply {
            currentPage = 0
            pageSize = 0
            orderRule = "INVALID ORDER RULE"
        }
        assertNull(pagination.orderBy)
        val constraintViolations = validator.validate(pagination)
        assertTrue(constraintViolations.isNotEmpty())
        val invalidFields = setOf(
            PaginationBase::currentPage.name,
            PaginationBase::pageSize.name,
            PaginationBase::orderRule.name
        )
        assertEquals(invalidFields.size, constraintViolations.size)
        constraintViolations.forEach {
            assertTrue(invalidFields.contains(it.propertyPath.toString()))
        }
    }
}
