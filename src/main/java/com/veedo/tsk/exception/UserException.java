package com.veedo.tsk.exception;


import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {
    public static final CustomError EMAIL_NOT_VALID = new CustomError("TSK-01-001", "Email tidak valid", "Email not valid", HttpStatus.BAD_REQUEST);
    public static final CustomError EMAIL_ALREADY_REGISTERED = new CustomError("TSK-01-002", "Email telah terdaftar", "Email already registered", HttpStatus.BAD_REQUEST);
    public static final CustomError EMAIL_REQUIRED = new CustomError("TSK-01-003", "Email tidak boleh kosong", "Email cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError PASSWORD_REQUIRED = new CustomError("TSK-01-004", "Passord tidak boleh kosong", "Password cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError FIRST_NAME_REQUIRED = new CustomError("TSK-01-005", "Nama pertama tidak boleh kosong", "First name cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError PHONE_REQUIRED = new CustomError("TSK-01-006", "Nomor telepon tidak boleh kosong", "Phone number cannot be empty", HttpStatus.BAD_REQUEST);
    public static final CustomError PHONE_NOT_VALID = new CustomError("TSK-01-007", "Nomor telepon tidak valid", "Phone number not valid", HttpStatus.BAD_REQUEST);
    public static final CustomError INCORRECT_EMAIL = new CustomError("TSK-01-008", "Email salah", "The email are incorrect", HttpStatus.UNAUTHORIZED);
    public static final CustomError INCORRECT_PASSWORD = new CustomError("TSK-01-009", "Password salah", "The password are incorrect", HttpStatus.UNAUTHORIZED);
    public static final CustomError USER_ALREADY_LOGGED_IN = new CustomError("TSK-01-010", "User sudah masuk ke aplikasi", "User is already logged in to application", HttpStatus.UNAUTHORIZED);
    public static final CustomError USER_NOT_FOUND = new CustomError("TSK-01-011", "User tidak ditemukan", "User not found", HttpStatus.BAD_REQUEST);
    public static final CustomError PASSWORD_CANNOT_BE_SAME = new CustomError("TSK-01-012", "Password yang baru tidak boleh sama dengan yang lama", "New password cannot be the same as your old password", HttpStatus.BAD_REQUEST);
}
