package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class StatusRepository {

    public static final String ON_HOLD = "On Hold";
    public static final String IN_PROGRESS = "In Progress";
    public static final String TO_DO = "To Do";
    public static final String DONE = "Done";
    public static final String CANCEL = "Cancel";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Status> findAllStatus() {
        String sql = "SELECT * FROM tsk_status";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllStatus(rs));
    }

    public Optional<Status> findByStatusId(String id) {
        String sql = "SELECT * FROM tsk_status WHERE status_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Status.class), UUID.fromString(id)).stream().findFirst();
    }

    public Optional<Status> findByStatusName(String name) {
        String sql = "SELECT * FROM tsk_status WHERE status_name = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapStatus(rs), name).stream().findFirst();
    }

    public List<Status> mapAllStatus(ResultSet rs) throws SQLException {
        List<Status> statusList = new ArrayList<>();
        do {
            Status status = new Status();
            status.setStatusId(rs.getString("status_id"));
            status.setStatusName(rs.getString("status_name"));
            statusList.add(status);
        } while (rs.next());
        return statusList;
    }


    public Status mapStatus(ResultSet rs) throws SQLException {
        Status status = new Status();
        status.setStatusId(rs.getString("status_id"));
        status.setStatusName(rs.getString("status_name"));
        return status;
    }

//    public Optional<Status> getAllStatus(String authorization) {
//
//    }
}
