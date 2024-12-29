package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class PriorityException extends RuntimeException {

    public static final CustomError PRIORITY_NOT_FOUND = new CustomError("TSK-05-001", "Priority tidak ada", "Priority not found", HttpStatus.BAD_REQUEST);
    public static final CustomError PRIORITY_REQUIRED = new CustomError("TSK-05-002", "Priority tidak boleh kosong", "Priority cannot be empty", HttpStatus.BAD_REQUEST);

}
