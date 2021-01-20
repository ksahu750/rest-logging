package com.test.service.impl;

import com.test.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService {

  @Override
  public void log(HttpServletRequest request, HttpServletResponse response) {
    // Request Details
    final String fullRequestUrl = getFullUrl(request);
    final String requestBody = getRequestBody(request);
    final String method = request.getMethod();

    // Response Details
    final String responseBody = getResponseBody(response);
    final int responseStatus = response.getStatus();

    log.info("method: {}, URL: {}, requestBody: {}, status: {}, responseBody: {}", method,
        fullRequestUrl, requestBody, responseStatus, responseBody);
  }

  private String getRequestBody(HttpServletRequest request) {
    ContentCachingRequestWrapper wrapper =
        WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      if (buf.length > 0) {
        int length = buf.length;
        String characterEncoding = wrapper.getCharacterEncoding();
        try {
          return new String(buf, 0, length, characterEncoding);
        } catch (UnsupportedEncodingException ex) {
          return "[unknown]";
        }
      }
    }
    return "";
  }

  private String getResponseBody(HttpServletResponse response) {
    ContentCachingResponseWrapper wrapper =
        WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (wrapper != null) {
      byte[] buf = wrapper.getContentAsByteArray();
      String characterEncoding = wrapper.getCharacterEncoding();
      if (buf.length > 0) {
        int length = buf.length;
        try {
          return new String(buf, 0, length, characterEncoding);
        } catch (UnsupportedEncodingException ex) {
          return "[unknown]";
        }
      }
    }
    return "";
  }

  private static String getFullUrl(HttpServletRequest request) {
    final StringBuilder urlBuilder = new StringBuilder(request.getRequestURL().toString());
    final String queryString = request.getQueryString();
    if (Objects.nonNull(queryString)) {
      urlBuilder.append("?").append(queryString);
    }
    return urlBuilder.toString();
  }

}
