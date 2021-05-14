package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.dao.line.LineDao;
import wooteco.subway.dao.section.SectionDao;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionAddRequest;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.domain.Station;
import wooteco.subway.dao.station.StationDao;
import wooteco.subway.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineDao lineDao;
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public LineService(LineDao lineDao, SectionDao sectionDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    @Transactional
    public LineResponse save(final LineRequest lineRequest) {
        LineEntity savedLineEntity = lineDao.save(new LineEntity(lineRequest.getName(), lineRequest.getColor()));
        sectionDao.save(new SectionEntity(savedLineEntity.id(), lineRequest.getUpStationId(), lineRequest.getDownStationId(), lineRequest.getDistance()));
        List<Station> stations = Arrays.asList(stationDao.findById(lineRequest.getUpStationId()).orElseThrow(() -> new IllegalStateException("없는 역임!")), stationDao.findById(lineRequest.getDownStationId()).orElseThrow(() -> new IllegalStateException("없는 역임!")));
        return new LineResponse(savedLineEntity.id(), savedLineEntity.name(), savedLineEntity.color(), stations.stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList())
        );
    }

    @Transactional
    public void addSection(final Long lineId, final SectionAddRequest sectionAddRequest) {
        // TODO : 예외
        //  lineId가 존재하는지
        //  line의 section에 upstationId와 downStationId 둘다 존재하는지 - 노선의 구간에 이미 등록되어있음
        //  upstationId 또는 downStationId로 section을 찾는데, 찾은 section의 distance가 sectionAddRequest의 distance보다 작거나 같은 경우


        // TODO : line의 section에 sectionAddRequest의 upstationId가 존재하는지
        //  존재하면 sectionAddRequest의 upstationId로 section을 찾고
        //  찾은 section의 upstationId를 sectionAddRequest의 downStationId로 수정한다.
        //  찾은 section의 distance를 sectionAddRequest의 distance를 뺀 값으로 수정한다.
        //

        // TODO : line의 section에 sectionAddRequest의 downStationId가 존재하는지
        //  존재하면 sectionAddRequest의 downStationId로 section을 찾고
        //  찾은 section의 downStationId를 sectionAddRequest의 upStationId로 수정한다.
        //  찾은 section의 distance를 sectionAddRequest의 distance를 뺀 값으로 수정한다.

        // TODO : section save

        sectionDao.save(sectionAddRequest.toEntity(lineId));
    }
}
