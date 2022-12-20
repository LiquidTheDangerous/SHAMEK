package com.bustme.shamek.controller;


import com.bustme.shamek.domain.Role;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    UserRepo userRepo;
    /*
    * using "${CurrentUser}" in th
    * */
    @ModelAttribute("CurrentUser")
    public String currentUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @ModelAttribute("CurrentUserRoles")
    public Set<Role> currentUserRoles(){
        String currentUsr = currentUser();
        if(currentUsr == null){
            return null;
        }
        if(currentUsr.equals("anonymousUser")){
            return null;
        }


        return new HashSet<Role>(userRepo.findByUsername(currentUser()).getRoles());
    }

    @ModelAttribute("AdminRole")
    public Role adminRole(){
        return Role.ADMIN;
    }

    @ModelAttribute("Roles")
    public Set<Role> roles(){
        return Arrays.stream(Role.values()).collect(Collectors.toSet());
    }
}

