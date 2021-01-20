package com.test.interceptor;

import com.test.annotation.Auditable;
import com.test.service.LoggingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CustomInterceptor implements HandlerInterceptor {

  @Autowired
  private LoggingService loggingService;

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      return;
    }
    final boolean isApiAuditable =
        ((HandlerMethod) handler).getMethod().isAnnotationPresent(Auditable.class);
    if (isApiAuditable) {
      loggingService.log(request, response);
    }
  }

}
