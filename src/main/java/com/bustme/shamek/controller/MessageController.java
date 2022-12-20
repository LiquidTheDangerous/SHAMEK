package com.bustme.shamek.controller;

import com.bustme.shamek.domain.Message;
import com.bustme.shamek.domain.Role;
import com.bustme.shamek.repo.MessageRepo;
import com.bustme.shamek.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.transaction.Transactional;
//import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/message")
@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
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
        String regex = "(?:\\s*#\\s*\\w*\\s*)+";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(tags).matches()){
            return "createMessage";
        }
        message.setApproved(false);
        message.setTag(parseTags(tags));
        message.setUser(userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));
        messageRepo.save(message);
        return "redirect:/";
    }


    @PostMapping("/delete/{messageId}")
    @Transactional
    public String deleteMessage(@PathVariable Integer messageId) {
        if (!(messageRepo
                .findMessageById(messageId)
                .getUser()
                .getUsername()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getRoles().contains(Role.ADMIN))){
            return "error";
        }
        messageRepo.removeById(messageId);
        return "redirect:/";
    }

    @GetMapping("/edit/{messageId}")
    public String getMessageEditForm(Model model, @PathVariable Integer messageId) {
        Message message = messageRepo.findMessageById(messageId);
        model.addAttribute("message", message);
        model.addAttribute("tags", message.getTags());
        return "editMessage";
    }


    //пользователь это автор поста или пользователь админ
    @PostMapping("/edit/{messageId}")
    public String saveEditedMessage(@ModelAttribute Message  message, @RequestParam String tags, @PathVariable Integer messageId, Model model) {
        if (!(messageRepo
                .findMessageById(messageId)
                .getUser()
                .getUsername()
                .equals(SecurityContextHolder.getContext().getAuthentication().getName()) ||
                userRepo.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()).getRoles().contains(Role.ADMIN))){
            return "error";
        }

        message.setTag(parseTags(tags));
        Message editedMessage = messageRepo.findMessageById(messageId);
        if (editedMessage != null) {
            editedMessage.setTag(message.getTag());
            editedMessage.setTitle(message.getTitle());
            editedMessage.setText(message.getText());
            String regex = "(?:\\s*#\\s*\\w*\\s*)+";
            Pattern pattern = Pattern.compile(regex);
            if (!pattern.matcher(tags).matches()){
                model.addAttribute("message", editedMessage);
                return "editMessage";
            }


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
                if (message.getApproved().equals(false)){
                    return false;
                }
                Set<String> intersection = new HashSet<String>(message.getTag());
                intersection.retainAll(res);
                return intersection.size() != 0;
            }
        }).toList();
        model.addAttribute("messages",messages);
        return "findMessages";
    }

    @GetMapping("/findByUserName/{UserName}")
    public String findMessagesByUserName(@PathVariable("UserName") String UserName, Model model){
        model.addAttribute("messages",messageRepo.findMessagesByUserUsername(UserName));
        return "findMessages";
    }

    @GetMapping("/unapproved")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String getAllUnapprovedMessages(Model model){
        Set<Message> messages = messageRepo.findMessagesByApproved(false);
        model.addAttribute("messages",messages);
        return "findMessages";
    }

    @PostMapping("/approve/{messageId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    public String approveMessage(@PathVariable("messageId") Integer messageId, Model model){
        Message message = messageRepo.findMessageById(messageId);
        message.setApproved(true);
        Set<Message> messages = messageRepo.findMessagesByApproved(false);
//        if (messages == null){
//
//        }
        model.addAttribute("messages",messages);
        return "redirect:/message/unapproved";
    }

}
