package com.aid.aidbackend.repository;

import com.aid.aidbackend.entity.Drawing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {
    Optional<Drawing> findByUri(String drawingUri);
    Page<Drawing> findAllByIsPrivate(boolean isPrivate, Pageable pageable);

    Page<Drawing> findAllByMemberId(Long memberId, Pageable pageable);
}
