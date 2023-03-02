package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(Integer id);

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserById(Integer id);

}
