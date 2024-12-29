package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Tasks;
import com.veedo.tsk.exception.TaskException;
import com.veedo.tsk.request.TaskCreateRequest;
import com.veedo.tsk.utils.Helpers;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TaskRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Helpers helpers;

    public UUID checkId(String id) {
        UUID taskId = null;
        try {
            taskId = UUID.fromString(id);
        } catch (Exception e) {
            throw TaskException.TASK_NOT_FOUND;
        }
        return taskId;
    }

    public int getTaskCode() {
        String sql = "SELECT COUNT(task_code) + 1 FROM tsk_tasks";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return (result != null) ? result : 0;
    }

    public Optional<Tasks> findTaskById(UUID taskId, UUID projectId) {
        String optional = "";
        if (projectId != null) {
            optional = "AND t.project_id = ?";
        }
        String sql = "SELECT t.task_id, t.task_code, t.project_id, p.project_code, p.project_name, t.task_name, t.task_description, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS task_assigned_to, " +
                "t.start_date, t.end_date, s.status_name, py.priority_name, t.created_date, t.created_by, t.last_update_date, " +
                "t.last_update_by FROM tsk_tasks t " +
                "JOIN tsk_users u ON t.task_assigned_to = u.user_id " +
                "JOIN tsk_projects p ON t.project_id = p.project_id " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "JOIN tsk_priority py ON t.task_priority = py.priority_id " +
                "WHERE t.task_id = ? AND t.delete_status = 0 " + optional;
        try {
            if (projectId != null) {
                return jdbcTemplate.query(sql, (rs, rowNum) -> mapTask(rs), taskId, projectId).stream().findFirst();
            } else {
                return jdbcTemplate.query(sql, (rs, rowNum) -> mapTask(rs), taskId).stream().findFirst();
            }
        } catch (Exception e) {
            return null;
        }

    }


    public List<Tasks> findAllTask(String status, String priority) {
        String statusParams = "";
        String priorityParams = "";

        if (!StringUtils.isBlank(status)) {
            statusParams = "AND s.status_name = ? ";
        }
        if (!StringUtils.isBlank(priority)) {
            priorityParams = "AND py.priority_name = ?";
        }


        String sql = "SELECT t.task_id, t.task_code, t.project_id, p.project_code, p.project_name, t.task_name, t.task_description, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS task_assigned_to, " +
                "t.start_date, t.end_date, s.status_name, py.priority_name, t.created_date, t.created_by, t.last_update_date, " +
                "t.last_update_by FROM tsk_tasks t " +
                "JOIN tsk_users u ON t.task_assigned_to = u.user_id " +
                "JOIN tsk_projects p ON t.project_id = p.project_id " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "JOIN tsk_priority py ON t.task_priority = py.priority_id " +
                "WHERE t.delete_status = 0 " + statusParams + priorityParams;

        try {
            if (!StringUtils.isBlank(status) && !StringUtils.isBlank(priority)) {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), status, priority);
            } else if (!StringUtils.isBlank(status)) {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), status);
            } else if (!StringUtils.isBlank(priority)) {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), priority);
            } else {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs));
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }

    public List<Tasks> findAllTaskByUser(String assignedId, String status, String priority) {
        String statusParams = "";
        String priorityParams = "";

        if (!StringUtils.isBlank(status)) {
            statusParams = "AND s.status_name = ? ";
        }
        if (!StringUtils.isBlank(priority)) {
            priorityParams = "AND py.priority_name = ?";
        }
        String sql = "SELECT t.task_id, t.task_code, t.project_id, p.project_code, p.project_name, t.task_name, t.task_description, " +
                "CONCAT(u.first_name, ' ', u.last_name) AS task_assigned_to, " +
                "t.start_date, t.end_date, s.status_name, py.priority_name, t.created_date, t.created_by, t.last_update_date, " +
                "t.last_update_by FROM tsk_tasks t " +
                "JOIN tsk_users u ON t.task_assigned_to = u.user_id " +
                "JOIN tsk_projects p ON t.project_id = p.project_id " +
                "JOIN tsk_status s ON t.task_status = s.status_id " +
                "JOIN tsk_priority py ON t.task_priority = py.priority_id " +
                "WHERE t.task_assigned_to = ? AND t.delete_status = 0 " + statusParams + priorityParams;

        try {
            if (!StringUtils.isBlank(status) && !StringUtils.isBlank(priority)) {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), UUID.fromString(assignedId), status, priority);
            } else if (!StringUtils.isBlank(status)) {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), UUID.fromString(assignedId), status);
            } else if (!StringUtils.isBlank(priority)) {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), UUID.fromString(assignedId), priority);
            } else {
                return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTask(rs), UUID.fromString(assignedId));
            }
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public void insertTask(TaskCreateRequest request, String projectId, String taskCode, String createdBy, LocalDateTime createdDate) {
        String sql = "INSERT INTO tsk_tasks (task_code, project_id, task_name, task_description, task_assigned_to, start_date, " +
                "end_date, task_status, task_priority, created_date, created_by) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, taskCode, UUID.fromString(projectId), request.getTaskName(), request.getTaskDesc(),
                UUID.fromString(request.getTaskAssignedTo()), helpers.convertToDate(request.getStartDate()),
                helpers.convertToDate(request.getEndDate()), UUID.fromString(request.getTaskStatus()),
                UUID.fromString(request.getTaskPriority()), createdDate, createdBy);
    }

    public void updateTask(TaskCreateRequest request, UUID taskId, UUID projectId, String lastUpdateBy, LocalDateTime lastUpdateDate) {
        String sql = "UPDATE tsk_tasks SET task_name = ?, task_description = ?, task_assigned_to = ?, start_date = ?, " +
                "end_date = ?, task_status = ?, task_priority = ?, last_update_date = ?, last_update_by = ? " +
                "WHERE task_id = ? AND project_id = ?";
        jdbcTemplate.update(sql, request.getTaskName(), request.getTaskDesc(), UUID.fromString(request.getTaskAssignedTo()),
                helpers.convertToDate(request.getStartDate()), helpers.convertToDate(request.getEndDate()),
                UUID.fromString(request.getTaskStatus()), UUID.fromString(request.getTaskPriority()), lastUpdateDate,
                lastUpdateBy, taskId, projectId);
    }

    public void deleteTask(UUID taskId, UUID projectId, LocalDateTime lastUpdateDate, String lastUpdateBy) {
        String sql = "UPDATE tsk_tasks SET delete_status = -1, last_update_date = ?, last_update_by = ? " +
                "WHERE task_id = ? AND project_id = ? AND delete_status = 0";
        jdbcTemplate.update(sql, lastUpdateDate, lastUpdateBy, taskId, projectId);
    }




    public List<Tasks> mapAllTask(ResultSet rs) throws SQLException {
        List<Tasks> tasksList = new ArrayList<>();
        do {
            tasksList.add(mapTask(rs));
        } while (rs.next());
        return tasksList;
    }

    public Tasks mapTask(ResultSet rs) throws SQLException {
        Tasks tasks = new Tasks();
        tasks.setTaskId(rs.getString("task_id"));
        tasks.setTaskCode(rs.getString("task_code"));
        tasks.setProjectId(rs.getString("project_id"));
        tasks.setProjectCode(rs.getString("project_code"));
        tasks.setProjectName(rs.getString("project_name"));
        tasks.setTaskName(rs.getString("task_name"));
        tasks.setTaskDesc(rs.getString("task_description"));
        tasks.setTaskAssignedTo(rs.getString("task_assigned_to"));
        tasks.setStartDate(helpers.convertToString(rs.getDate("start_date")));
        tasks.setEndDate(helpers.convertToString(rs.getDate("end_date")));
        tasks.setTaskStatus(rs.getString("status_name"));
        tasks.setTaskPriority(rs.getString("priority_name"));
        tasks.setCreatedDate(helpers.convertTimestampToString(rs.getTimestamp("created_date")));
        tasks.setCreatedBy(rs.getString("created_by"));
        tasks.setLastUpdateDate(helpers.convertTimestampToString(rs.getTimestamp("last_update_date")));
        tasks.setLastUpdateBy(rs.getString("last_update_by"));
        return tasks;
    }


}
