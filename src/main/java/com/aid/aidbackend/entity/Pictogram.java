package com.aid.aidbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pictogram")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pictogram {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "drawing_id", nullable = false)
    Long drawingId;

    @Column(name = "pictogram_url", nullable = false, unique = true)
    String uri;

    @Builder
    public Pictogram(Long drawingId, String uri) {
        this.drawingId = drawingId;
        this.uri = uri;
    }


}
