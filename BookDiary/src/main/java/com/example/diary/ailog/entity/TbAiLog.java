package com.example.diary.ailog.entity;

import com.example.diary.book.entity.TbBook;
import com.example.diary.user.entity.TbUser;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "tbAiLog")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TbAiLog {

    // Idx_AILog
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Idx_AiLog")
    private Long idxAiLog;

    // Idx_User - NOT NULL
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Idx_User", nullable = false)
    private TbUser user;

    // Idx_User2 - NOT NULL, tbUser FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Idx_User2", nullable = false)
    private TbUser user2;

    // Idx_Book - NOT NULL, tbBook FK
    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Idx_Book", nullable = false)
    private TbBook idxBook;*/

    @Column(name = "PromptSummary", columnDefinition = "TEXT")
    private String promptSummary;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "Result", columnDefinition = "JSON")
    private Map<String, Object> result;

    @Column(name = "CreateDate")
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist() {
        this.createDate = LocalDateTime.now();
    }

    @Column(name = "PromptTokens")
    private Integer promptTokens;

    @Column(name = "CompletionTokens")
    private Integer completionTokens;

    @Column(name = "TotalTokens")
    private Integer totalTokens;
}