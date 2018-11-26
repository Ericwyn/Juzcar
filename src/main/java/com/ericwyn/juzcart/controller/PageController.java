package com.ericwyn.juzcart.controller;

import com.ericwyn.juzcar.scan.annotations.JuzcarIgnoreScanner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * Test API Controller
 *
 * Created by Ericwyn on 18-11-22.
 */
@Controller
@JuzcarIgnoreScanner
public class PageController {

    @RequestMapping(value = "/page1",method = RequestMethod.GET)
    public String api1(){
        return "index";
    }

}
