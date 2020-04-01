package com.tanzi.cases;

import com.tanzi.config.TestConfig;
import com.tanzi.model.InterfaceName;
import com.tanzi.model.LoginCase;
import com.tanzi.utils.ConfigFile;
import com.tanzi.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

public class LoginTest {
    @BeforeTest(groups = "loginTrue",dependsOnGroups = "测试预加载，准备client对象")
    public void beforeTest(){
        TestConfig.loginurl = ConfigFile.getUrl(InterfaceName.LOGIN);
        TestConfig.getUserInfoUrl = ConfigFile.getUrl(InterfaceName.GETUSERINFO);
        TestConfig.updateUserInfoUrl = ConfigFile.getUrl(InterfaceName.UPDATEUSERINFO);
        TestConfig.addUserUrl = ConfigFile.getUrl(InterfaceName.ADDUSERINFO);
        TestConfig.getUserListUrl = ConfigFile.getUrl(InterfaceName.GETUSERLIST);
        TestConfig.cookieStore= new BasicCookieStore();
        TestConfig.httpClient =HttpClientBuilder.create().setDefaultCookieStore(TestConfig.cookieStore).build();

    }
    @Test(groups = "loginTrue",description = "用户登录成功对象")
    public  void  loginTrue() throws  Exception{
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        LoginCase loginCase = sqlSession.selectOne("loginCase",1);
//        System.out.println(loginCase.toString());
//        System.out.println(TestConfig.loginurl);
//
        String result = getResult(loginCase);
        System.out.println(result);

        Assert.assertEquals(loginCase.getExpected(),result);


    }
    @Test(description = "用户登录失败对象")
    public  void  loginFalse() throws  Exception{
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        LoginCase loginCase = sqlSession.selectOne("loginCase",2);
//        System.out.println(loginCase.toString());
//        System.out.println(TestConfig.loginurl);

        String result = getResult(loginCase);
        Assert.assertEquals(loginCase.getExpected(),result);

    }
    private String getResult(LoginCase loginCase) throws IOException {
        HttpPost post =new HttpPost(TestConfig.loginurl);
        JSONObject param = new JSONObject();
        param.put("userName",loginCase.getUserName());
        param.put("password",loginCase.getPassword());
        StringEntity entity =new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        post.setHeader("Content-Type","application/json");
        String result;
        HttpResponse response =TestConfig.httpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        System.out.println(TestConfig.cookieStore);

        System.out.println(TestConfig.cookieStore.getCookies().get(0).getName());
        System.out.println(TestConfig.cookieStore.getCookies().get(0).getValue());
      return result;

    }
}
