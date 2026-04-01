package com.example.diary.book.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "tbBook")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TbBook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Idx_Book")
    private Long id;

    @Column(name = "Isbn", nullable = false, length = 20, unique = true)
    private String isbn;

    @Column(name = "Title", nullable = false, length = 255)
    private String title;

    @Column(name = "Author", length = 100)
    private String author;

    @Column(name = "Publisher", length = 100)
    private String publisher;

    @Column(name = "Category", length = 50)
    private String category;

    @Column(name = "ImageURL", length = 500)
    private String imageUrl;

    @Column(name = "TotalPage")
    private Integer totalPage;

    @Column(name = "PublishDate")
    private LocalDate publishDate;
}
