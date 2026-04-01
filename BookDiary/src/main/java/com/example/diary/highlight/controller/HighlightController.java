package com.example.diary.highlight.controller;

import com.example.diary.common.controller.BaseController;
import com.example.diary.highlight.dto.HighlightResponseDto;
import com.example.diary.highlight.service.HighlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HighlightController extends BaseController {

    private final HighlightService highlightService;

    @GetMapping("/highlight/highlight")
    public String highlightList(Model model) {
        Long userIdx = getCurrentUserIdx();
        if (userIdx == null) return "redirect:/auth/login";

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<HighlightResponseDto> highlightPage = highlightService.getAllHighlightList(userIdx, pageable);
        model.addAttribute("highlightList", highlightPage.getContent());
        model.addAttribute("hasNext", highlightPage.hasNext());
        return "pages/highlight/highlight";
    }

    @GetMapping("/highlight/add")
    public String highlightAdd() {
        return "pages/highlight/highlightAdd";
    }

    @GetMapping("/highlight/detail")
    public String highlightDetail() {
        return "pages/highlight/highlightDetail";
    }

    @GetMapping("/highlight/edit")
    public String highlightEdit() {
        return "pages/highlight/highlightEdit";
    }
}