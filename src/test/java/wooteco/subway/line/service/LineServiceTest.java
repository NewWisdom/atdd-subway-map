package wooteco.subway.line.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.dto.LineRequest;
import wooteco.subway.line.dto.LineResponse;
import wooteco.subway.line.dto.SectionEntity;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.line.dto.SectionRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class LineServiceTest {
    @InjectMocks
    private LineService lineService;

    @Mock
    private LineDao lineDao;

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("노선 정상 저장된다")
    void save() {
        //given
        when(lineDao.save(any(Line.class))).thenReturn(new Line(1L, "신분당선", "화이트"));
        when(sectionDao.save(any(SectionEntity.class))).thenReturn(new SectionEntity(1L, 1L, 1L, 2L, 10));
        when(stationDao.findById(1L)).thenReturn(Optional.of(new Station(1L, "아마찌역")));
        when(stationDao.findById(2L)).thenReturn(Optional.of(new Station(2L, "검프역")));

        LineResponse lineResponse = lineService.save(new LineRequest("신분당선", "화이트", 1L, 2L, 10));

        assertThat(lineResponse.getId()).isEqualTo(1L);
        assertThat(lineResponse.getStations()).hasSize(2);
        assertThat(lineResponse.getStations().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("노선에 구간을 추가한다.")
    void addSection() {
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 10);
        when(sectionDao.save(any(SectionEntity.class))).thenReturn(null);
        lineService.addSection(1L, sectionRequest);
    }
}