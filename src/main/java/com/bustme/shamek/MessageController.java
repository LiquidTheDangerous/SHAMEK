package com.bustme.shamek;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/message")
@PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
public class MessageController {
    @Autowired
    MessageRepo messageRepo;

    //TODO: add verify for tags
    private Set<String> parseTags(String tags){
        Set<String> result = new TreeSet<>(Arrays.asList(tags.split("#")));
        result.remove(" ");
        result.remove("");
        return result;
    }

    @PostMapping("/create")
    public String saveMessage(Message message, @RequestParam String tags) {
        message.setTag(parseTags(tags));
        messageRepo.save(message);
        return "redirect:/";
    }

    @GetMapping("/edit/{messageId}")
    public String getMessageEditForm(Model model, @PathVariable Integer messageId) {
        model.addAttribute("message", messageRepo.findMessageById(messageId));
        return "editMessage";
    }



    @GetMapping("/create")
    public String getMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "createMessage";
    }
}
