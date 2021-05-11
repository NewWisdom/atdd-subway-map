package wooteco.subway.line.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class SectionDaoImpl implements SectionDao {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Section> sectionRowMapper;

    public SectionDaoImpl(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionRowMapper = (rs, rowNum) ->
                new Section(rs.getLong("id"),
                        rs.getLong("line_id"),
                        rs.getLong("up_station_id"),
                        rs.getLong("down_station_id"),
                        rs.getInt("distance"));
    }

    @Override
    public Section save(final Section section) {
        String sql = "INSERT INTO SECTION(line_id, up_station_id, down_station_id, distance) VALUES(?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"});
            ps.setLong(1, section.lineId());
            ps.setLong(2, section.upStation().id());
            ps.setLong(3, section.downStation().id());
            ps.setInt(4, section.distance());
            return ps;
        }, keyHolder);

        long newId = keyHolder.getKey().longValue();
        return new Section(newId, section.upStation(), section.downStation(), section.distance());
    }

    @Override
    public List<Section> findAll() {
        return null;
    }

    @Override
    public Optional<Section> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public List<Section> findAllByLineId(Long lineId) {
        String sql = "SELECT * FROM SECTION WHERE line_id = ?";
        List<Section> sectionEntities = jdbcTemplate.query(sql, sectionRowMapper, lineId);
        if (sectionEntities.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 노선에 구간이 등록되어 있지 않습니다.");
        }
        return sectionEntities;
    }

    @Override
    public void update(Section section) {
        String sql = "UPDATE SECTION " +
                "SET up_station_id = ?, down_station_id = ?, distance = ? " +
                "WHERE id = ?";
        jdbcTemplate.update(sql, section.upStation().id(), section.downStation().id(), section.distance(), section.id());
    }
}
