package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;


@Controller

public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //отображение всех пользователей
    @GetMapping("/admin")
    public String showAllUsers(Model model) {
        List<User> userList = userService.getAllUsers();
        model.addAttribute("allUsers", userList);
        return "admin";
    }

    //Отображение юзера по id
    @GetMapping("/{id}")
    public String showUser(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }


    //страница редактирования пользователя
    @GetMapping("/editUser/{id}")
    public String editUser(Model model, @PathVariable("id") Integer id) {
        model.addAttribute("roles", roleService.getRoles());
        model.addAttribute("user", userService.getUserById(id));
        return "edit";
    }

    //    создание юзера
    @GetMapping("/new")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.getRoles());
        return "new";
    }

    @PostMapping("/new")
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
        if (userService.getUserByUsername(user.getUsername()) != null) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getRoles());
            model.addAttribute("usernameError", "Пользователь с таким именем существует!");
            return "new";
        } else {
            userService.saveUser(user);
            return "redirect:/admin";
        }
    }

    //страница обновления пользователя
    @PatchMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user,
                             @PathVariable("id") Integer id) {
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    //удаление пользователя
    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") Integer id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}
