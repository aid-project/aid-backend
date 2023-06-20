package com.aid.aidbackend.repository;

import com.aid.aidbackend.entity.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrawingRepository extends JpaRepository<Drawing, Long> {
}
