package com.udacity.jwdnd.course1.cloudstorage.services;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class GetUserIdService {
    private UserService userService;

    public GetUserIdService(UserService userService) {
        this.userService = userService;
    }

    public Integer getUserId(Authentication authentication) {
        String username = authentication.getName();
        Integer userId = this.userService.getUserId(username);

        return userId;
    }
}
