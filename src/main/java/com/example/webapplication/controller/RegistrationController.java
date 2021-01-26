package com.example.webapplication.controller;

import com.example.webapplication.domain.User;
import com.example.webapplication.domain.dto.CaptchaResponseDto;
import com.example.webapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {

    @Value("${recaptcha.url}")
    private String recaptchaURL;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserService userService;
    
    @Value("${recaptcha.secretkey}")
    private String secret;

    @GetMapping("/registration")
    public String registration(Model model ){
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@RequestParam("password2") String passwordConfirm,
                          @RequestParam("g-recaptcha-response") String recaptchaResponse,
                          @Valid User user,
                          BindingResult bindingResult,
                          Model model){

        boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
        String url = String.format(recaptchaURL, secret, recaptchaResponse);
        CaptchaResponseDto responseDto = restTemplate.postForObject(url, Collections.emptyMap(), CaptchaResponseDto.class);

        if(!responseDto.isSuccess()){
            model.addAttribute("captchaError", "Please fill captcha");
        }

        if(isConfirmEmpty){
            model.addAttribute("password2Error","Password  confirmation can not be null");
        }
        if(user.getPassword() != null && !user.getPassword().equals(passwordConfirm)){
            model.addAttribute("passwordError", "Password are different!");
        }

        if(isConfirmEmpty || bindingResult.hasErrors() || !responseDto.isSuccess()){
            Map<String, String> errorMassages = UtilsController.getErrors(bindingResult);
            model.addAttribute("user", user);
            model.addAttribute("password2", passwordConfirm);
            model.mergeAttributes(errorMassages);
            return "registration";
        }
        if (!userService.addUser(user)){
            model.addAttribute("usernameError", "User already exist!");
            return "registration";
        }
        model.addAttribute("message", null);
        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code){
        boolean isActivated = userService.isActivateUser(code);
        if(isActivated){
            model.addAttribute("messageType","success");
            model.addAttribute("message","User successfully activated!");
        }else{
            model.addAttribute("messageType","danger");
            model.addAttribute("message","Activation code is not found!");
        }
        return "login";
    }
}
