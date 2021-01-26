package com.example.webapplication.controller;

import com.example.webapplication.domain.Role;
import com.example.webapplication.domain.User;
import com.example.webapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public String userList(Model model){
        model.addAttribute("users", userService.findAll());
        return "userList";
    }

    // со страницы userList возвращаем userID spring умный из базы вытащит сразу данные пользователя по ID
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable Long user, Model model){
        model.addAttribute("user", userService.findUserById(user));
//        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/delete/{user}")
    public String userDeleteForm(@PathVariable Long user){
        userService.delete(user);
        return "redirect:/user";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String,String> form,
            @RequestParam("userId") Long user
    ){
        userService.saveUser(user, username, form);
        return"redirect:/user";
    }

    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user){
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());
        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(@AuthenticationPrincipal User user,
                                @RequestParam String password,
                                @RequestParam String email){

        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }

    @GetMapping("subscribe/{userId}")
    public String subscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId
    ){
        userService.subscribe(currentUser, userId);
        return "redirect:/user-messages/" + userId;
    }

    @GetMapping("unsubscribe/{userId}")
    public String unSubscribe(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Long userId
    ){
        userService.unSubscribe(currentUser, userId);
        return "redirect:/user-messages/" + userId;
    }

    @GetMapping("{type}/{userId}/list")
    public String userList(
            @PathVariable String type,
            @PathVariable Long userId,
            Model model
    ){
        User user = userService.findUserById(userId);
        model.addAttribute("userChannel", user);;
        model.addAttribute("type", type);

        if("subscriptions".equals(type)){
            model.addAttribute("users", user.getSubscriptions());
        }else{
            model.addAttribute("users", user.getSubscribers());
        }
        return "subscriptions";

    }

}
