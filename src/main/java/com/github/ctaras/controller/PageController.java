package com.github.ctaras.controller;

import com.github.ctaras.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PageController {

    @RequestMapping("/")
    public String redirect() {
        return "redirect:/workspace";
    }

    @RequestMapping("/login")
    public String login(Model uiModel) {
        User user = new User();
        uiModel.addAttribute("userForm", user);
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(Model uiModel) {
        User user = new User();
        uiModel.addAttribute("userForm", user);
        return "login?logout";
    }

    @RequestMapping("/join")
    public String join(Model uiModel) {
        User user = new User();
        uiModel.addAttribute("userForm", user);
        return "join";
    }

    @RequestMapping("/404.html")
    public String render404() {
        return "404";
    }
}
