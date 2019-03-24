package com.musala.gateways_test.web.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Util class to generate Headers for REST Response
 */
@Component
public class HeaderUtil {

    /**
     * Create a HttpHeaders with {@link HEADERS} and action message
     *
     * @param header
     * @param message
     * @return
     */
    public HttpHeaders header(HEADERS header, String message) {
        return createHeader(header, message);
    }

    /**
     * Create a SUCCESS {@link HEADERS} HttpHeaders with  and action message
     *
     * @param message
     * @return
     */
    public HttpHeaders successHeader(String message) {
        return createHeader(HEADERS.SUCCESS, message);
    }

    /**
     * Create a SUCCESS {@link HEADERS} HttpHeaders with {@link HttpStatus} status
     *
     * @param httpStatus
     * @return
     */
    public HttpHeaders successHeader(HttpStatus httpStatus) {
        return createHeader(HEADERS.SUCCESS, httpStatus.getReasonPhrase());
    }

    /**
     * Create a INFO {@link HEADERS} HttpHeaders with  and action message
     *
     * @param message
     * @return
     */
    public HttpHeaders infoHeader(String message) {
        return createHeader(HEADERS.INFO, message);
    }

    /**
     * Create a INFO {@link HEADERS} HttpHeaders with {@link HttpStatus} status
     *
     * @param httpStatus
     * @return
     */
    public HttpHeaders infoHeader(HttpStatus httpStatus) {
        return createHeader(HEADERS.INFO, httpStatus.getReasonPhrase());
    }

    /**
     * Create a ERROR {@link HEADERS} HttpHeaders with  and action message
     *
     * @param message
     * @return
     */
    public HttpHeaders errorHeader(String message) {
        return createHeader(HEADERS.ERROR, message);
    }

    /**
     * Create a ERROR {@link HEADERS} HttpHeaders with {@link HttpStatus} status
     *
     * @param httpStatus
     * @return
     */
    public HttpHeaders errorHeader(HttpStatus httpStatus) {
        return createHeader(HEADERS.ERROR, httpStatus.getReasonPhrase());
    }

    /**
     * Create a {@link HttpHeaders} with {@link HEADERS} and message
     *
     * @param header
     * @param message
     * @return
     */
    private HttpHeaders createHeader(HEADERS header, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(header.getHeader(), message);
        return headers;
    }

    /**
     * Enum Headers allowed
     */
    public enum HEADERS {
        SUCCESS("success"),
        INFO("info"),
        ERROR("error");

        private String header;

        HEADERS(String header) {
            this.header = header;
        }

        public String getHeader() {
            return header;
        }
    }
}
