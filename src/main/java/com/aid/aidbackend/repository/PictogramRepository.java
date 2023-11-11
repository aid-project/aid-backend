package com.aid.aidbackend.repository;

import com.aid.aidbackend.entity.Pictogram;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictogramRepository extends JpaRepository<Pictogram, Long> {
    List<Pictogram> findAllByDrawingId(Long drawingId);

    void deleteAllByDrawingId(Long drawingId);
}
