package com.novelfactory.common.exception;

import com.novelfactory.common.api.ErrorCode;

public class NotFoundException extends BusinessException {

  public NotFoundException(ErrorCode errorCode, String message) {
    super(errorCode, message);
  }
}
