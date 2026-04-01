package com.example.diary.highlight.entity;

import com.example.diary.diary.entity.TbDiary;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tbHighlight")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TbHighlight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Idx_Highlight")
    private Long idxHighlight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Idx_Diary", nullable = false)
    private TbDiary diary;

    @Column(name = "PageNumber")
    private Integer pageNumber;

    @Column(name = "HighlightText", nullable = false, columnDefinition = "TEXT")
    private String highlightText;

    @Column(name = "CreateDate", nullable = false, updatable = false)
    private LocalDateTime createDate;

    @Column(name = "UpdateDate", nullable = false)
    private LocalDateTime updateDate;

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
        this.updateDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateDate = LocalDateTime.now();
    }
    
    public void update(Integer pageNumber, String highlightText) {
        this.pageNumber = pageNumber;
        this.highlightText = highlightText;
    }
}