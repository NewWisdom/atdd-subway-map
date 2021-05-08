package wooteco.subway.domain;

public class Section {
    private Long id;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section(){}

    public Section(final Station upStation, final Station downStation, final int distance) {
        this(0L, upStation, downStation, distance);
    }

    public Section(final Long id, final Station upStation, final Station downStation, final int distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Long id() {
        return id;
    }

    public Station upStation() {
        return upStation;
    }

    public void changeUpStation(Station upStation) {
        this.upStation = upStation;
    }

    public Station downStation() {
        return downStation;
    }

    public void changeDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public int distance() {
        return distance;
    }

    public void changeDistance(int distance) {
        this.distance = distance;
    }
}
