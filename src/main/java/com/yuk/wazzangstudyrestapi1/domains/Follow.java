package com.yuk.wazzangstudyrestapi1.domains;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "follow", uniqueConstraints = @UniqueConstraint(columnNames = {"followerId", "followedId"}))

public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long followedId;

    @Column(nullable = false)
    private Long followerId;

    public Follow(Long followedId, Long followerId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }
}
