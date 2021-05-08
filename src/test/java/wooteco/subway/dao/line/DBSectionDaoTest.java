package wooteco.subway.dao.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.section.DBSectionDao;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dao.station.DBStationDao;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.dao.dto.SectionEntity;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class DBSectionDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    @Autowired
    DBSectionDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.sectionDao = new DBSectionDao(jdbcTemplate);
        this.stationDao = new DBStationDao(jdbcTemplate);
        this.lineDao = new DBLineDao(jdbcTemplate);
    }

    @BeforeEach
    void init() {

    }

    @Test
    void save() {
        //given
        Station station = stationDao.save(new Station("백기역"));
        Station station2 = stationDao.save(new Station("흑기역"));
        Line line = lineDao.save(new Line("신분당선", "bg-red-600"));

        //when
        SectionEntity sectionEntity = new SectionEntity(line.id(), station.getId(), station2.getId(), 15);
        SectionEntity savedSectionEntity = sectionDao.save(sectionEntity);

        //then
        assertThat(sectionEntity.getLineId()).isEqualTo(savedSectionEntity.getLineId());
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }
}