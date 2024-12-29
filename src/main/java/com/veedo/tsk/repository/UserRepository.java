package com.veedo.tsk.repository;

import com.veedo.tsk.config.JwtTokenConfig;
import com.veedo.tsk.entity.Position;
import com.veedo.tsk.entity.Role;
import com.veedo.tsk.entity.User;
import com.veedo.tsk.exception.ProjectException;
import com.veedo.tsk.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private JwtTokenConfig jwtTokenConfig;


    public UUID checkId(String id) {
        UUID userId = null;
        try {
            userId = UUID.fromString(id);
        } catch (Exception e) {
            throw UserException.USER_NOT_FOUND;
        }
        return userId;
    }


    public List<User> findALl() {
        String sql = "SELECT u.user_id, u.email, u.first_name, u.last_name, u.phone, r.role_name, p.position_name FROM tsk_users u " +
                "JOIN tsk_roles r ON u.role_id = r.role_id " +
                "JOIN tsk_positions p ON u.position_id = p.position_id " +
                "AND u.delete_status = 0 " +
                "ORDER BY u.first_name ASC";
        try {
            return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapFindAll(rs));
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Optional<User> findByUserId(UUID id) {
        String sql = "SELECT u.user_id, r.role_name, p.position_name, u.email, u.first_name, u.last_name, u.phone "
                + "FROM tsk_users u JOIN tsk_roles r ON u.role_id = r.role_id "
                + "JOIN tsk_positions p ON u.position_id = p.position_id " +
                "WHERE u.user_id = ? AND u.delete_status = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapUser(rs), id).stream().findFirst();
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT u.user_id, r.role_name, p.position_name, u.email, u.first_name, u.last_name, u.phone "
                + "FROM tsk_users u JOIN tsk_roles r ON u.role_id = r.role_id "
                + "JOIN tsk_positions p ON u.position_id = p.position_id " +
                "WHERE u.email = ? AND u.delete_status = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapUser(rs), email).stream().findFirst();
    }

    public Optional<User> findAccount(String email) {
        String sql = "SELECT user_id, email, password " +
                "FROM tsk_users WHERE email = ? AND delete_status = 0";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapAccount(rs), email).stream().findFirst();
    }

    public String insertUser(User user, LocalDateTime time) {
        String sql = "INSERT INTO tsk_users(role_id, position_id, email, password, "
                + "first_name, last_name, phone, created_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING user_id";
        Role role = roleRepository.findByRoleName(user.getRoleName());
        Position position = positionRepository.findByPositionName(user.getPositionName());

        UUID userId = jdbcTemplate.queryForObject(sql, UUID.class, UUID.fromString(role.getRoleId()),
                UUID.fromString(position.getPositionId()), user.getEmail(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getPhone(), time);
        return userId.toString();
    }

    public UUID createLoginSession(String email) {
        LocalDateTime time = LocalDateTime.now();
        UUID userId = UUID.fromString(findByEmail(email).orElse(null).getUserId());
        String sql = "INSERT INTO tsk_user_sessions (user_id, login_time)" +
                " VALUES (?, ?) RETURNING session_id";
        return jdbcTemplate.queryForObject(sql, UUID.class, userId, time);
    }

    public Boolean isUserInUsed(String email) {
        LocalDateTime time = LocalDateTime.now();
        System.out.println("LOCAL DATE TIME BROK: " + time);
        String sql = "SELECT COUNT(*) FROM tsk_user_sessions s" +
                " JOIN tsk_users u ON s.user_id = u.user_id" +
                " WHERE DATE_TRUNC('day', s.login_time) >= DATE_TRUNC('day', ? - interval '7 days')" +
                " AND s.logout_time IS NULL" +
                " AND u.email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, time, email);
        return (count == 0);
    }

    public void updateUser(User user) {
        System.out.println("[USER REPOSITORY] INI ROLE NAME " + user.getRoleName());
        System.out.println("[USER REPOSITORY] INI POSITION NAME " + user.getPositionName());
        Role role = roleRepository.findByRoleName(user.getRoleName());
        Position position = positionRepository.findByPositionName(user.getPositionName());
        System.out.println("[USER REPOSITORY] INI ROLE ID " + role.getRoleId() + " " + role.getRoleName());
        System.out.println("[USER REPOSITORY] INI POSITION ID " + position.getPositionId() + " " + position.getPositionName());

        String sql = "UPDATE tsk_users SET password = ?, first_name = ?, last_name = ?, " +
                "phone = ?, role_id = ?, position_id = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sql, user.getPassword(), user.getFirstName(), user.getLastName(), user.getPhone(),
                UUID.fromString(role.getRoleId()), UUID.fromString(position.getPositionId()), UUID.fromString(user.getUserId()));
    }

//    public void uploadProfile(byte[] file, UUID userId) {
//        String sql = "UPDATE tsk_users SET profile_image = ? " +
//                "WHERE user_id = ? AND delete_status = 0";
//        jdbcTemplate.update(sql, file, userId);
//    }

    public void logoutSession(String token) {
        String sessionId = jwtTokenConfig.getSessionIdFromToken(token);
        LocalDateTime time = LocalDateTime.now();
        String sql = "UPDATE tsk_user_sessions SET logout_time = ? " +
                " WHERE session_id = ? AND logout_time IS NULL";
        jdbcTemplate.update(sql, time, UUID.fromString(sessionId));
    }

    public UUID getActiveSession(String token) {
        String sessionId = jwtTokenConfig.getSessionIdFromToken(token);
        String sql = "SELECT session_id FROM tsk_user_sessions WHERE session_id = ? AND logout_time IS NULL LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, UUID.class, UUID.fromString(sessionId));
        } catch (Exception e) {
            return null;
        }
    }

    @Scheduled(fixedDelay = 15000)
    public void checkSession() {
        LocalDateTime time = LocalDateTime.now();
        try {
            timeoutSession(time);
        } catch (Exception e) {
//            Do Nothing
        }
    }

    public void timeoutSession(LocalDateTime time) {
            String sql = "UPDATE tsk_user_sessions SET logout_time = login_time + interval '12 hours' " +
                    "WHERE DATE_TRUNC('day', login_time) >= DATE_TRUNC('day', ? - interval '7 days') " +
                    "AND logout_time IS NULL " +
                    "AND ROUND(EXTRACT(EPOCH FROM(? - login_time))) > 43200";
        jdbcTemplate.update(sql, time, time);
    }





    public List<User> mapFindAll(ResultSet rs) throws SQLException {
        List<User> userList = new ArrayList<>();

        do {
            User user = new User();
            user.setUserId(rs.getString("user_id"));
            user.setEmail(rs.getString("email"));
//            user.setProfileImage(rs.getBytes("profile_image"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhone(rs.getString("phone"));
            user.setRoleName(rs.getString("role_name"));
            user.setPositionName(rs.getString("position_name"));
            userList.add(user);
        } while (rs.next());

        return userList;
    }

    public User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setPositionName(rs.getString("position_name"));
        user.setEmail(rs.getString("email"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setPhone(rs.getString("phone"));
        return user;
    }

    public User mapAccount(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getString("user_id"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    }


}
