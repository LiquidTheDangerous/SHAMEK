package com.bustme.shamek;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.repo.MessageRepo;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.function.Predicate;

@Controller
@RequestMapping("/message")
public class MessageController {
    @Autowired
    MessageRepo messageRepo;

    @Autowired
    UserRepo userRepo;

    //TODO: add verify for tags
    static Set<String> parseTags(String tags){
        Set<String> result = new TreeSet<>(Arrays.asList(tags.split("#")));
        result.remove(" ");
        result.remove("");
        return result;
    }

    @PostMapping("/create")
    public String saveMessage(@ModelAttribute Message message, @RequestParam String tags) {
        message.setTag(parseTags(tags));
        message.setUser(userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
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
    @PostMapping("/findByTags")
    public String findMessages(@RequestParam("tags") String tags, Model model){
        Set<String> res = parseTags(tags);
        List<Message> messages = messageRepo.findAll().stream().filter(new Predicate<Message>() {
            @Override
            public boolean test(Message message) {
                Set<String> intersection = new HashSet<String>(message.getTag());
                intersection.retainAll(res);
                return intersection.size() != 0;
            }
        }).toList();
        model.addAttribute("messages",messages);
        return "findMessages";
    }
}
