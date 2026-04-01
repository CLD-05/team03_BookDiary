package com.example.diary.user.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbUser")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TbUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Idx_User")
    private Long idxUser;

    @Column(name = "ID", nullable = false, unique = true)
    private String userId;

    @Column(name = "Pass", nullable = false)
    private String pass;

    @Column(name = "Email")
    private String email;

    @Column(name = "Name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private Status status;

    @Column(name = "CreateDate")
    private LocalDateTime createDate;

    @Column(name = "UpdateDate")
    private LocalDateTime updateDate;

    public enum Role { ROLE_USER, ROLE_ADMIN }
    public enum Status { ACTIVE, SUSPENDED }

    // 정보 수정 메서드
    public void updateProfile(String name, String email) {
        this.name = name;
        this.email = email;
        this.updateDate = LocalDateTime.now();
    }

    // 비밀번호 변경 메서드
    public void updatePassword(String newEncodedPass) {
        this.pass = newEncodedPass;
        this.updateDate = LocalDateTime.now();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}