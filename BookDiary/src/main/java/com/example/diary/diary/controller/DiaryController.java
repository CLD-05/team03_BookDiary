package com.example.diary.diary.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.diary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.example.diary.diary.dto.DiaryResponseDto;
import org.springframework.data.domain.Pageable;

@Controller
@RequiredArgsConstructor
public class DiaryController extends BaseController {

    private final DiaryService diaryService;

    @GetMapping("/diary/diary")
    public String home(Model model) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) return "redirect:/auth/login";
        
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<DiaryResponseDto> diaryPage = diaryService.getDiaryList(userIdx, null, pageable);
        
        // 로그 추가
        System.out.println("totalElements: " + diaryPage.getTotalElements());
        System.out.println("totalPages: " + diaryPage.getTotalPages());
        System.out.println("hasNext: " + diaryPage.hasNext());
        System.out.println("content size: " + diaryPage.getContent().size());
        
        model.addAttribute("diaryList", diaryPage.getContent());
        model.addAttribute("hasNext", diaryPage.hasNext());
        return "pages/diary/diary";
    }

    @GetMapping("/diary/detail/{diaryId}")
    public String detail(@PathVariable Long diaryId) {
        return "pages/diary/diaryDetail";
    }

    @GetMapping("/diary/add")
    public String add() {
        return "pages/diary/diaryAdd";
    }

    @GetMapping("/diary/edit/{diaryId}")
    public String edit(@PathVariable Long diaryId) {
        return "pages/diary/diaryEdit";
    }
}