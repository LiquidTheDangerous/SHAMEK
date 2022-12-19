package com.bustme.shamek;

import com.bustme.shamek.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @Autowired
    MessageRepo messageRepo;


    @GetMapping()
    public String home(Model model)
    {
        model.addAttribute("messages",messageRepo.findAll());
        return "home";
    }

}
