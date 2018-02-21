package com.yf.springmvc.controller;

import org.junit.runner.RunWith;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comm")
public class CommController {

    @RequestMapping(value = "/getMsg")
    @ResponseBody
    public String getMsg(String name) {
        return "Hello, " + name;
    }
}
