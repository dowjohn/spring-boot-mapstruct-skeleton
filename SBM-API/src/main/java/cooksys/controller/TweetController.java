package cooksys.controller;

import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by student-2 on 3/21/2017.
 */
@RestController
@Validated
@RequestMapping("tweets")
@Api(tags = {"public", "users"})
public class TweetController {
}
