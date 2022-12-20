package com.bustme.shamek.controller;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.domain.Role;
import com.bustme.shamek.repo.MessageRepo;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomeController {
    //TODO: Add global Model data such as roles and userRole
    @Autowired
    MessageRepo messageRepo;

    @Autowired
    UserRepo userRepo;


    @GetMapping()
    public String home(Model model) {
        model.addAttribute("messages", messageRepo.findMessagesByApproved(true));
        return "home";
    }
}
