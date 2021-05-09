package wooteco.subway.station.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.dao.MemoryStationDao;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemoryStationDaoTest {

    private final StationDao stationDao = new MemoryStationDao();

    @AfterEach
    void clean() {
        stationDao.clear();
    }

    @Test
    @DisplayName("이름으로 역을 찾는다.")
    void findByName() {
        String name = "검프역";
        stationDao.save(new Station(1L, name));
        Station station = stationDao.findByName(name).get();

        assertThat(station.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("같은 이름 역을 저장할 시 예외가 발생한다.")
    void saveDuplicatedName() {
        String name = "검프역";
        stationDao.save(new Station(1L, name));
        assertThatThrownBy(() ->
                stationDao.save(new Station(2L, name)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}