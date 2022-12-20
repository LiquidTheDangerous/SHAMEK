package com.bustme.shamek.controller;


import com.bustme.shamek.UserService;
import com.bustme.shamek.domain.Role;
import com.bustme.shamek.domain.User;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.List;

@Controller
public class RegistrationController {
    @Autowired
    UserService userService;

    @Autowired
    UserRepo userRepo;

    @GetMapping("/registration")
    public String registration(Model model){
//        model.addAttribute("user", new User());

        return "registration";
    }

    @RequestMapping(value = {"/login"}, method = RequestMethod.GET)
    public String login(){
        return "login";
    }
    @PostMapping("/registration")
    public String registerUser(Model model, @RequestParam("username") String username, @RequestParam("password") String password) {

        User u = userRepo.findByUsername(username);


        if(u!=null){
            model.addAttribute("message", "User already exists");
            return "redirect:/registration";
        }
        User newusr = new User();
        newusr.setPassword(password);
        newusr.setUsername(username);
        userService.saveUser(newusr);
        model.addAttribute("message", "User registered successfully!");

        return "redirect:/login";
    }
}
