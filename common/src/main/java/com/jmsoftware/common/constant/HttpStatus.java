package com.jmsoftware.common.constant;

import lombok.Getter;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * <h1>HttpStatus</h1>
 * <p>Hypertext Transfer Protocol (HTTP) Status Code</p>
 *
 * @author Johnny Miller (鍾俊), email: johnnysviva@outlook.com
 * @date 2019-03-23 16:48
 * @see <a href="https://www.iana.org/assignments/http-status-codes">HTTP Status Code Registry</a>
 * @see <a href="https://en.wikipedia.org/wiki/List_of_HTTP_status_codes">List of HTTP status codes</a>
 **/
@Getter
@ToString
@SuppressWarnings("unused")
public enum HttpStatus implements IUniversalStatus {
    // 2xx Success
    /**
     * Success
     */
    OK(200, "OK. The standard response for successful HTTP requests."),

    // --- 4xx Client Error ---
    /**
     * Unauthorized
     */
    UNAUTHORIZED(401, "Unauthorized. The requester is not authorized to access the resource."),
    /**
     * Forbidden
     */
    FORBIDDEN(403, "Forbidden. " +
                   "The request was formatted correctly but the server is refusing to supply the requested resource."),
    /**
     * Not found
     */
    NOT_FOUND(404, "Not found. The resource could not be found."),
    /**
     * Method not allowed
     */
    METHOD_NOT_ALLOWED(405, "The resource was requested using a method that is not allowed."),
    /**
     * Bad request
     */
    BAD_REQUEST(400, "Bad request."),
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
     * Error or failure
     */
    ERROR(500, "Error. A generic status for an error in the server itself."),
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

    HttpStatus(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

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
     * @see HttpStatus.Series
     */
    public Series series() {
        return Series.valueOf(this);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#INFORMATIONAL}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @see #series()
     */
    public boolean is1xxInformational() {
        return (series() == Series.INFORMATIONAL);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#SUCCESSFUL}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @see #series()
     */
    public boolean is2xxSuccessful() {
        return (series() == Series.SUCCESSFUL);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#REDIRECTION}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @see #series()
     */
    public boolean is3xxRedirection() {
        return (series() == Series.REDIRECTION);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @see #series()
     */
    public boolean is4xxClientError() {
        return (series() == Series.CLIENT_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}.
     * This is a shortcut for checking the value of {@link #series()}.
     *
     * @see #series()
     */
    public boolean is5xxServerError() {
        return (series() == Series.SERVER_ERROR);
    }

    /**
     * Whether this status code is in the HTTP series {@link org.springframework.http.HttpStatus.Series#CLIENT_ERROR} or
     * {@link org.springframework.http.HttpStatus.Series#SERVER_ERROR}. This is a shortcut for checking the value of
     * {@link #series()}.
     *
     * @see #is4xxClientError()
     * @see #is5xxServerError()
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

        private final int value;

        Series(int value) {
            this.value = value;
        }

        /**
         * Return the integer value of this status series. Ranges from 1 to 5.
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
