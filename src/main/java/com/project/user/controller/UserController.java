package com.project.user.controller;

import com.project.user.domain.SecurityUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String login(@AuthenticationPrincipal SecurityUser user) {
        if (user != null) return "redirect:/user/userInfo";
        return "user/login";
    }

    @GetMapping("/userInfo")
    public String userInfo(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SecurityUser user = (SecurityUser) authentication.getPrincipal();

        model.addAttribute("user", user);
        return "user/userInfo";
    }

    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

    @GetMapping("/logout")
    public String logout(String accessToken, String loginId) {
        
        return "redirect:/user/login";
    }

}
