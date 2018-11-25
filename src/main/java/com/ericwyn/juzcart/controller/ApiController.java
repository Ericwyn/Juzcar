package com.ericwyn.juzcart.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * Test API Controller
 *
 * Created by Ericwyn on 18-11-22.
 */
@RestController
public class ApiController {

    @RequestMapping(value = "/api1",method = RequestMethod.POST)
    public String api1(){
        return "Hello World";
    }

}
