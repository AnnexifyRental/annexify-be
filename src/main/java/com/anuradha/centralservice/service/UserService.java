package com.anuradha.centralservice.service;

import com.anuradha.centralservice.dto.UserSaveDto;
import com.anuradha.centralservice.model.User;
import com.anuradha.centralservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public void saveUser(UserSaveDto userDto) {

        userRepository.findByUsernameOrEmail(userDto.username(), userDto.email())
                .ifPresentOrElse(
                        user -> log.info("User already exists: username : {} , email : {}", user.getUsername(), user.getEmail()),
                        () -> userRepository.save(toUser(userDto))
                );
    }

    private User toUser(UserSaveDto userDto) {
        return new User(
                userDto.username(),
                userDto.email()
        );
    }

}
