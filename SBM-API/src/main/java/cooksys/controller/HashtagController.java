package cooksys.controller;

import cooksys.service.HashtagService;
import io.swagger.annotations.Api;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by student-2 on 3/21/2017.
 */
@RestController
@Validated
@RequestMapping("tags")
@Api(tags = {"public", "users"})
public class HashtagController {
    private HashtagService hashtagService;

    public HashtagController(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }
}
