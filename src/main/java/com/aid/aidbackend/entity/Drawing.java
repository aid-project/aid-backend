package com.aid.aidbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "drawing")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Drawing {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "is_private", nullable = false)
    private boolean isPrivate;

    @Column(name = "drawing_url", length = 40, unique = true, nullable = false)
    private String uri;

    @Column(name = "created_at", nullable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder
    public Drawing(Long memberId, boolean isPrivate, String uri) {
        this.memberId = memberId;
        this.isPrivate = isPrivate;
        this.uri = uri;
    }

    public void updateIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }
}
