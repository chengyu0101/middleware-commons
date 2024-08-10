
package com.yujian.middleware.commons.support;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author cy
 * @Date 2021/7/28 3:55 PM
 */
public class DefaultCorsFilter implements Filter {

    private static final int DEFAULT_MAX_AGE = 86400; // 指定本次预检请求的有效期，单位为秒,默认先写个1天

    private int accessControlMaxAge = DEFAULT_MAX_AGE;

    @Override
    public void init(FilterConfig filterConfig) {
        String maxAgeString = filterConfig.getInitParameter("ACCESS_CONTROL_MAX_AGE");
        if (maxAgeString != null) {
            try {
                int maxAge = Integer.parseInt(maxAgeString);
                if (maxAge >= 0) {
                    accessControlMaxAge = maxAge;
                }
            } catch (Exception ignore) {
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (isCorsRequest((HttpServletRequest) request) && allowCors((HttpServletRequest) request)) {
            HttpServletRequest servletRequest = (HttpServletRequest) request;
            HttpServletResponse servletResponse = (HttpServletResponse) response;
            // 必填
            servletResponse.setHeader("Access-Control-Allow-Origin", servletRequest.getHeader("Origin"));
            // 可选
            servletResponse.setHeader("Access-Control-Allow-Methods", servletRequest.getHeader("Access-Control-Request-Method"));
            // 可选
            servletResponse.setHeader("Access-Control-Allow-Headers", servletRequest.getHeader("Access-Control-Request-Headers"));
            // 可选
            servletResponse.setHeader("Access-Control-Allow-Credentials", "true");
            // 可选，指定本次预检请求的有效期，
            servletResponse.setHeader("Access-Control-Max-Age", String.valueOf(accessControlMaxAge));
        }
        if (isPreFlightRequest(request)) {
            return;
        } else {
            chain.doFilter(request, response);
        }
    }

    public static boolean isCorsRequest(HttpServletRequest request) {
        return (request.getHeader("Origin") != null);
    }

    /**
     * Returns {@code true} if the request is a valid CORS pre-flight one.
     */
    public static boolean isPreFlightRequest(ServletRequest servletRequest) {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        return isCorsRequest(request) && "OPTIONS".equalsIgnoreCase(request.getMethod()) && (request.getHeader("Access-Control-Request-Method") != null);
    }

    protected boolean allowCors(HttpServletRequest request) {
        return true;
    }

    @Override
    public void destroy() {
    }
}