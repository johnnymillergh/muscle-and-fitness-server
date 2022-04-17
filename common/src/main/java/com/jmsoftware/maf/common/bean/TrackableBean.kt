package com.jmsoftware.maf.common.bean

/**
 * # TrackableBean
 *
 * Change description here.
 *
 * @author Johnny Miller (锺俊), e-mail: johnnysviva@outlook.com, date: 4/12/22 9:50 PM
 */
open class TrackableBean {
    /**
     * The Track. Pattern: [Zipkin trace id]#[Span id]
     */
    var track: String? = null
}
