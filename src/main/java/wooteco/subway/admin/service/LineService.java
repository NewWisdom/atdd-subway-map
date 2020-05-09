package wooteco.subway.admin.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.LineStationCreateRequest;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class LineService {
	private LineRepository lineRepository;
	private StationRepository stationRepository;

	public LineService(LineRepository lineRepository, StationRepository stationRepository) {
		this.lineRepository = lineRepository;
		this.stationRepository = stationRepository;
	}

	public LineResponse save(Line line) {
		if (lineRepository.existsByName(line.getName())) {
			throw new RuntimeException();
		}
		return LineResponse.of(lineRepository.save(line));
	}

	public List<LineResponse> showLines() {
		return LineResponse.listOf(lineRepository.findAll());
	}

	public LineResponse updateLine(Long id, Line line) {
		if (!lineRepository.existsByName(line.getName())) {
			throw new RuntimeException();
		}
		Line persistLine = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		return LineResponse.of(lineRepository.save(persistLine.update(line)));
	}

	public void deleteLineById(Long id) {
		lineRepository.deleteById(id);
	}

	public void addLineStation(Long id, LineStationCreateRequest lineStationCreateRequest) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		line.addLineStation(lineStationCreateRequest.toLineStation());
		lineRepository.save(line);
	}

	public void removeLineStation(Long lineId, Long stationId) {
		Line line = lineRepository.findById(lineId).orElseThrow(RuntimeException::new);
		line.removeLineStationById(stationId);
		lineRepository.save(line);
	}

	public List<LineResponse> findAllLineWithStations() {
		List<Line> lines = lineRepository.findAll();
		List<Set<Station>> stations = lines.stream().map(this::getStations).collect(Collectors.toList());
		return LineResponse.listOf(lines, stations);
	}

	private Set<Station> getStations(Line line) {
		return stationRepository.findAllById(line.getLineStationsId());
	}

	public LineResponse findLineWithStationsById(Long id) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		return LineResponse.of(line, getStations(line));
	}

	public LineResponse findById(final Long id) {
		Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
		return LineResponse.of(line);
	}
}
