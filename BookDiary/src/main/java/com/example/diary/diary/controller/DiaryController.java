package com.example.diary.diary.controller;

import com.example.diary.book.service.BookService;
import com.example.diary.common.controller.BaseController;
import com.example.diary.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
public class DiaryController extends BaseController {

    private final DiaryService diaryService;
    private final BookService bookService;

    @GetMapping("/diary/home")
    public String home(Model model) {
        Long userIdx = getCurrentUserIdx();
        
        if (userIdx == null) {
            return "redirect:/auth/login";
        }
        
        model.addAttribute("diaryList", diaryService.getDiaryList(userIdx));
        model.addAttribute("bookList", bookService.getBookList());
        return "pages/diary/home";
    }
}