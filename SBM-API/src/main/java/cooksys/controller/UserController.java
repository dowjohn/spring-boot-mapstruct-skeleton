package cooksys.controller;

import cooksys.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by student-2 on 3/21/2017.
 */
@RestController
@Validated
@RequestMapping("user")
@Api(tags = {"public", "users"})
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
}
