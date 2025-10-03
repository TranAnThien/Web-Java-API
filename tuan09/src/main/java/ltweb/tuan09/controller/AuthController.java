package ltweb.tuan09.controller;

import ltweb.tuan09.dto.LoginDto;
import ltweb.tuan09.dto.SignUpDto;
import ltweb.tuan09.entity.Role;
import ltweb.tuan09.entity.Users;
import ltweb.tuan09.repository.RoleRepository;
import ltweb.tuan09.repository.UserRepository;
import ltweb.tuan09.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new SignUpDto());
        return "register";
    }

    // Xử lý thông tin đăng ký
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") SignUpDto signUpDto, Model model) {
        if (userService.existsByUsername(signUpDto.getUsername())) {
            model.addAttribute("errorMessage", "Username is already taken!");
            return "register";
        }

        if (userService.existsByEmail(signUpDto.getEmail())) {
            model.addAttribute("errorMessage", "Email is already taken!");
            return "register";
        }

        userService.saveUser(signUpDto);
        return "redirect:/register?success";
    }

    @GetMapping("/403")
    public String accessDeniedPage() {
        return "403";
    }
}
