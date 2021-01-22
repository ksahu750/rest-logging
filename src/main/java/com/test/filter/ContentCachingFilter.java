package com.test.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(1)
public class ContentCachingFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
    ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
    filterChain.doFilter(wrappedRequest, wrappedResponse);
    wrappedResponse.copyBodyToResponse();
  }

}
