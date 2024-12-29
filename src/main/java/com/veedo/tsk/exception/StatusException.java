package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class StatusException extends RuntimeException {

    public static final CustomError STATUS_NOT_FOUND = new CustomError("TSK-04-001", "Status tidak ada", "Status not found", HttpStatus.BAD_REQUEST);
    public static final CustomError STATUS_REQUIRED = new CustomError("TSK-04-002", "Status tidak boleh kosong", "Status cannot be empty", HttpStatus.BAD_REQUEST);

}
