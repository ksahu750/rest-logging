package com.test.controller;

import com.test.annotation.Auditable;
import com.test.dto.DTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {

  @GetMapping(path = "/test/{path}")
  @Auditable
  public ResponseEntity<DTO> test(@PathVariable("path") Integer path,
      @RequestParam(name = "param", required = false) Integer param,
      @RequestParam(name = "param2", required = false) Integer param2) {
    log.info("param: {}, param2: {}", param, param2);
    return new ResponseEntity<>(new DTO(1), HttpStatus.OK);
  }

  @PostMapping(path = "/test2")
  @Auditable
  public ResponseEntity<DTO> test(@RequestBody DTO body) {
    log.info("body: {}", body);
    throw new RuntimeException("");
    //    return new ResponseEntity<>(new DTO(1), HttpStatus.OK);
  }

  @PostMapping(path = "/test3")
  @Auditable
  public ResponseEntity<DTO> test(@RequestParam MultipartFile file) {
    log.info("body: {}", file);
    return new ResponseEntity<>(new DTO(1), HttpStatus.OK);
  }

}
