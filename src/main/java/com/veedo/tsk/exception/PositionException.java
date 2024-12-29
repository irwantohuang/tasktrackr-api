package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class PositionException extends RuntimeException {
    public static final CustomError POSITION_NOT_VALID = new CustomError("TSK-03-001", "Posisi kerja tidak valid", "Job position not valid", HttpStatus.BAD_REQUEST);
    public static final CustomError POSITION_EXISTING = new CustomError("TSK-03-002", "Posisi kerja sudah ada", "Job position existing", HttpStatus.BAD_REQUEST);
    public static final CustomError POSITION_NOT_FOUND = new CustomError("TSK-03-003", "Posisi kerja tidak ditemukan", "Job position not found", HttpStatus.BAD_REQUEST);


}
