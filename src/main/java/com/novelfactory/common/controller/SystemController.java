package com.novelfactory.common.controller;

import com.novelfactory.common.api.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/system")
public class SystemController {

  @GetMapping("/ping")
  public ApiResponse<String> ping() {
    return ApiResponse.ok("pong");
  }
}
