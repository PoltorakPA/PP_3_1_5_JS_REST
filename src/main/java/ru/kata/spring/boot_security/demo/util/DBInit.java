package ru.kata.spring.boot_security.demo.util;

import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;

@Component
public class DBInit {

    private final UserService userService;
    private final RoleService roleService;

    public DBInit(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @PostConstruct
    public void initDB() {
        Role roleAdmin = new Role(1, "ROLE_ADMIN");
        Role roleUser = new Role(2, "ROLE_USER");
        Set<Role> adminSet = new HashSet<>();
        Set<Role> userSet = new HashSet<>();

        roleService.addRole(roleAdmin);
        roleService.addRole(roleUser);

        adminSet.add(roleAdmin);
        adminSet.add(roleUser);
        userSet.add(roleUser);

        User user = new User("Admin", "Pavel", "Poltorak", 36, "pavel@mail.ru", "123", adminSet);
        user.setId(1);
        User user1 = new User("Oleg", "Oleg", "Bochkov", 40, "oleg@mail.ru", "123", userSet);
        user1.setId(2);
        User user2 = new User("Ilnur", "Ilnur", "Batruha", 41, "ilnur@mail.ru", "123", userSet);
        user2.setId(3);
        User user3 = new User("qwerty", "qwerty", "qwerty", 37, "qwerty@mail.ru", "123", userSet);
        user3.setId(4);

        userService.saveUser(user);
        userService.saveUser(user1);
        userService.saveUser(user2);
        userService.saveUser(user3);
    }
}
