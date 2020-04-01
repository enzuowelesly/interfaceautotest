package com.tanzi.controller;

import com.tanzi.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


@RestController
@Api(value = "v1")
@RequestMapping(value = "v1",name = "用户管理系统")
public class UserManager {
    @Autowired
    private SqlSessionTemplate template;
    @ApiOperation(value = "登录接口",httpMethod = "POST")
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Boolean login(HttpServletResponse response, @RequestBody User user){
        int i =template.selectOne("login",user);
        Cookie cookie =new Cookie("login","true");
        response.addCookie(cookie);
//        Log.info("查询到的结果是"+i);

        if (i==1){
//            Log.info("登录的用户是" + user.getUserName());
            return true;
        }
        return false;
    }
    @ApiOperation(value = "添加用户",httpMethod = "POST")
    @RequestMapping(value = "addUser",method = RequestMethod.POST)
    public boolean addUser(HttpServletRequest request,@RequestBody User user){
        Boolean x=verifyCookies(request);
        int result=0;
        if (x!=null){
            result =  template.insert("addUser",user);
        }
        if (result>0){
//            Log.info("添加用户的数量："+result);
            return true;
        }
        return false;
    }
    @ApiOperation(value = "获取用户信息列表",httpMethod = "POST")
    @RequestMapping(value = "getUserInfo",method = RequestMethod.POST)
    public List<User> getUserInfo(HttpServletRequest request,@RequestBody User user){
        Boolean x =verifyCookies(request);
        if (x==true){
            List<User> users=template.selectList("getUserInfo",user);
            return users;

        }else {
            return null;
        }
    }
    @ApiOperation(value = "更新用户信息",httpMethod = "POST")
    @RequestMapping(value = "updateUserInfo",method = RequestMethod.POST)
    public int updateUser(HttpServletRequest request ,@RequestBody User user){
        Boolean x =verifyCookies(request);
        int i =0;
        if (x==true) {
             i = template.update("updateUserInfo", user);
//            Log.info("更新用户数量为："+i);
        }
        return i;
    }

    private Boolean verifyCookies(HttpServletRequest request){
        Cookie[] cookies=request.getCookies();
        if (Objects.isNull(cookies)){
//            Log.info("cookies null");
            return false;
        }
        for (Cookie cookie:cookies){
            if (cookie.getName().equals("login")
                    &&cookie.getValue().equals("true")){
//                Log.info("cookie success");
                return true;
            }

        }
        return false;
    }
}
