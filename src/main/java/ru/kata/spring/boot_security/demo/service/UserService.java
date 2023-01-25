package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUser(Integer id);

    void saveUser(User user);

    void updateUser(Integer id, User updatedUser);

    void deleteUser(User deleteUser);
}
