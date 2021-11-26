package com.grimels.lazurcity.controller;

import com.grimels.lazurcityapi.controller.UserController;
import com.grimels.lazurcityapi.model.user.UserCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
public class UserControllerImpl implements UserController {
    private final UserCredentials userCredentials;

    @Override
    public void signin(UserCredentials request) {
        if (userCredentials.equals(request)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

}
