package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Teams;
import com.veedo.tsk.request.TeamsRequest;
import com.veedo.tsk.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
public class TeamRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Helpers helpers;


    public List<Teams> findAllMemberByProjectId(UUID projectUUID) {
        String sql = "SELECT * FROM tsk_project_teams WHERE project_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTeams(rs), projectUUID);
    }

    public List<Teams> findTeamMember(String userId, UUID projectId) {
        String sql = "SELECT * FROM tsk_project_teams WHERE team_member_id = ? AND project_id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllTeams(rs), userId, projectId);
    }


    public Optional<Teams> findTeamMemberById(UUID teamId) {
        String sql = "SELECT * FROM tsk_project_teams WHERE team_id = ? AND delete_status = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapTeams(rs), teamId).stream().findFirst();
    }

    public Boolean isUserHasInTeam(String userId, String projectId) {
        String sql = "SELECT COUNT(*) FROM tsk_project_teams " +
                "WHERE team_member_id = ? AND project_id = ? AND delete_status = 0";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, UUID.fromString(userId), UUID.fromString(projectId));
        System.out.println("HASIL COUNT :: " + count);
        return (count == 0);
    }


    public void insertTeams(UUID projectId, String memberId, String createdUser, LocalDateTime time) {
        String sql = "UPDATE INTO tsk_project_teams (project_id, team_member_id, " +
                "created_date, created_by) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, projectId, UUID.fromString(memberId), time, createdUser);
    }

    public void deleteTeamMember(String teamId, LocalDateTime lastUpdateDate, String lastUpdateBy) {
        String sql = "UPDATE tsk_project_teams SET delete_status = 0, last_update_date = ?, last_update_by = ? " +
                "WHERE team_id = ? AND delete_status = 0";
        jdbcTemplate.update(sql, lastUpdateDate, lastUpdateBy, UUID.fromString(teamId));
    }

    public List<Teams> mapAllTeams(ResultSet rs) throws SQLException {
        List<Teams> teamsList = new ArrayList<>();
        do {
            System.out.println("ini team repo : " + rs.getString("team_member_id"));
            Teams teams = new Teams();
            teams.setTeamId(rs.getString("team_id"));
            teams.setProjectId(rs.getString("project_id"));
            teams.setTeamMemberId(rs.getString("team_member_id"));
            teams.setCreatedDate(helpers.convertTimestampToString(rs.getTimestamp("created_date")));
            teams.setCreatedBy(rs.getString("created_by"));
            teams.setLastUpdateDate(helpers.convertTimestampToString(rs.getTimestamp("last_update_date")));
            teams.setLastUpdateBy(rs.getString("last_update_by"));
            teamsList.add(teams);
        } while (rs.next());
        return teamsList;
    }


    public Teams mapTeams(ResultSet rs) throws SQLException {
        Teams teams = new Teams();
        teams.setTeamId(rs.getString("team_id"));
        teams.setProjectId(rs.getString("project_id"));
        teams.setTeamMemberId(rs.getString("team_member_id"));
        teams.setCreatedDate(helpers.convertTimestampToString(rs.getTimestamp("created_date")));
        teams.setCreatedBy(rs.getString("created_by"));
        teams.setLastUpdateDate(helpers.convertTimestampToString(rs.getTimestamp("last_update_date")));
        teams.setLastUpdateBy(rs.getString("last_update_by"));
        return teams;
    }
}
