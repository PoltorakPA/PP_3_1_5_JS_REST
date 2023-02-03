package ru.kata.spring.boot_security.demo.service;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.security.UsersDetails;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Integer id) {//+
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Override
    public User getUserByUsername(String username) {//+
        return userRepository.findUserByUsername(username);
    }

    @Override
    @Transactional
    public void saveUser(User user) {//+
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUser(Integer id, User updatedUser) {//+
        User userToUpdate = userRepository.getById(id);
        userToUpdate.setUsername(updatedUser.getUsername());
        userToUpdate.setName(updatedUser.getName());
        userToUpdate.setLastname(updatedUser.getLastname());
        userToUpdate.setPassword(updatedUser.getPassword());
        userToUpdate.setAge(updatedUser.getAge());
        userToUpdate.setEmail(updatedUser.getEmail());
        userToUpdate.setRoles(updatedUser.getRoles());
        userRepository.save(updatedUser);
    }

    @Override
    @Transactional
    public void deleteUserById(Integer id) {//+
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else throw new UsernameNotFoundException("User not found!");
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findUserByUsername(username));
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UsersDetails(user.get());
    }
}
