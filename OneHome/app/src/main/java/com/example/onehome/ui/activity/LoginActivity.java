package com.example.onehome.ui.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.onehome.R;
import com.example.onehome.UserInfoHolder;
import com.example.onehome.bean.User;
import com.example.onehome.biz.UserBiz;
import com.example.onehome.net.CommonCallback;
import com.example.onehome.utils.T;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.CookieJarImpl;

import okhttp3.Call;

public class LoginActivity extends BaseActivity {

    private UserBiz mUserBiz = new UserBiz();

    private EditText mEtUsername;
    private EditText mEtPassword;
    private Button mBtLogin;
    private TextView mTvRegister;
    private UserBiz getmUserBiz = new UserBiz();

    private static final String KEY_USERNAME = "key_username";
    private static final String KEY_PASSWORD = "key_password";



    protected void onResume(){
        super.onResume();

       CookieJarImpl cookieJar = (CookieJarImpl) OkHttpUtils.getInstance().getOkHttpClient().cookieJar();
        cookieJar.getCookieStore().removeAll();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initView();
        
        initEvent();

        initIntent(getIntent());
    }

    private void initEvent() {
        
        mBtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("username or password can't be empty");
                    return;
                }

                startLoadingProgress();

                mUserBiz.login(username, password, new CommonCallback<User>() {
                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("login successful");
                        //keep user information
                        UserInfoHolder.getInstance().setUser(response);
                        //check login successful
                        toOrderActivity();
                    }

                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }


                });

            }
        });
        
        mTvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toRegisterActivity();
            }
        });
    }



    private void toRegisterActivity() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }



    private void initView() {
        mEtUsername =(EditText) findViewById(R.id.id_et_username);
        mEtPassword =(EditText) findViewById(R.id.id_et_password);
        mBtLogin =(Button) findViewById(R.id.id_btn_login);
        mTvRegister =(TextView) findViewById(R.id.id_tv_register);
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }

    private void initIntent(Intent intent) {
        if(intent == null){
            return;
        }
        String username = intent.getStringExtra(KEY_USERNAME);
        String password = intent.getStringExtra(KEY_PASSWORD);

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
            return;
        }

        mEtUsername.setText(username);
        mEtPassword.setText(password);
    }

    public static void launch(Context context, String username, String password) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(KEY_USERNAME, username);
        intent.putExtra(KEY_PASSWORD, password);
        context.startActivity(intent);
    }
}