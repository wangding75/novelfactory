package com.novelfactory.common.exception;

import com.novelfactory.common.api.ErrorCode;

public class BusinessException extends IllegalArgumentException {
  private final ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }
}
