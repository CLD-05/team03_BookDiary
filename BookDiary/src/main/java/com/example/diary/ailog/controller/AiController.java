package com.example.diary.ailog.controller;

import com.example.diary.common.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AiController extends BaseController {

    @GetMapping("/ai/ai")
    public String aiPage() {
        return "pages/ai/ai";
    }
}