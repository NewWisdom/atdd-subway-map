package wooteco.subway.station.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.dto.StationRequest;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() throws JsonProcessingException {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        String content = objectMapper.writeValueAsString(stationRequest);

        // when
        ExtractableResponse<Response> response = addStation(content);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> addStation(String content) {
        return RestAssured.given().log().all()
                .body(content)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() throws JsonProcessingException {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        String content = objectMapper.writeValueAsString(stationRequest);

        addStation(content);

        // when
        ExtractableResponse<Response> response = addStation(content);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() throws JsonProcessingException {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        String content = objectMapper.writeValueAsString(stationRequest);

        ExtractableResponse<Response> createResponse1 = addStation(content);

        StationRequest stationRequest2 = new StationRequest("역삼역");
        String content2 = objectMapper.writeValueAsString(stationRequest2);
        ExtractableResponse<Response> createResponse2 = addStation(content2);

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() throws JsonProcessingException {
        // given
        StationRequest stationRequest = new StationRequest("강남역");
        String content = objectMapper.writeValueAsString(stationRequest);

        ExtractableResponse<Response> createResponse = addStation(content);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("역 제거시 없는 노선이면 예외가 발생한다.")
    @Test
    void deleteStationException() {
        // given
        String uri = "/stations/{id}";

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .delete(uri, 0L)
                .then().log().all()
                .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
