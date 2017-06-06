package com.mzhang.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/validation")
    public String validation() {
        return "validation";
    }

    @GetMapping("/display")
    public String display() {
        return "display";
    }
}