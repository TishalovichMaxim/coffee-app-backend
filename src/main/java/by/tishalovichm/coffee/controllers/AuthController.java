package by.tishalovichm.coffee.controllers;

import by.tishalovichm.coffee.dtos.in.UserDtoIn;
import by.tishalovichm.coffee.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("sign-up")
    public ResponseEntity<String> signUp(@RequestBody UserDtoIn userDtoIn) {
        userService.createNewUser(userDtoIn);
        return ResponseEntity.ok("Success");
    }
}
