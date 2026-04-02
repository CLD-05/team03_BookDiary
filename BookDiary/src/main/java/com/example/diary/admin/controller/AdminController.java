package com.example.diary.admin.controller;

import com.example.diary.common.controller.BaseController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController extends BaseController {

	@GetMapping("/admin/admin")
	public String adminPage() {
	    return "pages/admin/admin";
	}
}