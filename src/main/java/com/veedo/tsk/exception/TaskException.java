package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class TaskException extends RuntimeException{

    public static final CustomError TASK_NOT_FOUND = new CustomError("TSK-07-001", "Task tidak ada", "Task not found", HttpStatus.BAD_REQUEST);
    public static final CustomError TASK_NAME_REQUIRED = new CustomError("TSK-07-001", "Nama task tidak boleh kosong", "Task name cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError DATE_REQUIRED = new CustomError("TSK-07-001", "Tanggal tidak boleh kosong", "Date cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError DATE_FORMAT = new CustomError("TSK-07-001", "Format tanggal harus dd/MM/yyyy", "Date format must be dd/MM/yyyy", HttpStatus.BAD_REQUEST);
    public static final CustomError INVALID_DATE = new CustomError("TSK-07-001", "Tanggal berakhir tidak boleh lebih kecil dari tanggal mulai project.", "End date cannot be less than project start date.", HttpStatus.BAD_REQUEST);
    public static final CustomError ASSIGNED_USER_REQUIRED = new CustomError("TSK-07-001", "User yang di assign tidak boleh kosong", "Assigned user cannot be empty", HttpStatus.BAD_REQUEST);



}
