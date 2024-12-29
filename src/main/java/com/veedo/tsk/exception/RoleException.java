package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class RoleException extends RuntimeException {

    public static final CustomError ROLE_NOT_VALID = new CustomError("TSK-02-001", "Role tidak valid", "Role not valid", HttpStatus.BAD_REQUEST);
    public static final CustomError ROLE_EXISTING = new CustomError("TSK-02-002", "Role sudah ada", "Role existing", HttpStatus.BAD_REQUEST);
    public static final CustomError ROLE_NOT_FOUND = new CustomError("TSK-02-003", "Role tidak ditemukan", "Role not found", HttpStatus.BAD_REQUEST);


}
