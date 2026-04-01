package com.example.diary.book.controller;

import com.example.diary.book.dto.BookSearchResponse;
import com.example.diary.book.service.BookService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.common.dto.ApiResponse;
import com.example.diary.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController extends BaseController{

    private final BookService bookService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<BookSearchResponse>>> searchBooks(
            @RequestParam("keyword") String keyword
    ) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw CustomException.badRequest("검색어는 필수입니다.");
        }

        List<BookSearchResponse> response = bookService.searchBooks(keyword);
        return ok(response);
    }
}