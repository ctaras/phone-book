package com.github.ctaras.controller;

import com.github.ctaras.dao.UserDao;
import com.github.ctaras.domain.User;
import com.github.ctaras.error.UserLoginAlreadyExistsException;
import com.github.ctaras.security.SecurityService;
import com.github.ctaras.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class);

    private final UserDao userDao;

    private final SecurityService securityService;

    @Autowired
    public UserController(SecurityService securityService, UserDao userDao) {
        this.securityService = securityService;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public String registration(@Valid @ModelAttribute("userForm") User userForm,
                               BindingResult bindingResult,
                               Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            return "join";
        }

        try {
            userDao.save(userForm);
        } catch (UserLoginAlreadyExistsException e) {
            model.addAttribute("loginExists", true);
            return "join";
        }

        //securityService.autologin(userForm.getLogin(), userForm.getPassword());

        return "redirect:/login";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String viewProfile(Model model, Principal principal) {
        User user = getCurrentUser(principal);
        model.addAttribute("userForm", user);

        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String updateProfile(@Valid @ModelAttribute("userForm") User userForm,
                                BindingResult bindingResult, Model model, Principal principal) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("hasError", true);
            return "profile";
        }

        User user = getCurrentUser(principal);
        user.setFullName(userForm.getFullName());
        user.setPassword(userForm.getPassword());

        try {
            userDao.save(userForm);
        } catch (UserLoginAlreadyExistsException e) {
            Log.e(logger, () -> "Save error user: " + user, e);
        }

        return "redirect:/workspace";
    }

    @RequestMapping(value = "/workspace")
    public String workspace() {
        return "workspace";
    }

    private User getCurrentUser(Principal principal) {
        User user = null;

        String username = principal.getName();
        if (username != null) {
            user = userDao.findByLogin(username);
        }

        return user;
    }
}
