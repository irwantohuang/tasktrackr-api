package com.veedo.tsk.config;

import com.veedo.tsk.schema.CustomError;
import com.veedo.tsk.schema.ErrorSchema;
import com.veedo.tsk.schema.ResponseSchema;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@ControllerAdvice
public class ExceptionConfig {

    public static final CustomError SUCCESS = new CustomError("WGO-00-000", "Berhasil", "Success", HttpStatus.OK);
    public static final CustomError AUTHORIZATION = new CustomError("TSK-00-001", "Authorization header tidak ada", "Authorization header missing", HttpStatus.UNAUTHORIZED);
    public static final CustomError INVALID_TOKEN = new CustomError("TSK-00-002", "Token tidak valid", "Invalid Token", HttpStatus.UNAUTHORIZED);
    public static final CustomError MISSING_HEADER = new CustomError("TSK-00-003", "Header tidak ada (Detail)", "Missing header (Detail)", HttpStatus.UNAUTHORIZED);
    public static final CustomError NO_ACCESS = new CustomError("TSK-00-004", "Tidak berhak mengakses service ini", "Not allowed to access this service", HttpStatus.UNAUTHORIZED);
    public static final CustomError EMPTY_RESULT = new CustomError("TSK-00-999", "Data tidak ada", "Data not found", HttpStatus.BAD_REQUEST);


    @ExceptionHandler(CustomError.class)
    public ResponseEntity<ResponseSchema> handleCustomError(CustomError err) {
        return new ResponseEntity<>(
                new ResponseSchema(
                        new ErrorSchema(err.getErrorCode(), err.getIndonesian(), err.getEnglish())
                ),
                err.getHttpStatus()
        );
    }

    @ExceptionHandler({ExpiredJwtException.class, MalformedJwtException.class, SignatureException.class})
    public ResponseEntity<ResponseSchema> handleInvalidToken() {
        return handleCustomError(INVALID_TOKEN);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ResponseSchema> handleMissingReqHeader(MissingRequestHeaderException e) {
        CustomError err = e.getHeaderName().equals("Authorization") ? AUTHORIZATION : MISSING_HEADER;
        return handleCustomError(err);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ResponseSchema> handleEmptyResult(EmptyResultDataAccessException e) {
        return handleCustomError(EMPTY_RESULT);
    }

//    public ResponseEntity<ResponseSchema> handleGeneralError(Exception e) {
//        Map<String, String> response = new LinkedHashMap<>();
//        response.put("result", e.toString());
//        CustomError err =
//    }
}
