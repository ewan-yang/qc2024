package com.tecpie.platform.common.exception;

import com.tecpie.library.common.business.controller.resource.Result;
import com.tecpie.library.common.exception.SystemError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class SqlExceptionHandler {

    @ExceptionHandler(value = BadSqlGrammarException.class)
    public ResponseEntity handleBadSqlGrammarException(BadSqlGrammarException e) {
        return new ResponseEntity<>(Result.error(SystemError.PARAM_NOT_CHECKED_ERROR, e.getCause().getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    public ResponseEntity handleIllegalStateException(IllegalStateException e) {
        return new ResponseEntity<>(Result.error(SystemError.UNKNOWN_SYSTEM_ERROR, e.getCause().getMessage()), HttpStatus.BAD_REQUEST);
    }

}
