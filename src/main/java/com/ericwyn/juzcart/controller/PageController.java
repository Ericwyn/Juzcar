package com.ericwyn.juzcart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Test API Controller
 *
 * Created by Ericwyn on 18-11-22.
 */
@Controller
public class PageController {

    @RequestMapping(value = "/page1",method = RequestMethod.GET)
    public String api1(){
        return "index";
    }

}
