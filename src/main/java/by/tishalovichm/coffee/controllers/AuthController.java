package by.tishalovichm.coffee.controllers;

import by.tishalovichm.coffee.dtos.in.UserDtoIn;
import by.tishalovichm.coffee.dtos.out.TokenDtoOut;
import by.tishalovichm.coffee.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("sign-up")
    public TokenDtoOut signUp(@RequestBody UserDtoIn userDtoIn) {
        return userService.signUp(userDtoIn);
    }

    @PostMapping("sign-in")
    public TokenDtoOut signIn(@RequestBody UserDtoIn userDtoIn) {
        return userService.signIn(userDtoIn);
    }
}
