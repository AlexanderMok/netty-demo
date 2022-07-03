package com.example.demo.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;


/**
 * Add security headers to http header
 */
@Component
public class HeaderWriter {
    /*X-XSS-Protection*/
    public static final String X_XSS_PROTECTION = "X-XSS-Protection";
    public static final String X_XSS_PROTECTION_VALUE = "1; mode=block";
    /*Cache Control*/
    public static final String CACHE_CONTROL = HttpHeaders.CACHE_CONTROL;
    public static final String CACHE_CONTROL_VALUE = "no-cache, no-store, max-age=0, must-revalidate";
    public static final String PRAGMA = HttpHeaders.PRAGMA;
    public static final String PRAGMA_VALUE = "no-cache";
    public static final String EXPIRES = HttpHeaders.EXPIRES;
    public static final String EXPIRES_VALUE = "0";
    /*X-Frame-Options*/
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    public static final String X_FRAME_OPTIONS_VALUE = "SAMEORIGIN";
    /*X-Content-Options*/
    public static final String X_CONTENT_OPTIONS = "X-Content-Options";
    public static final String X_CONTENT_OPTIONS_VALUE = "nosniff";
    /*Strict-Transport-Security*/
    public static final String STRICT_TRANSPORT_SECURITY = "Strict-Transport-Security";
    public static final String STRICT_TRANSPORT_SECURITY_VALUE = "max-age=31536000; preload";



    public void writerSecurityHeaders(ServerHttpResponse response) {
        response.getHeaders().add(X_XSS_PROTECTION, X_XSS_PROTECTION_VALUE);
        response.getHeaders().add(CACHE_CONTROL, CACHE_CONTROL_VALUE);
        response.getHeaders().add(PRAGMA, PRAGMA_VALUE);
        response.getHeaders().add(EXPIRES, EXPIRES_VALUE);
        response.getHeaders().add(X_FRAME_OPTIONS, X_FRAME_OPTIONS_VALUE);
        response.getHeaders().add(X_CONTENT_OPTIONS, X_CONTENT_OPTIONS_VALUE);
        response.getHeaders().add(STRICT_TRANSPORT_SECURITY, STRICT_TRANSPORT_SECURITY_VALUE);
    }
}
