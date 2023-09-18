package com.project.user.controller;

import com.project.user.domain.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/userInfo")
    public String userInfo(@AuthenticationPrincipal SecurityUser user, Model model) {
        model.addAttribute("user", user);
        return "user/userInfo";
    }

    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

}
