package com.aid.aidbackend.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", length = 255, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "nickname", length = 10, unique = true, nullable = false)
    private String nickname;

    @Column(name = "profile_url", length = 40)
    private String profileUrl;

    @CreationTimestamp
    @Column(name = "registered_at", nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", length = 20, nullable = false)
    private Authority authority;

    @Builder
    public Member(String email, String password, String nickname, Authority authority) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.authority = authority;
    }
}
