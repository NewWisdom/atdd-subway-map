package wooteco.subway.line.domain;

import wooteco.subway.line.entity.SectionEntity;

import java.util.List;
import java.util.Optional;

public interface SectionDao {
    SectionEntity save(SectionEntity sectionEntity);

    List<SectionEntity> findAll();

    Optional<SectionEntity> findById(Long id);

    void delete(Long id);
}
