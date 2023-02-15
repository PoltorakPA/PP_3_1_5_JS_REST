package ru.kata.spring.boot_security.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DBInit {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public DBInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initDB() {
        Role roleAdmin = new Role(1, "ROLE_ADMIN");
        Role roleUser = new Role(2, "ROLE_USER");
        List<Role> adminSet = new ArrayList<>();
       List<Role> userSet = new ArrayList<>();

        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        adminSet.add(roleAdmin);
        adminSet.add(roleUser);
        userSet.add(roleUser);

        User admin = new User(1, "admin", "Pavel", "Poltorak", 36,
                "pavel@mail.ru", "123", adminSet);
        admin.setId(1);
        User user = new User(2, "user",
                "Oleg", "Bochkov", 40,
                "oleg@mail.ru", "123", userSet);
        user.setId(2);
        User user1 = new User(3, "ilnur",
                "ilnur", "batruha", 40,
                "ilnur@mail.ru", "123", userSet);
        user.setId(2);
        userService.saveUser(admin);
        userService.saveUser(user);
        userService.saveUser(user1);

    }


}
