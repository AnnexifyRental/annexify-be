package com.anuradha.centralservice.repository;

import com.anuradha.centralservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

      Optional<User> findByUsernameOrEmail(String username, String email);

}
