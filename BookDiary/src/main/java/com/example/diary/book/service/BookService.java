package com.example.diary.book.service;

import com.example.diary.book.client.KakaoBookClient;
import com.example.diary.book.dto.BookResponseDto;
import com.example.diary.book.dto.BookSearchResponse;
import com.example.diary.book.dto.KakaoBookSearchResponse;
import com.example.diary.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final KakaoBookClient kakaoBookClient;
    private final BookRepository bookRepository;

    public List<BookSearchResponse> searchBooks(String keyword) {
        KakaoBookSearchResponse response = kakaoBookClient.searchBooks(keyword);
        return response.getDocuments().stream()
                .map(doc -> BookSearchResponse.builder()
                        .title(doc.getTitle())
                        .author(doc.getAuthors() == null ? null : String.join(", ", doc.getAuthors()))
                        .publisher(doc.getPublisher())
                        .isbn(doc.getIsbn())
                        .imageUrl(doc.getThumbnail())
                        .contents(doc.getContents())
                        .build())
                .toList();
    }

    public List<BookResponseDto> getBookList() {
        return bookRepository.findAll().stream()
                .map(book -> BookResponseDto.builder()
                        .bookId(book.getIdxBook())
                        .isbn(book.getIsbn())
                        .title(book.getTitle())
                        .author(book.getAuthor())
                        .publisher(book.getPublisher())
                        .imageUrl(book.getImageUrl())  // 수정
                        .publishDate(book.getPublishDate())
                        .build())
                .collect(java.util.stream.Collectors.toList()); // 수정
    }
}