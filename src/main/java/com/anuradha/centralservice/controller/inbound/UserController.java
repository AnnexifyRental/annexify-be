package com.anuradha.centralservice.controller.inbound;

import com.anuradha.centralservice.dto.UserSaveDto;
import com.anuradha.centralservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public void saveUser(@RequestBody UserSaveDto userDto) {
        userService.saveUser(userDto);
    }

}
