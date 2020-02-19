package com.jmsoftware.common.constant;

/**
 * <h1>IUniversalStatus</h1>
 * The interface of Universal Status code
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 16:48
 * @see <a href="https://www.iana.org/assignments/http-status-codes">HTTP Status Code Registry</a>
 * @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes">List of HTTP status codes</a>
 **/
public interface IUniversalStatus {
    /**
     * Get status code.
     *
     * @return Status code
     */
    Integer getCode();

    /**
     * Get message of status.
     *
     * @return message of status
     */
    String getMessage();
}
