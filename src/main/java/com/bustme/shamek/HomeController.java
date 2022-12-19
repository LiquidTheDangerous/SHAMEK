package com.bustme.shamek;

import com.bustme.shamek.domain.Role;
import com.bustme.shamek.repo.MessageRepo;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

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
