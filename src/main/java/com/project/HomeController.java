package com.project;

import com.project.user.domain.SecurityUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(@AuthenticationPrincipal SecurityUser user, Model model) {
        if (user == null) {
            return "user/login";
        } else {
            model.addAttribute("user", user);
            return "user/userInfo";
        }
    }
}
