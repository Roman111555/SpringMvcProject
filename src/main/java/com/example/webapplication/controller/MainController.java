package com.example.webapplication.controller;

import com.example.webapplication.domain.Message;
import com.example.webapplication.domain.User;
import com.example.webapplication.repository.MessageRepo;
import com.example.webapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.UUID;


@Controller
public class MainController {

    @Autowired
    private MessageRepo messageRepo;

    @Autowired
    private UserService userService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting( Map<String, Object> model){
        return "greeting";
    }

    @GetMapping("/main")
    public String main(@RequestParam(name = "filter", required = false, defaultValue = "") String filter, Model model){

        Iterable<Message> messages;
        if(filter != null && !filter.isEmpty()){
            messages = messageRepo.findByTag(filter);
        }
        else {
            messages = messageRepo.findAll();
        }
        model.addAttribute("messages", messages);
        model.addAttribute("filter", filter);
        return "main";
    }

    @PostMapping("/main")
    public String add(
            @AuthenticationPrincipal User user,
            @Valid Message message,
            BindingResult bindingResult,
            Model model,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (bindingResult.hasErrors()) {

            Map<String, String> errorMap = UtilsController.getErrors(bindingResult);
            model.mergeAttributes(errorMap);
            model.addAttribute("message", message);

        } else {
            saveFile(message, file);
            message.setAuthor(user);
            model.addAttribute("message", null);
            messageRepo.save(message);
        }
        Iterable<Message> messages = messageRepo.findAll();
        model.addAttribute("messages", messages);
        return "main";
    }

    private void saveFile(
            @Valid Message message,
            @RequestParam("file") MultipartFile file) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "_" + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));
            message.setFilename(resultFileName);
        }
    }

    @GetMapping("/user-messages/{userId}")
    public String userMessages(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId,
            Model model,
            @RequestParam(required = false, defaultValue = "") Long messageId
    ){
        User user = userService.findUserById(userId);
        Set<Message> messages = user.getMessages();

        if(messageId != null){
            Message message = messageRepo.findById(messageId).get();
            model.addAttribute("message", message);
        }

        model.addAttribute("userChannel", user);
        model.addAttribute("subscribersCount", user.getSubscribers().size());
        model.addAttribute("subscriptionsCount", user.getSubscriptions().size());
        model.addAttribute("messages", messages);
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        

        return "userMessages";
    }

    @GetMapping("/delete/user-messages/{userId}")
    public String deleteUserMessages(
            @RequestParam(required = false, defaultValue = "") Long messageId,
            @PathVariable Long userId
    ){
        if(messageId != null){
            Message message = messageRepo.findById(messageId).get();
            messageRepo.delete(message);
        }
        return "redirect:/user-messages/" + userId;
    }

    @PostMapping("/user-messages/{userId}")
    public String updateMessage(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId,
            @RequestParam(required = false) Long messageId,
            @RequestParam String text,
            @RequestParam String tag,
            @RequestParam("file") MultipartFile file) throws IOException {

        Message message = messageRepo.findById(messageId).get();
        if(message.getAuthor().equals(currentUser)){
            if(!StringUtils.isEmpty(text)){
                message.setText(text);
            }
            if(!StringUtils.isEmpty(tag)){
                message.setTag(tag);
            }
            saveFile(message, file);
            messageRepo.save(message);
        }
        return "redirect:/user-messages/" + userId;
    }

}