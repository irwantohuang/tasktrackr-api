package com.veedo.tsk.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class SummaryRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public Long countProjects() {
        String sql = "SELECT COUNT(*) FROM tsk_projects WHERE delete_status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Long countTasks() {
        String sql = "SELECT COUNT(*) FROM tsk_tasks WHERE delete_status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    public Long countTaskInProgress() {
        String sql = "SELECT COUNT(*) FROM tsk_tasks t " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "WHERE s.status_name = ? AND delete_status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class, StatusRepository.IN_PROGRESS);
    }

    public Long countTaskInProgressByUser(String userId) {
        String sql = "SELECT COUNT(*) FROM tsk_tasks t " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "WHERE s.status_name = ? AND t.task_assigned_to = ? AND delete_status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class, StatusRepository.IN_PROGRESS, UUID.fromString(userId));
    }

    public Long countTaskToDo() {
        String sql = "SELECT COUNT(*) FROM tsk_tasks t " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "WHERE s.status_name = ? AND delete_status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class, StatusRepository.TO_DO);
    }

    public Long countTaskToDoByUser(String userId) {
        String sql = "SELECT COUNT(*) FROM tsk_tasks t " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "WHERE s.status_name = ? AND t.task_assigned_to = ? AND delete_status = 0";
        return jdbcTemplate.queryForObject(sql, Long.class, StatusRepository.TO_DO, UUID.fromString(userId));
    }
}
