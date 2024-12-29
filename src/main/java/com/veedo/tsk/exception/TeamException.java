package com.veedo.tsk.exception;

import com.veedo.tsk.schema.CustomError;
import org.springframework.http.HttpStatus;

public class TeamException  extends RuntimeException {

    public static final CustomError MEMBER_HAS_IN_TEAM = new CustomError("TSK-08-001", "Member telah ada di dalam team", "Member has in the team", HttpStatus.BAD_REQUEST);
    public static final CustomError DUPLICATE = new CustomError("TSK-08-001", "User tidak boleh duplikat", "User cannot be duplicate", HttpStatus.BAD_REQUEST);
    public static final CustomError MEMBER_NOT_FOUND = new CustomError("TSK-08-001", "Member tidak ditemukan", "Member not found", HttpStatus.BAD_REQUEST);
    public static final CustomError USER_NOT_IN_TEAM = new CustomError("TSK-08-001", "User tidak ada didalam team", "User not in team", HttpStatus.BAD_REQUEST);



}