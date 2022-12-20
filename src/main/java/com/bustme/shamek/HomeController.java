package com.bustme.shamek;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.domain.Role;
import com.bustme.shamek.repo.MessageRepo;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        model.addAttribute("messages", messageRepo.findAll());
        model.addAttribute("userName",
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName());
        model.addAttribute("roles",
                userRepo.findByUsername(
                        SecurityContextHolder
                                .getContext()
                                .getAuthentication()
                                .getName()).getRoles());
        model.addAttribute("adminRole", Role.ADMIN);
        return "home";
    }
}
