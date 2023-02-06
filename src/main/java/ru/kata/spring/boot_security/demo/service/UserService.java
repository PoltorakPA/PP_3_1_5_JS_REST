package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> getAllUsers();

    User getUserById(Integer id);

    User getUserByUsername(String username);

    void saveUser(User user);

    void updateUser(String username, User updatedUser);

    void deleteUserById(Integer id);

}
