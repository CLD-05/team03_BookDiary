package com.example.diary.diary.entity;

import com.example.diary.book.entity.TbBook;
import com.example.diary.user.entity.TbUser;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbDiary")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TbDiary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Idx_Diary")
	private Long idxDiary;

	@Column(name = "StartDate")
	private LocalDate startDate;

	@Column(name = "EndDate")
	private LocalDate endDate;

	@Column(name = "Rating", precision = 2, scale = 1)
	private BigDecimal rating;

	@Column(name = "Favorite", nullable = false)
	@Builder.Default
	private Boolean favorite = false;

	@Column(name = "MemoTitle", length = 100)
	private String memoTitle;

	@Lob
	@Column(name = "MemoContent")
	private String memoContent;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private BookStatus status;

	@Column(name = "CreateDate")
	private LocalDateTime createDate;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "Idx_Book")
	private TbBook book;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "Idx_User")
	private TbUser user;

	public void update(BookStatus status, LocalDate startDate, LocalDate endDate, BigDecimal rating, Boolean favorite,
			String memoTitle, String memoContent) {
		if (status != null)
			this.status = status;
		if (startDate != null)
			this.startDate = startDate;
		if (endDate != null)
			this.endDate = endDate;
		this.rating = rating;
		this.favorite = favorite != null ? favorite : this.favorite;
		this.memoTitle = memoTitle;
		this.memoContent = memoContent;
	}

	public void changeStatus(BookStatus status) {
		this.status = status;
	}

}
