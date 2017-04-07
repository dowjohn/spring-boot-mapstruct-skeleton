package cooksys.controller;


import cooksys.dto.CredentialsDto;
import cooksys.service.ValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Validated
@RequestMapping("validate")
@Api(tags = {"public", "validate"})
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @RequestMapping(method = RequestMethod.GET, value = "/username/available/@{name}")
    @ApiOperation(value = "", nickname = "checkIfUsernameAvailable")
    public boolean userAvailable(@PathVariable String name, HttpServletResponse httpResponse) {
        return validationService.userAvailable(name);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/username/exists/@{name}")
    @ApiOperation(value = "", nickname = "checkIfUserExists")
    public boolean userExists(@PathVariable String name) {
        return validationService.userExists(name);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tag/exists/{label}")
    @ApiOperation(value = "", nickname = "checkIfTagExists")
    public boolean doesTagExist(@PathVariable String label) {
        return validationService.tagExists(label);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    @ApiOperation(value = "", nickname = "login")
    public boolean login(@RequestBody CredentialsDto creds, HttpServletResponse httpResponse) {
        httpResponse.setStatus(HttpServletResponse.SC_OK);
        return validationService.login(creds);
    }
}
