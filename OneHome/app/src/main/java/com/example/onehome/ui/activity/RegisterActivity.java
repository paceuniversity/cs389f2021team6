package com.example.onehome.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.onehome.R;
import com.example.onehome.UserInfoHolder;
import com.example.onehome.bean.User;
import com.example.onehome.biz.UserBiz;
import com.example.onehome.net.CommonCallback;
import com.example.onehome.utils.T;

import okhttp3.Call;


public class RegisterActivity extends BaseActivity {

    private EditText mEtUsername;
    private EditText mEtPassword;
    private EditText mEtRePassword;
    private Button mBtnRegister;
    private UserBiz mUserBiz = new UserBiz();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUpToolbar();
        initView();
        initEvent();
        setTitle("Sign up");

    }
    public void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.id_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void initView() {
        mEtUsername = (EditText) findViewById(R.id.id_et_username);
        mEtPassword = (EditText) findViewById(R.id.id_et_password);
        mEtRePassword = (EditText) findViewById(R.id.id_et_repassword);
        mBtnRegister = (Button) findViewById(R.id.id_btn_register);

    }

    private void initEvent() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();
                final String repassword = mEtRePassword.getText().toString();

                if(TextUtils.isEmpty(username) || TextUtils.isEmpty(password)){
                    T.showToast("username or password can't be empty");
                    return;
                }

                if(!password.equals(repassword)){
                    T.showToast("The passwords are not same");
                    return;
                }

                startLoadingProgress();

                mUserBiz.register(username, password, new CommonCallback<User>() {
                    @Override
                    public void onSuccess(User response) {
                        stopLoadingProgress();
                        T.showToast("sign up successful, Username is: "+response.getUsername());

                        LoginActivity.launch(RegisterActivity.this , response.getUsername(), response.getPassword());
                        finish();

                    }

                    @Override
                    public void onError(Exception e) {
                        stopLoadingProgress();
                        T.showToast(e.getMessage());
                    }

                });
            }
        });
    }
}