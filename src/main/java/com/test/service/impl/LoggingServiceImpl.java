package com.test.service.impl;

import com.test.dto.RestApiContext;
import com.test.service.LoggingService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoggingServiceImpl implements LoggingService {

  @SneakyThrows
  @Override
  @Async
  public void log(RestApiContext restApiContext) {
    //    Thread.sleep(10000);
    log.info("api context: {}", restApiContext);
  }

}
