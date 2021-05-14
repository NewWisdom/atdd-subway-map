package wooteco.subway.line.domain;

import wooteco.subway.name.domain.Name;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private Long id;
    private Name name;
    private String color;
    private List<Section> sections = new ArrayList<>();

    public Line(Long id, Name name, String color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<Section> getSections() {
        return sections;
    }
}
