package com.solution.citylogia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.StorageService;
import com.solution.citylogia.network.api.IAuthorizationApi;
import com.solution.citylogia.network.api.IProfileApi;
import com.solution.citylogia.network.models.input.TokenResponse;
import com.solution.citylogia.network.models.output.LoginParameters;
import com.solution.citylogia.network.models.output.RegistrationParameters;
import com.solution.citylogia.services.ClientAuthData;
import com.solution.citylogia.services.Token;
import com.solution.citylogia.services.Tokens;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail;
    private EditText loginPassword;
    private ProgressBar progressBar;

    @Inject
    RetrofitSingleton retrofit;

    @Inject
    StorageService storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginEmail = findViewById(R.id.login_email);
        loginPassword = findViewById(R.id.login_password);

        Button register = findViewById(R.id.reg_btn);
        Button loginBtn = findViewById(R.id.login_btn);
        register.setOnClickListener(this);
        loginBtn.setOnClickListener(this);

        progressBar = findViewById(R.id.login_prb);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_btn:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.login_btn:
                loginUser();
                /*if (it is succes) {
                    startActivity(new Intent(this, MapActivity.class));
                } else {
                    Toast.makeText(this, "Возможно вы вели неправильные данные либо такого пользователя не существует", Toast.LENGTH_LONG).show();
                }*/
        }
    }

    private void loginUser() {
        String email = loginEmail.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();

        if (email.isEmpty()) {
            loginEmail.setError("Это обязательное поле!");
            loginEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            loginPassword.setError("Это обязательное поле!");
            loginPassword.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError("Неправильный формат почты!");
            loginEmail.requestFocus();
            return;
        }
        
        this.login(email, password);
        progressBar.setVisibility(View.VISIBLE);
    }

    @SuppressLint("CheckResult")
    private void login(String email, String password) {
        // Have fun
        IAuthorizationApi api = this.retrofit.getRetrofit().create(IAuthorizationApi.class);
        LoginParameters parameters = new LoginParameters(email, password);
        api.login(parameters)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    System.out.println(res);
                    TokenResponse access = res.getData().getAccess();
                    TokenResponse refresh = res.getData().getRefresh();
                    Token a = new Token(access.getToken(), access.getExpiry());
                    Token r = new Token(refresh.getToken(), refresh.getExpiry());
                    Tokens tokens = new Tokens(a, r);
                    ClientAuthData data = new ClientAuthData(false, tokens);
                    Gson gson = new Gson();
                    String jsonData = gson.toJson(data);
                    storage.putItem("STORAGE_TOKENS_KEY", jsonData);

                    startActivity(new Intent(this, MapActivity.class));
                    finish();
                });
    }

}