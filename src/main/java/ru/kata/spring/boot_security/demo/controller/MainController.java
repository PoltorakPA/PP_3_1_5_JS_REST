package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public MainController(UserService userService, RoleService roleService, UserRepository userRepository, UserDetailsServiceImpl userDetailsService) {
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
    public String userPage(Principal principal, Model model) {
        UserDetails user = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", user);
        return "user";
    }

    //отображение всех пользователей
    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("allUsers", userList);
        return "admin";
    }

    //Отображение юзера по id
    @GetMapping("/admin/{id}")
    public String showUser(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }

    //страница создание юзера
    @GetMapping("/admin/new")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "new";
    }

    @PostMapping("/admin/save")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            return "new";
        }
        if (user.getPassword().isEmpty()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("passwordError", "Пароль не должен быть пустым");
            return "new";
        }
        if (userRepository.findUserByUsername(user.getUsername()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("usernameError", "Пользователь с таким именем существует!");
            return "new";
        } else {
            userService.saveUser(user);
            return "redirect:/admin";
        }
    }

    //страница редактирования пользователя
    @GetMapping("/admin/editUser/{username}")
    public String editUser(Model model, @PathVariable("username") String username) {
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("user", userDetailsService.loadUserByUsername(username));
        return "edit";
    }

    @PatchMapping("/admin/{username}")
    public String updateUser(@ModelAttribute("user") @Valid User user
            , @PathVariable("username") String username
    ) {
        userService.updateUser(
                username,
                user);
        return "redirect:/admin";
    }

    //удаление пользователя
    @DeleteMapping("/admin/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
