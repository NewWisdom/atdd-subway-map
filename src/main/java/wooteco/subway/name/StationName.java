package wooteco.subway.name;

import java.util.regex.Pattern;

public class StationName implements Name {
    private static final Pattern PATTERN = Pattern.compile("^[가-힣|0-9]*역$");
    private final String name;

    public StationName(final String name) {
        this.name = name;
    }

    public void validateName() {
        if (!PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException("잘못된 역 이름입니다.");
        }
    }

    @Override
    public String name() {
        validateName();
        return name;
    }
}
