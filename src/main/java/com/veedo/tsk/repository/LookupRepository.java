package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Status;
import com.veedo.tsk.response.LookupUserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LookupRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<LookupUserResponse> findLookupUser() {
        String sql = "SELECT u.user_id, u.email, " +
                "NULLIF(CONCAT(u.first_name, ' ', u.last_name), '') AS full_name, " +
                "r.role_name " +
                "FROM tsk_users u " +
                "JOIN tsk_roles r ON u.role_id = r.role_id";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(LookupUserResponse.class));
    }

    public List<Status> findLookupStatus() {
        String sql = "SELECT * FROM tsk_status";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Status.class));
    }
}
