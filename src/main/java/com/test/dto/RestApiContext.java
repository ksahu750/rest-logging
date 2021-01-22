package com.test.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(doNotUseGetters = true)
@Slf4j
public class RestApiContext {

  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final MimeType JSON_MIME_TYPE = MimeTypeUtils.APPLICATION_JSON;

  // Request Details
  private String url;
  private MimeType requestBodyMimeType;
  private String requestBody;
  private String method;

  // Response Details
  private Integer status;
  private MimeType responseBodyMimeType;
  private String responseBody;

  public RestApiContext(HttpServletRequest request, HttpServletResponse response) {
    // Request Details
    this.url = getFullUrl(request);
    this.requestBodyMimeType = getMimeType(request.getContentType());
    if (isJsonContent(this.requestBodyMimeType)) {
      this.requestBody = getRequestBody(request);
    }
    this.method = request.getMethod();

    // Response Details
    this.status = response.getStatus();
    this.responseBodyMimeType = getMimeType(response.getContentType());
    if (isJsonContent(this.responseBodyMimeType)) {
      this.responseBody = getResponseBody(response);
    }
  }

  private static boolean isJsonContent(MimeType mimeType) {
    return JSON_MIME_TYPE.includes(mimeType);
  }

  private static MimeType getMimeType(String contentType) {
    return Objects.nonNull(contentType) ? MimeType.valueOf(contentType) : null;
  }

  public static JsonNode getJson(String jsonString) {
    if (Objects.isNull(jsonString)) {
      return null;
    }
    try {
      return MAPPER.readTree(jsonString);
    } catch (IOException e) {
      log.error("Unable to parse: {} to json", jsonString);
      return null;
    }
  }

  private static String getRequestBody(HttpServletRequest request) {
    ContentCachingRequestWrapper wrapper =
        WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (wrapper == null) {
      return "";
    }
    return getContentFromByteBuffer(wrapper.getContentAsByteArray(),
        wrapper.getCharacterEncoding());
  }

  private static String getResponseBody(HttpServletResponse response) {
    ContentCachingResponseWrapper wrapper =
        WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (wrapper == null) {
      return "";
    }
    return getContentFromByteBuffer(wrapper.getContentAsByteArray(),
        wrapper.getCharacterEncoding());
  }

  private static String getContentFromByteBuffer(byte[] buf, String characterEncoding) {
    int length = buf.length;
    if (length > 0) {
      try {
        return new String(buf, 0, length, characterEncoding);
      } catch (UnsupportedEncodingException ex) {
        return "[unknown]";
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
