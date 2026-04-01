package com.example.diary.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbUser")
@Getter
@NoArgsConstructor
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

    public enum Role {
        ROLE_USER, ROLE_ADMIN
    }

    public enum Status {
        ACTIVE, SUSPENDED
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}