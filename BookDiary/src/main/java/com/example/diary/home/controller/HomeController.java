package com.example.diary.home.controller;

import com.example.diary.common.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController extends BaseController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("userName", "테스트");
        model.addAttribute("currentDiary", null);
        model.addAttribute("totalBooks", 0);
        model.addAttribute("totalHighlights", 0);
        model.addAttribute("recentHighlight", null);
        return "pages/home/index";
    }
}