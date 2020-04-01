package com.tanzi.cases;

import com.mysql.cj.xdevapi.JsonArray;
import com.tanzi.config.TestConfig;
import com.tanzi.model.GetUserListCase;
import com.tanzi.model.User;
import com.tanzi.utils.DatabaseUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.ibatis.session.SqlSession;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

public class getUserListInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获得性别为男的用户")
    public void getUserListInfo() throws IOException {
        SqlSession sqlSession = DatabaseUtil.getSqlSession();
        GetUserListCase getUserListCase =sqlSession.selectOne("getUserListCase",1);
        System.out.println(getUserListCase.toString());
        System.out.println(TestConfig.getUserListUrl);

        JSONArray resultJson =getJsonResult(getUserListCase);

        List<User> userList =sqlSession.selectList(getUserListCase.getExpected(),getUserListCase);

        for (User u:userList){
            System.out.println("获取到的用户是"+u.toString());
        }
        JSONArray userListjson = new JSONArray(userList);
        Assert.assertEquals(userListjson.length(),resultJson.length());
        for (int i=0;i<resultJson.length();i++){
            JSONObject expect =(JSONObject) resultJson.get(i);
            JSONObject actual =(JSONObject) userListjson.get(i);
            Assert.assertEquals(expect.toString(),actual.toString());
        }


    }

    private JSONArray getJsonResult(GetUserListCase getUserListCase) throws IOException {
        HttpPost post =new HttpPost(TestConfig.getUserListUrl);
        JSONObject param =new JSONObject();
        param.put("useramne",getUserListCase.getUserName());
        param.put("sex",getUserListCase.getSex());
        param.put("age",getUserListCase.getAge());
        post.setHeader("content-type","application/json");
        StringEntity entity =new StringEntity(param.toString());
        post.setEntity(entity);
        String result;
        HttpResponse response =TestConfig.httpClient.execute(post);
        result = EntityUtils.toString(response.getEntity());
        JSONArray jsonArray  =new JSONArray(result);
        return jsonArray;
    }
}
