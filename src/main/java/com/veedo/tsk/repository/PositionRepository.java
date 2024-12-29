package com.veedo.tsk.repository;

import com.veedo.tsk.entity.Position;
import io.micrometer.common.util.StringUtils;
import org.hibernate.annotations.processing.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PositionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Position> findAll() {
        String sql = "SELECT * FROM tsk_positions";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapAllPosition(rs));
    }

    public Optional<Position> findByPositionId(String positionId) {
        String sql = "SELECT * FROM tsk_positions WHERE position_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapPosition(rs), UUID.fromString(positionId)).stream().findFirst();
    }

    public Position findByPositionName(String positionName) {
        String sql = "SELECT * FROM tsk_positions WHERE position_name = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapPosition(rs), positionName);
    }

    public boolean isExistByPositionName(String positionName) {
        if (!StringUtils.isBlank(positionName)) {
            String sql = "SELECT COUNT(*) FROM tsk_positions WHERE position_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, positionName);
            return count > 0;
        } else {
            return true;
        }
    }

    public void insertPosition(String positionName) {
        String sql = "INSERT INTO tsk_positions(position_name) VALUES (?)";
        jdbcTemplate.update(sql, positionName);
    }

    public List<Position> mapAllPosition(ResultSet rs) throws SQLException {
        List<Position> positionList = new ArrayList<>();

        do {
            Position position = new Position();
            position.setPositionId(rs.getString("position_id"));
            position.setPositionName(rs.getString("position_name"));
            positionList.add(position);
        } while (rs.next());

        return positionList;
    }

    public Position mapPosition(ResultSet rs) throws SQLException {
        System.out.println(" ===== TESTING POSITION REPOSITORY ===== ");
        Position position = new Position();
        position.setPositionId(rs.getString("position_id"));
        position.setPositionName(rs.getString("position_name"));
        System.out.println("[POSITION REPOSITORY] INI POSITION " + position.getPositionId() + " " + position.getPositionName());
        System.out.println(" ");
        return position;
    }
}
