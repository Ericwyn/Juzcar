package com.ericwyn.juzcart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping(value = "/api2", method = RequestMethod.GET)
    public String api2(@RequestParam(value = "p1",required = false) String p1,
                       @RequestParam(value = "p2") String p2){
        return "HelloWorld";
    }

    @PostMapping(value = "/api3")
    public String api3(@RequestParam("p1") Integer p1,
                       @RequestParam("p2") String p2){
        return "HelloWorld";
    }

    @GetMapping(value = "/api4")
    public String api4(@RequestParam("p1") Integer p1,
                       @RequestParam("p2") String p2){
        return "HelloWorld";
    }

}
