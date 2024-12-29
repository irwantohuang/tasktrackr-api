package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class ProjectException extends RuntimeException{

    public static final CustomError PROJECT_NOT_FOUND = new CustomError("TSK-06-001", "Project tidak ada", "Project not found", HttpStatus.BAD_REQUEST);
    public static final CustomError NOT_PROJECT_MANAGER = new CustomError("TSK-06-002", "Project ini bukan milik anda", "This project is not yours", HttpStatus.UNAUTHORIZED);
    public static final CustomError DATE_REQUIRED = new CustomError("TSK-06-003", "Tanggal tidak boleh kosong", "Date cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError DATE_FORMAT = new CustomError("TSK-06-004", "Format tanggal harus dd/MM/yyyy", "Date format must be dd/MM/yyyy", HttpStatus.BAD_REQUEST);
    public static final CustomError INVALID_DATE = new CustomError("TSK-06-005", "Tanggal berakhir tidak boleh lebih kecil dari tanggal mulai project.", "End date cannot be less than project start date.", HttpStatus.BAD_REQUEST);
    public static final CustomError PM_NOT_FOUND = new CustomError("TSK-06-006", "Project manager tidak ada", "Project manager not found", HttpStatus.BAD_REQUEST);
    public static final CustomError PROJECT_NAME_REQUIRED = new CustomError("TSK-06-007", "Nama project tidak boleh kosong", "Project name cannot be empty", HttpStatus.BAD_REQUEST);


}
