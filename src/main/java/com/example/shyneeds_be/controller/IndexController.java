package com.example.shyneeds_be.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class IndexController {

    @ApiOperation(value = "swagger test")
    @GetMapping("/index")
    public String index(){
        return "index";
    }
}
