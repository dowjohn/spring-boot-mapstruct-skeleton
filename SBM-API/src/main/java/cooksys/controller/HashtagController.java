package cooksys.controller;

import cooksys.service.HashtagService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by student-2 on 3/21/2017.
 */
@RestController
@RequestMapping("tags")
@Api(tags = {"public", "hashtags"})
public class HashtagController {
    @Autowired
    private HashtagService hashtagService;


}
