package com.project.user.controller;

import com.project.user.domain.SecurityUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public String userInfo(@AuthenticationPrincipal SecurityUser user, Model model) {
        model.addAttribute("user", user);
        return "user/userInfo";
    }

    @GetMapping("/join")
    public String join() {
        return "user/join";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        new SecurityContextLogoutHandler().logout(req, res,
                SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/user/login";
    }

}
