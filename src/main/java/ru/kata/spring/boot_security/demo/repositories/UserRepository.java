package ru.kata.spring.boot_security.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    User findUserByEmail(@Param("email") String email);

    User findUserById(@Param("id") Integer id);
}
