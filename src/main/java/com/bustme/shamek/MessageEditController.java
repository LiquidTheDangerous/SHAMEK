package com.bustme.shamek;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/edit")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class MessageEditController {
    @Autowired
    MessageRepo messageRepo;

    //TODO: add verify for tags
    private Set<String> parseTags(String tags){
        Set<String> result = new TreeSet<>(Arrays.asList(tags.split("#")));
        result.remove(" ");
        return result;
    }

    @PostMapping
    public String saveMessage(Message message, @RequestParam String tags) {
        message.setTag(parseTags(tags));
        messageRepo.save(message);
        return "redirect:/";
    }

    @GetMapping
    public String getMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "edit";
    }
}
