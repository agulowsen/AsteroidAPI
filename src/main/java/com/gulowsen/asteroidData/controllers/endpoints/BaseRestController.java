package com.gulowsen.asteroidData.controllers.endpoints;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Functional APIs")
@RestController
@RequestMapping("/")
public class BaseRestController {

    private final String VERSION_PATH = "/version";

    @Value("${build.version}")
    private String version;

    @ApiOperation(value = "Health endpoint")
    @GetMapping("/")
    public String health() {
        return "OK";
    }

    @ApiOperation(value = "Returns the current build version")
    @GetMapping(VERSION_PATH)
    public String version() {
        return version;
    }



}
