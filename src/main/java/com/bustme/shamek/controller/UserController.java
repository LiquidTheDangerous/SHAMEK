package com.bustme.shamek.controller;

import com.bustme.shamek.domain.Role;
import com.bustme.shamek.domain.User;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String usersList(Model model){
        model.addAttribute("users",userRepo.findAll());
        return "usersList";
    }
    @GetMapping("{user}")
    public String userEdit(@PathVariable Long user, Model model){
        model.addAttribute("UserEdit",userRepo.findById(user));
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }
    @PostMapping
    public String userSave(@RequestParam String username, @RequestParam Map<String,String> form, @RequestParam("userId") User userEdit){
        userEdit.setUsername(username);
        Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());
        userEdit.getRoles().clear();
        for(String key : form.keySet()){
            if (roles.contains(key)){
                userEdit.getRoles().add(Role.valueOf(key));
            }
        }
        userRepo.save(userEdit);
        return "redirect:/users";
    }
}
