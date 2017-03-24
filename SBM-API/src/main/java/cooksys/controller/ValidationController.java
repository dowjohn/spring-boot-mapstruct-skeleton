package cooksys.controller;


import cooksys.service.ValidationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@Validated
@RequestMapping("validate")
@Api(tags = {"public", "validate"})
public class ValidationController {

    @Autowired
    private ValidationService validationService;

    @RequestMapping(method = RequestMethod.GET, value = "/username/available/{name}")
    @ApiOperation(value = "", nickname = "checkIfUsernameAvailable")
    public boolean userAvailable(@PathVariable String name, HttpServletResponse httpResponse) {
        return validationService.userAvailable(name.substring(1));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/username/exists/{name}")
    @ApiOperation(value = "", nickname = "checkIfUserExists")
    public boolean userExists(@PathVariable String name) {
        return validationService.userExists(name.substring(1));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/tag/exists/{label}")
    @ApiOperation(value = "", nickname = "checkIfTagExists")
    public boolean doesTagExist(@PathVariable String label) {
        return validationService.tagExists(label.substring(1));
    }
}
