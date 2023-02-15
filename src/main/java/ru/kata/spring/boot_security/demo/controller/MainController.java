package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public MainController(UserService userService, RoleService roleService, UserRepository userRepository,
                          UserDetailsServiceImpl userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    //страница входа в систему
    @RequestMapping("/login")
    public String homePage() {
        return "login";
    }

    //страница пользователя
    @GetMapping("/user")
    public String userPage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("user", userDetailsService.findByEmail(userDetails.getUsername()));
        return "user";
    }

    //отображение всех пользователей и страница admin
    @GetMapping("/admin")
    public String showAllUsers(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        model.addAttribute("userAuthorized", userDetailsService.findByEmail(userDetails.getUsername()));
        model.addAttribute("roles", roleService.getAllRoles());
        return "admin";
    }

    //страница создание юзера
    @GetMapping("/admin/new")
    public String addNewUser(@AuthenticationPrincipal UserDetails userDetails, Principal principal, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        return "new";
    }

    //сохранение пользователя
    @PostMapping("/admin/save")
    public String createUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getAllRoles());
            return "new";
        }
        if (user.getPassword().isEmpty()) {
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("passwordError", "Пароль не должен быть пустым");
            return "new";
        }
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("usernameError", "Пользователь с таким именем существует!");
            return "new";
        }
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("roles", roleService.getAllRoles());
            model.addAttribute("emailError", "Пользователь с такой почтой существует!");
            return "new";
        } else {
            userService.saveUser(user);
            return "redirect:/admin";
        }
    }

    //редактирование пользователя
    @PatchMapping("/admin/editUser/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "redirect:/admin/editUser/{id}";
        }
        userService.updateUser(user);
        return "redirect:/admin";
    }

    //удаление пользователя
    @DeleteMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
