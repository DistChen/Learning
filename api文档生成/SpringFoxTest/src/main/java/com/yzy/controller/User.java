package com.yzy.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by hasee on 2016/11/10.
 */

//@EnableSwagger2
@ApiModel(value = "Super")
public class User {

    String name;

    @ApiModelProperty(value = "用户ID",allowableValues = "[1,3]",required = true)
    long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
