package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Project;
import com.veedo.tsk.entity.Teams;
import com.veedo.tsk.request.TeamsRequest;
import com.veedo.tsk.response.ProjectDetailResponse;
import com.veedo.tsk.response.ProjectTaskResponse;
import com.veedo.tsk.exception.ProjectException;
import com.veedo.tsk.request.ProjectRequest;
import com.veedo.tsk.response.ProjectTeamResponse;
import com.veedo.tsk.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class ProjectRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Helpers helpers;

    public UUID checkId(String id) {
        UUID projectId = null;
        try {
            projectId = UUID.fromString(id);
        } catch (Exception e) {
            throw ProjectException.PROJECT_NOT_FOUND;
        }
        return projectId;
    }

    public int getProjectCode() {
        String sql = "SELECT COUNT(project_code) + 1 FROM tsk_projects";
        Integer result = jdbcTemplate.queryForObject(sql, Integer.class);
        return (result != null) ? result : 0;
    }


    public List<Project> findAllProject(String status) {
        String extra = " ";
        System.out.println("iini status kamu " + status);
        if (status != null) {
            extra = (status.equals("overview")) ? "AND s.status_name NOT IN ('Done', 'Cancel')" : "AND s.status_name + '" + status + "' ";
        }
        System.out.println("ini extra kamu " + extra);
        String sql = "SELECT p.project_id, p.project_code, p.project_name, p.project_description, p.start_date, p.end_date, " +
                "CONCAT_WS(' ', u.first_name, u.last_name) AS manager_name, " +
                "u.email AS project_manager, s.status_name, pt.team_id, upt.first_name AS team_members, " +
                "(SELECT COUNT(*) FROM tsk_tasks t WHERE t.project_id = p.project_id AND t.delete_status = 0) AS total_task, " +
                "p.created_date, p.created_by " +
                "FROM tsk_projects p " +
                "JOIN tsk_users u ON p.project_manager = u.user_id " +
                "JOIN tsk_status s ON p.project_status = s.status_id " +
                "LEFT JOIN tsk_project_teams pt ON p.project_id = pt.project_id " +
                "LEFT JOIN tsk_users upt ON pt.team_member_id = upt.user_id " +
                "WHERE p.delete_status = 0 " + extra +
                "ORDER BY \n" +
                "\tCASE\n" +
                "\t\tWHEN s.status_name = 'To Do' THEN 1\n" +
                "\t\tWHEN s.status_name = 'In Progress' THEN 2\n" +
                "\t\tWHEN s.status_name = 'On Hold' THEN 3\n" +
                "\t\tWHEN s.status_name = 'Done' THEN 4\n" +
                "\t\tWHEN s.status_name = 'Cancel' THEN 5\n" +
                "\t\tELSE 6\n" +
                "\tEND";
        System.out.println("ini quer sql" + sql);

        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllProjects(rs));
        } catch (Exception e) {
            return null;
        }

    }

    public List<Project> findAllProjectByUser(String userId, String status) {
        String extra = "";
        if (status != null) {
            extra = "AND s.status_name = '" + status + "' ";
        }
        String sql = "SELECT p.project_id, p.project_code, p.project_name, p.project_description, p.start_date, p.end_date, " +
                "CONCAT_WS(' ', u.first_name, u.last_name) AS manager_name," +
                "u.email AS project_manager, s.status_name, pt.team_id, upt.first_name AS team_members, " +
                "(SELECT COUNT(*) FROM tsk_tasks t WHERE t.project_id = p.project_id AND t.delete_status = 0) AS total_task, " +
                "p.created_date, p.created_by " +
                "FROM tsk_projects p " +
                "JOIN tsk_users u ON p.project_manager = u.user_id " +
                "JOIN tsk_status s ON p.project_status = s.status_id " +
                "LEFT JOIN tsk_project_teams pt ON p.project_id = pt.project_id " +
                "LEFT JOIN tsk_users upt ON pt.team_member_id = upt.user_id " +
                "WHERE pt.team_member_id = ? AND p.delete_status = 0 " + extra;
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllProjects(rs), UUID.fromString(userId));
        } catch (Exception e) {
            return  null;
        }
    }

    public ProjectDetailResponse findProjectDetail(UUID projectId) {
        String sql = "SELECT p.project_id, p.project_code, p.project_name, p.project_description, p.start_date, p.end_date, " +
                "CONCAT_WS(' ', u.first_name, u.last_name) AS project_manager, ps.status_name AS project_status, " +
                "NULLIF(CONCAT_WS(' ', upt.first_name, upt.last_name), '') AS team_members, " +
                "t.task_code, t.task_name, CONCAT(ut.first_name, ' ', ut.last_name) AS assigned_user, " +
                "t.start_date as task_start_date, t.end_date AS task_end_date, ts.status_name AS task_status, " +
                "tp.priority_name AS task_priority, p.created_date, p.created_by " +
                "FROM tsk_projects p " +
                "JOIN tsk_users u ON p.project_manager = u.user_id " +
                "JOIN tsk_status ps ON p.project_status = ps.status_id " +
                "LEFT JOIN tsk_project_teams pt ON p.project_id = pt.project_id " +
                "LEFT JOIN tsk_users upt ON pt.team_member_id = upt.user_id " +
                "LEFT JOIN tsk_tasks t ON p.project_id = t.project_id " +
                "LEFT JOIN tsk_status ts ON t.task_status = ts.status_id " +
                "LEFT JOIN tsk_priority tp ON t.task_priority = tp.priority_id " +
                "LEFT JOIN tsk_users ut ON t.task_assigned_to = ut.user_id " +
                "WHERE p.delete_status = 0 " +
                "AND p.project_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapProjectDetails(rs), projectId);
    }

    public Optional<Project> findProjectById(UUID projectId) {
        String sql = "SELECT * FROM tsk_projects WHERE project_id = ? AND delete_status = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapProject(rs), projectId).stream().findFirst();
    }

    public List<Project> mapAllProjects(ResultSet rs) throws SQLException {
        Map<String, Project> map = new LinkedHashMap<>();

        do {
            String id = rs.getString("project_id");
            Project project = map.get(id);
            if (project == null) {
                project = new Project();
                project.setProjectId(rs.getString("project_id"));
                project.setProjectCode(rs.getString("project_code"));
                project.setProjectName(rs.getString("project_name"));
                project.setProjectDesc(rs.getString("project_description"));
                project.setStartDate(helpers.convertToString(rs.getDate("start_date")));
                project.setEndDate(helpers.convertToString(rs.getDate("end_date")));
                project.setProjectManager(rs.getString("project_manager"));
                project.setManagerName(rs.getString("manager_name"));
                project.setProjectStatus(rs.getString("status_name"));
                project.setCreatedDate(helpers.convertTimestampToString(rs.getTimestamp("created_date")));
                project.setCreatedBy(rs.getString("created_by"));
                project.setTotalTask(rs.getString("total_task"));
                map.put(id, project);
            }
            // ADD TEAM MEMBERS
            String teamId = rs.getString("team_id");
            if (teamId != null && !project.hasTeamInside(teamId)) {
                ProjectTeamResponse data = new ProjectTeamResponse();
                data.setTeamId(rs.getString("team_id"));
                data.setTeamMember(rs.getString("team_members"));
                project.addTeams(data);
            }
        } while (rs.next());

        return new ArrayList<>(map.values());
    }


    public void insertProject(ProjectRequest request, String userId, String projectCode, LocalDateTime time) {
        String sql = "INSERT INTO tsk_projects(project_code, project_name, project_description, start_date, end_date, project_manager, " +
                "project_status, created_date, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, projectCode, request.getProjectName(), request.getProjectDesc(), helpers.convertToDate(request.getStartDate()),
                helpers.convertToDate(request.getEndDate()), UUID.fromString(request.getProjectManager()), UUID.fromString(request.getProjectStatus()), time, userId);
    }

    public void updateProject(ProjectRequest request, UUID projectId, LocalDateTime time, String userName) {
        String sql = "UPDATE tsk_projects SET project_name = ?, project_description = ?, " +
                "start_date = ?, end_date = ?, project_manager = ?, project_status = ?, " +
                "last_update_date = ?, last_update_by = ? " +
                "WHERE project_id = ?";
        jdbcTemplate.update(sql, request.getProjectName(), request.getProjectDesc(), helpers.convertToDate(request.getStartDate()),
                helpers.convertToDate(request.getEndDate()), UUID.fromString(request.getProjectManager()), UUID.fromString(request.getProjectStatus()),
                time, userName, projectId);
    }

    public void deleteProject(UUID projectId) {
        String sql = "UPDATE tsk_projects SET delete_status = -1 WHERE project_id = ? AND delete_status = 0";
        jdbcTemplate.update(sql, projectId);
    }



    public ProjectDetailResponse mapProjectDetails(ResultSet rs) throws SQLException {
        ProjectDetailResponse detail = null;
        Set<String> processProjects = new HashSet<>();
        do {
            String projectId = rs.getString("project_id");
            if (detail == null) {
                detail = new ProjectDetailResponse();
                detail.setProjectId(rs.getString("project_id"));
                detail.setProjectCode(rs.getString("project_code"));
                detail.setProjectName(rs.getString("project_name"));
                detail.setProjectDesc(rs.getString("project_description"));
                detail.setStartDate(helpers.convertToString(rs.getDate("start_date")));
                detail.setEndDate(helpers.convertToString(rs.getDate("end_date")));
                detail.setProjectManager(rs.getString("project_manager"));
                detail.setProjectStatus(rs.getString("project_status"));
                detail.setCreatedDate(helpers.convertTimestampToString(rs.getTimestamp("created_date")));
                detail.setCreatedBy(rs.getString("created_by"));
                processProjects.add(projectId);
            }

            // ADD TEAM MEMBERS
            String teamMember = rs.getString("team_members");
            if(teamMember != null && !detail.getTeamMembers().contains(teamMember)) {
                detail.addTeamMembers(teamMember);
            }

            // ADD TASK LISTS
            String taskCode = rs.getString("task_code");
            if (taskCode != null && !detail.hasTaskProject(taskCode)) {
                ProjectTaskResponse taskProject = new ProjectTaskResponse();
                taskProject.setTaskCode(rs.getString("task_code"));
                taskProject.setTaskName(rs.getString("task_name"));
                taskProject.setAssignedUser(rs.getString("assigned_user"));
                taskProject.setStartDate(helpers.convertToString(rs.getDate("task_start_date")));
                taskProject.setEndDate(helpers.convertToString(rs.getDate("task_end_date")));
                taskProject.setTaskStatus(rs.getString("task_status"));
                taskProject.setTaskPriority(rs.getString("task_priority"));
                detail.addTaskProject(taskProject);
            }

        } while (rs.next());
        return detail;
    }


    public Project mapProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setProjectId(rs.getString("project_id"));
        project.setProjectCode(rs.getString("project_code"));
        project.setProjectName(rs.getString("project_name"));
        project.setProjectDesc(rs.getString("project_description"));
        project.setStartDate(helpers.convertToString(rs.getDate("start_date")));
        project.setEndDate(helpers.convertToString(rs.getDate("end_date")));
        project.setProjectManager(rs.getString("project_manager"));
        project.setProjectStatus(rs.getString("project_status"));
        project.setCreatedDate(helpers.convertTimestampToString(rs.getTimestamp("created_date")));
        project.setCreatedBy(rs.getString("created_by"));
        return project;
    }





}
