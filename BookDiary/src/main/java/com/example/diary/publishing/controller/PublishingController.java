package com.example.diary.publishing.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PublishingController {

	@GetMapping("/publishing/index")
    public String index() {
        return "pages/publishing/index";
    }

    @GetMapping("/publishing/login")
    public String login() {
        return "pages/publishing/login";
    }

    @GetMapping("/publishing/signup")
    public String signup() {
        return "pages/publishing/signup";
    }

    @GetMapping("/publishing/dashboard")
    public String dashboard() {
        return "pages/publishing/dashboard";
    }

    @GetMapping("/publishing/book-list")
    public String bookList() {
        return "pages/publishing/book-list";
    }
}