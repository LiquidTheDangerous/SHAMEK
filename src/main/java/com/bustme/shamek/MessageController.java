package com.bustme.shamek;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.repo.MessageRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Controller
@RequestMapping("/message")
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
    public String saveMessage(@ModelAttribute Message message, @RequestParam String tags) {
        message.setTag(parseTags(tags));

        messageRepo.save(message);
        return "redirect:/";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/edit/{messageId}")
    public String getMessageEditForm(Model model, @PathVariable Integer messageId) {
        Message message = messageRepo.findMessageById(messageId);
        model.addAttribute("message", message);
        model.addAttribute("tags", message.getTags());
        return "editMessage";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/edit/{messageId}")
    public String saveEditedMessage(@ModelAttribute Message  message, @RequestParam String tags, @PathVariable Integer messageId) {
        message.setTag(parseTags(tags));
        Message editedMessage = messageRepo.findMessageById(messageId);
        if (editedMessage != null) {
            editedMessage.setTag(message.getTag());
            editedMessage.setTitle(message.getTitle());
            editedMessage.setText(message.getText());
            messageRepo.save(editedMessage);
        }
        return "redirect:/";
    }


    @GetMapping("/create")
    public String getMessageForm(Model model) {
        model.addAttribute("message", new Message());
        return "createMessage";
    }
}
