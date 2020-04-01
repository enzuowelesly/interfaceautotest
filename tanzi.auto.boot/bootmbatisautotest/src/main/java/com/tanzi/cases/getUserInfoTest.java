package com.tanzi.cases;

import com.sun.org.apache.bcel.internal.generic.ATHROW;
import com.tanzi.config.TestConfig;
import com.tanzi.model.GetUserInfoCase;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class getUserInfoTest {
    @Test(dependsOnGroups = "loginTrue",description = "获取userid为1的用户信息")
    public void getUserInfo() throws IOException{
        SqlSession session = DatabaseUtil.getSqlSession();
        GetUserInfoCase getUserInfoCase =session.selectOne("getUserInfoCase",1);
        System.out.println(getUserInfoCase.toString());
        System.out.println(TestConfig.getUserInfoUrl);

        JSONArray resultjson =getJsonResult(getUserInfoCase);
        User user =session.selectOne(getUserInfoCase.getExpected(),getUserInfoCase);
        List userlist = new ArrayList();
        userlist.add(user);
        JSONArray jsonArray =new JSONArray(userlist);
        JSONArray jsonArray1 =new JSONArray(resultjson.getString(0));
        Assert.assertEquals(jsonArray.toString(),jsonArray1.toString());

    }

    private JSONArray getJsonResult(GetUserInfoCase getUserInfoCase) throws IOException {
        HttpPost post =new HttpPost(TestConfig.getUserInfoUrl);
        JSONObject param =new JSONObject();
        param.put("id",getUserInfoCase.getUserId());
        post.setHeader("content-type","application/json");
        StringEntity entity =new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        String result;
        HttpResponse response =TestConfig.httpClient.execute(post);
        result = EntityUtils.toString(response.getEntity(),"utf-8");
        List resultList = Arrays.asList(result);
        JSONArray array =new JSONArray(resultList);
        return array;

    }
}
