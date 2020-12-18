package com.jmsoftware.maf.common.constant;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * <h1>HttpStatus</h1>
 * <p>Hypertext Transfer Protocol (HTTP) Status Code</p>
 *
 * @author Johnny Miller (锺俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 16:48
 * @see <a href="https://www.iana.org/assignments/http-status-codes">HTTP Status Code Registry</a>
 * @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes">List of HTTP status codes</a>
 */
@Getter
@ToString
@SuppressWarnings("unused")
public enum HttpStatus implements IUniversalStatus {
    /*
    Value            Description                             Reference
     100   Continue                        [RFC7231, Section 6.2.1]
     101   Switching Protocols             [RFC7231, Section 6.2.2]
     102   Processing                      [RFC2518]
     103   Early Hints                     [RFC8297]
   104-199 Unassigned
     200   OK                              [RFC7231, Section 6.3.1]
     201   Created                         [RFC7231, Section 6.3.2]
     202   Accepted                        [RFC7231, Section 6.3.3]
     203   Non-Authoritative Information   [RFC7231, Section 6.3.4]
     204   No Content                      [RFC7231, Section 6.3.5]
     205   Reset Content                   [RFC7231, Section 6.3.6]
     206   Partial Content                 [RFC7233, Section 4.1]
     207   Multi-Status                    [RFC4918]
     208   Already Reported                [RFC5842]
   209-225 Unassigned
     226   IM Used                         [RFC3229]
   227-299 Unassigned
     300   Multiple Choices                [RFC7231, Section 6.4.1]
     301   Moved Permanently               [RFC7231, Section 6.4.2]
     302   Found                           [RFC7231, Section 6.4.3]
     303   See Other                       [RFC7231, Section 6.4.4]
     304   Not Modified                    [RFC7232, Section 4.1]
     305   Use Proxy                       [RFC7231, Section 6.4.5]
     306   (Unused)                        [RFC7231, Section 6.4.6]
     307   Temporary Redirect              [RFC7231, Section 6.4.7]
     308   Permanent Redirect              [RFC7538]
   309-399 Unassigned
     400   Bad Request                     [RFC7231, Section 6.5.1]
     401   Unauthorized                    [RFC7235, Section 3.1]
     402   Payment Required                [RFC7231, Section 6.5.2]
     403   Forbidden                       [RFC7231, Section 6.5.3]
     404   Not Found                       [RFC7231, Section 6.5.4]
     405   Method Not Allowed              [RFC7231, Section 6.5.5]
     406   Not Acceptable                  [RFC7231, Section 6.5.6]
     407   Proxy Authentication Required   [RFC7235, Section 3.2]
     408   Request Timeout                 [RFC7231, Section 6.5.7]
     409   Conflict                        [RFC7231, Section 6.5.8]
     410   Gone                            [RFC7231, Section 6.5.9]
     411   Length Required                 [RFC7231, Section 6.5.10]
     412   Precondition Failed             [RFC7232, Section 4.2][RFC8144, Section 3.2]
     413   Payload Too Large               [RFC7231, Section 6.5.11]
     414   URI Too Long                    [RFC7231, Section 6.5.12]
     415   Unsupported Media Type          [RFC7231, Section 6.5.13][RFC7694, Section 3]
     416   Range Not Satisfiable           [RFC7233, Section 4.4]
     417   Expectation Failed              [RFC7231, Section 6.5.14]
   418-420 Unassigned
     421   Misdirected Request             [RFC7540, Section 9.1.2]
     422   Unprocessable Entity            [RFC4918]
     423   Locked                          [RFC4918]
     424   Failed Dependency               [RFC4918]
     425   Too Early                       [RFC8470]
     426   Upgrade Required                [RFC7231, Section 6.5.15]
     427   Unassigned
     428   Precondition Required           [RFC6585]
     429   Too Many Requests               [RFC6585]
     430   Unassigned
     431   Request Header Fields Too Large [RFC6585]
   432-450 Unassigned
     451   Unavailable For Legal Reasons   [RFC7725]
   452-499 Unassigned
     500   Internal Server Error           [RFC7231, Section 6.6.1]
     501   Not Implemented                 [RFC7231, Section 6.6.2]
     502   Bad Gateway                     [RFC7231, Section 6.6.3]
     503   Service Unavailable             [RFC7231, Section 6.6.4]
     504   Gateway Timeout                 [RFC7231, Section 6.6.5]
     505   HTTP Version Not Supported      [RFC7231, Section 6.6.6]
     506   Variant Also Negotiates         [RFC2295]
     507   Insufficient Storage            [RFC4918]
     508   Loop Detected                   [RFC5842]
     509   Unassigned
     510   Not Extended                    [RFC2774]
     511   Network Authentication Required [RFC6585]
   512-599 Unassigned
     */

    // --- 2xx Success ---
    /**
     * OK
     */
    OK(200, "OK. The standard response for successful HTTP requests."),

    // --- 4xx Client Error ---
    /**
     * Bad request
     */
    BAD_REQUEST(400, "Bad request. The server cannot or will not process the request due to an apparent client error."),
    /**
     * Unauthorized
     */
    UNAUTHORIZED(401, "Unauthorized. The requester is not authorized to access the resource."),
    /**
     * Forbidden
     */
    FORBIDDEN(403, "Forbidden. The request was formatted correctly but the server is refusing to supply the requested" +
                   " resource."),
    /**
     * Not found
     */
    NOT_FOUND(404, "Not found. The resource could not be found."),
    /**
     * Method not allowed
     */
    METHOD_NOT_ALLOWED(405, "The resource was requested using a method that is not allowed."),
    /**
     * Not acceptable
     */
    NOT_ACCEPTABLE(406, "Not acceptable. The requested resource is capable of generating only content not acceptable " +
                        "according to the Accept headers sent in the request."),
    /**
     * Param not matched
     */
    PARAM_NOT_MATCH(460, "Param not matched. " +
                         "The request could not be fulfilled due to the incorrect syntax of the request."),
    /**
     * Param not null
     */
    PARAM_NOT_NULL(461, "Param not null."),
    /**
     * Invalid param
     */
    INVALID_PARAM(462, "Invalid param."),
    /**
     * User disabled
     */
    USER_DISABLED(463, "User disabled."),
    /**
     * Failure
     */
    FAILURE(464, "Failure. Business failure or operation failure."),
    /**
     * Warning
     */
    WARNING(465, "Warning. Operation may by possible danger or trouble."),

    // --- 5xx Server Error ---
    /**
     * Internal Server Error
     */
    ERROR(500, "Internal server error. A generic status for an error in the server itself."),
    /**
     * Not implemented
     */
    NOT_IMPLEMENTED(501, "Not implemented. The server either does not recognize the request method, or it lacks the " +
                         "ability to fulfil the request."),
    /**
     * Bad gateway
     */
    BAD_GATEWAY(502, "Bad gateway. The server was acting as a gateway or proxy and received an invalid response from " +
                     "the upstream server."),
    /**
     * Service unavailable
     */
    SERVICE_UNAVAILABLE(503, "Service unavailable. The server cannot handle the request (because it is overloaded or " +
                             "down for maintenance). Generally, this is a temporary state."),
    /**
     * Gateway timeout
     */
    GATEWAY_TIMEOUT(504, "Gateway timeout. The server was acting as a gateway or proxy and did not receive a timely " +
                         "response from the upstream server."),
    /**
     * Role not found
     */
    ROLE_NOT_FOUND(552, "Role not found."),
    /**
     * Username or password is not correct or account error
     */
    BAD_CREDENTIALS(553, "Username or password is not correct or account error."),
    /**
     * Token expired
     */
    TOKEN_EXPIRED(554, "Token expired."),
    /**
     * Token parse error
     */
    TOKEN_PARSE_ERROR(555, "Token parse error."),
    /**
     * Token out of control
     */
    TOKEN_OUT_OF_CONTROL(556, "Token out of control. Current user has logged in before. " +
                              "Please try to reset current password or sign in again."),
    /**
     * Kick out self 无法手动踢出自己，请尝试退出登录操作！
     */
    KICK_OUT_SELF(557, "Cannot kick self out. Please try to sign in again.");

    /**
     * Code of status
     */
    private final Integer code;
    /**
     * Message of status
     */
    private final String message;

    /**
     * Instantiates a new Http status.
     *
     * @param code    the code
     * @param message the message
     */
    HttpStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * From code http status.
     *
     * @param code the code
     * @return the http status
     */
    public static HttpStatus fromCode(Integer code) {
        HttpStatus[] httpStatuses = HttpStatus.values();
        for (HttpStatus httpStatus : httpStatuses) {
            if (httpStatus.getCode().equals(code)) {
                return httpStatus;
            }
        }
        throw new IllegalArgumentException(String.format("Cannot find HTTP status by code: %d!", code));
    }

    /**
     * Return the HTTP status series of this status code.
     *
     * @return the series
     * @see HttpStatus.Series
     */
    public Series series() {
        return Series.valueOf(this);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#INFORMATIONAL}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @return the boolean
     * @see #series() #series()
     */
    public boolean is1xxInformational() {
        return (series() == Series.INFORMATIONAL);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#SUCCESSFUL}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @return the boolean
     * @see #series() #series()
     */
    public boolean is2xxSuccessful() {
        return (series() == Series.SUCCESSFUL);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#REDIRECTION}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @return the boolean
     * @see #series() #series()
     */
    public boolean is3xxRedirection() {
        return (series() == Series.REDIRECTION);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @return the boolean
     * @see #series() #series()
     */
    public boolean is4xxClientError() {
        return (series() == Series.CLIENT_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @return the boolean
     * @see #series() #series()
     */
    public boolean is5xxServerError() {
        return (series() == Series.SERVER_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR} or
     * {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}. This is a shortcut for checking the value of
     * {@link #series()}.
     *
     * @return the boolean
     * @see #is4xxClientError() #is4xxClientError()
     * @see #is5xxServerError() #is5xxServerError()
     * @since 5.0
     */
    public boolean isError() {
        return (is4xxClientError() || is5xxServerError());
    }

    /**
     * Enumeration of HTTP status series.
     * <p>Retrievable via {@link org.springframework.http.HttpStatus#series()}.
     */
    public enum Series {
        /**
         * Informational
         */
        INFORMATIONAL(1),
        /**
         * Successful
         */
        SUCCESSFUL(2),
        /**
         * Redirection
         */
        REDIRECTION(3),
        /**
         * Client error
         */
        CLIENT_ERROR(4),
        /**
         * Server error
         */
        SERVER_ERROR(5);

        /**
         * The Value.
         */
        private final int value;

        /**
         * Instantiates a new Series.
         *
         * @param value the value
         */
        Series(int value) {
            this.value = value;
        }

        /**
         * Return the integer value of this status series. Ranges from 1 to 5.
         *
         * @return the int
         */
        public int value() {
            return this.value;
        }

        /**
         * Return the enum constant of this type with the corresponding series.
         *
         * @param status a standard HTTP status enum value
         * @return the enum constant of this type with the corresponding series
         * @throws IllegalArgumentException if this enum has no corresponding constant
         */
        public static Series valueOf(HttpStatus status) {
            return valueOf(status.code);
        }

        /**
         * Return the enum constant of this type with the corresponding series.
         *
         * @param statusCode the HTTP status code (potentially non-standard)
         * @return the enum constant of this type with the corresponding series
         * @throws IllegalArgumentException if this enum has no corresponding constant
         */
        public static Series valueOf(int statusCode) {
            Series series = resolve(statusCode);
            if (series == null) {
                throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
            }
            return series;
        }

        /**
         * Resolve the given status code to an {@code HttpStatus.Series}, if possible.
         *
         * @param statusCode the HTTP status code (potentially non-standard)
         * @return the corresponding {@code Series}, or {@code null} if not found
         * @since 5.1.3
         */
        @Nullable
        public static Series resolve(int statusCode) {
            int seriesCode = statusCode / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            return null;
        }
    }
}
