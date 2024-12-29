package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Priority;
import com.veedo.tsk.entity.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PriorityRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Priority> findAllPriority() {
        String sql = "SELECT * FROM tsk_priority";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllPriority(rs));
    }

    public Optional<Priority> findByPriorityName(String name) {
        String sql = "SELECT * FROM tsk_priority WHERE priority_name = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapPriority(rs), name).stream().findFirst();
    }

    public List<Priority> mapAllPriority(ResultSet rs) throws SQLException {
        List<Priority> priorityList = new ArrayList<>();
        do {
            Priority priority = new Priority();
            priority.setPriorityId(rs.getString("priority_id"));
            priority.setPriorityName(rs.getString("priority_name"));
            priorityList.add(priority);
        } while (rs.next());
        return priorityList;
    }


    public Priority mapPriority(ResultSet rs) throws SQLException {
        Priority priority = new Priority();
        priority.setPriorityId(rs.getString("priority_id"));
        priority.setPriorityName(rs.getString("priority_name"));
        return priority;
    }
}
