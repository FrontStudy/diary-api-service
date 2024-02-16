package com.yuk.wazzangstudyrestapi1.domains;

import com.yuk.wazzangstudyrestapi1.dtos.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(length=512)
    private String passwd;

    @Setter
    @Column(length=512)
    private String jtoken;

    @Column(length=512)
    private String userrole = "ROLE_USER";

    private String profilePicture;

    @Column(nullable = false, unique = true)
    private String nickname;

    private String birthDate;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String gender;

    private LocalDateTime withdrawalDate;

    @Column(nullable = false)
    private boolean active;

    @Builder
    public Member(Long id, String passwd, String jtoken, String userrole, String profilePicture, String nickname, String birthDate,
                  String name, String gender, String email, LocalDateTime withdrawalDate,
                  boolean active) {
        this.id = id;
        this.passwd = passwd;
        this.jtoken = jtoken;
        this.userrole = userrole;
        this.profilePicture = profilePicture;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.name = name;
        this.gender = gender;
        this.email = email;
        this.withdrawalDate = withdrawalDate;
        this.active = active;
    }

    public void update(String profilePicture, String nickname, boolean active) {
        this.profilePicture = profilePicture;
        this.nickname = nickname;
        this.active = active;
    }
}