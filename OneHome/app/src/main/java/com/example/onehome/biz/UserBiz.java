package com.example.onehome.biz;

import com.example.onehome.bean.User;
import com.example.onehome.config.Config;
import com.example.onehome.net.CommonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

public class UserBiz {

    public void login(String username, String password, CommonCallback<User> commonCallback){
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_login")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

    public void register(String username, String password, CommonCallback<User> commonCallback){
        OkHttpUtils
                .post()
                .url(Config.baseUrl + "user_register")
                .tag(this)
                .addParams("username", username)
                .addParams("password", password)
                .build()
                .execute(commonCallback);
    }

}
