package cooksys.controller;

import cooksys.dto.UserDto;
import cooksys.dto.UserDtoOutput;
import cooksys.entity.User;
import cooksys.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@Validated
@RequestMapping("users")
@Api(tags = {"public", "users"})
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @ApiOperation(value = "", nickname = "getAllUsers")
    public List<UserDtoOutput> index() {
        return userService.index();
    }

    @GetMapping("{id}")
    @ApiOperation(value = "", nickname = "getUser")
    public UserDtoOutput get(@PathVariable Long id) {
        return userService.get(id);
    }

    @PostMapping
    @ApiOperation(value = "", nickname = "createUser")
    public Long post(@RequestBody @Validated UserDto userDto, HttpServletResponse httpResponse) {
        Long id = userService.post(userDto);
        httpResponse.setStatus(HttpServletResponse.SC_CREATED);
        return id;
    }
}
