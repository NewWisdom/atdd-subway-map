package wooteco.subway.line.repository;

import org.springframework.stereotype.Repository;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;

import java.util.List;
import java.util.Optional;

@Repository
public class LineRepository {
    private final LineDao lineDao;
    private final SectionRepository sectionRepository;

    public LineRepository(LineDao lineDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.sectionRepository = sectionRepository;
    }

    public List<Line> findAll() {
        return lineDao.findAll();
    }

    public Line findById(Long id) {
        Line line = lineDao.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("[ERROR] 존재하지 않는 노선입니다."));
        line.initSections(sectionRepository.findAllByLineId(id));
        return line;
    }

    public Optional<Line> findByName(String name) {
        return lineDao.findByName(name);
    }

    public void update(Line line) {
        lineDao.update(line);
    }

    public Line save(String name, String color, Long upStationId, Long downStationId, int distance) {
        Line line = lineDao.save(new Line(name, color));
        Section section = sectionRepository.save(line.id(), upStationId, downStationId, distance);
        return new Line(line.id(), line.name(), line.color(), section);
    }

    public void updateSection(Long lineId, Section section) {
        sectionRepository.update(lineId, section);
    }
}
