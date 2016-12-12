package com.yzy.controller;

import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;


/**
 * Created by hasee on 2016/11/7.
 */
@RestController
@RequestMapping("user")
@Api(tags = {"user"}, description = "测试用控制器2",produces = "application/json,application/xml",consumes = "application/json,application/xml",protocols = "HTTP")//定义控制器类信息

//新标签，自定义swagger配置模板
//@SwaggerDefinition(
//        info = @Info(
//                description = "Gets the weather",
//                version = "V12.0.12",
//                title = "The Weather API",
//                termsOfService = "http://localhost:8080/dist/index.html",
//                contact = @Contact(
//                        name = "Rain Moore",
//                        email = "rain.moore@theweatherapi.io",
//                        url = "http://theweatherapi.io"
//                ),
//                license = @License(
//                        name = "Apache 2.0",
//                        url = "http://www.apache.org/licenses/LICENSE-2.0"
//                )
//        ),
//        consumes = {"application/json", "application/xml"},
//        produces = {"application/json", "application/xml"},
//        schemes = {SwaggerDefinition.Scheme.HTTP, SwaggerDefinition.Scheme.HTTPS},
//        tags = {
//                @Tag(name = "Private", description = "Tag used to denote operations as private")
//        },
//        externalDocs = @ExternalDocs(value = "Meteorology", url = "http://theweatherapi.io/meteorology.html")
//)

public class MyController2 {
        static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

        @ApiOperation(value = "获取用户列表", notes = "",produces = "application/json,appication/xml",response = User.class,responseContainer = "List")//单次操作信息注释
        @RequestMapping(value = "UserList", method = RequestMethod.GET)
        //响应信息内容自定义
        @ApiResponses(value={
                @ApiResponse(code = 200,message = "test response information"),
                @ApiResponse(code = 202,message = "test response information2")
        })
        public List<User> getUserList() {
            List<User> r = new ArrayList<User>(users.values());
            return r;
        }

        @ApiOperation(value = "创建用户", notes = "根据User对象创建用户",response = User.class)
        @ApiImplicitParam(name = "user", value = "用户详细实体user", required = true ,dataType = "User",paramType = "body")//参数信息注释
        @RequestMapping(value = "CreateUser", method = RequestMethod.POST)
        public User postUser(@RequestBody User user) {
            users.put((long) user.getId(), user);
            return users.get(user.getId());
        }

        @ApiOperation(value = "获取用户详细信息", notes = "根据url的id来获取用户详细信息")
        //@ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int")
        @RequestMapping(value = "UserInfo/{id}", method = RequestMethod.GET)
        public User getUser(@ApiParam(name = "long id",value = "用户ID",defaultValue = "1")@PathVariable long id) {
            return users.get(id);
        }

        @ApiOperation(value = "更新用户详细信息", notes = "根据url的id来指定更新对象，并根据传过来的user信息来更新用户详细信息",consumes = "application/xml")
        @ApiImplicitParams({ @ApiImplicitParam(name = "id", value = "整数型Id", required = true, dataType = "int",paramType = "path")
                 })
        @RequestMapping(value = "UpdataUser/{id}", method = RequestMethod.PUT)
        public String putUser(@PathVariable long id, @RequestBody User user) {
            User u = users.get(id);
            u.setName(user.getName());
            u.setId(user.getId());
            users.put(id, u);
            return "success";
        }

        @ApiOperation(value = "删除用户", notes = "根据url的id来指定删除对象")
        @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "int")
        @RequestMapping(value = "DeleteUser/{id}", method = RequestMethod.DELETE)
        public String deleteUser(@PathVariable long id) {
            users.remove(id);
            return "success";
        }

    }
