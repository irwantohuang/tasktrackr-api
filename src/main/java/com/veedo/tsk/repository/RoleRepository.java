package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RoleRepository {

    public static final String ADMIN = "Admin";
    public static final String MANAGER = "Manager";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Role> findAll() {
        String sql = "SELECT * FROM tsk_roles";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllRole(rs));
    }

    public Optional<Role> findByRoleId(String roleId) {
        String sql = "SELECT * FROM tsk_roles WHERE role_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRole(rs), UUID.fromString(roleId)).stream().findFirst();
    }

    public Role findByRoleName(String roleName) {
        String sql = "SELECT * FROM tsk_roles WHERE role_name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRole(rs), roleName);
    }

    public boolean isExistByRoleName(String roleName) {
        String sql = "SELECT COUNT(*) FROM tsk_roles WHERE role_name = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, roleName);
        return count > 0;
    }

    public void insertRole(String roleName) {
        String sql = "INSERT INTO tsk_roles(role_name) VALUES (?)";
        jdbcTemplate.update(sql, roleName);
    }

    public void deleteRole(String roleId) {
        String sql = "DELETE FROM tsk_roles WHERE role_id = ?";
        jdbcTemplate.update(sql, UUID.fromString(roleId));
    }

    public List<Role> mapAllRole(ResultSet rs) throws SQLException {
        List<Role> roleList = new ArrayList<>();

        do {
            Role role = new Role();
            role.setRoleId(rs.getString("role_id"));
            role.setRoleName(rs.getString("role_name"));
            roleList.add(role);
        } while (rs.next());

        return roleList;
    }

    public Role mapRole(ResultSet rs) throws SQLException {
        Role role = new Role();
        role.setRoleId(rs.getString("role_id"));
        role.setRoleName(rs.getString("role_name"));
        return role;
    }

}
