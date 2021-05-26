 package com.solution.citylogia;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.solution.citylogia.network.RetrofitSingleton;
import com.solution.citylogia.network.StorageService;
import com.solution.citylogia.network.api.IAuthorizationApi;
import com.solution.citylogia.network.models.input.TokenResponse;
import com.solution.citylogia.network.models.output.RegistrationParameters;
import com.solution.citylogia.services.ClientAuthData;
import com.solution.citylogia.services.Token;
import com.solution.citylogia.services.Tokens;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

     EditText reg_name;
     EditText reg_email;
     EditText reg_password;

     Button reg_sign_in;
     Button reg_create;

     ProgressBar progressBar;

     @Inject
     RetrofitSingleton retrofit;
     @Inject
        StorageService storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_name = findViewById(R.id.reg_name);
        reg_email = findViewById(R.id.reg_email);
        reg_password = findViewById(R.id.reg_password);

        reg_sign_in = findViewById(R.id.reg_sign_in);
        reg_sign_in.setOnClickListener(this);
        reg_create = findViewById(R.id.reg_create);
        reg_create.setOnClickListener(this);

        progressBar = findViewById(R.id.reg_pbar);
    }

     @SuppressLint("NonConstantResourceId")
     @Override
     public void onClick(View v) {
         switch (v.getId()) {
             case R.id.reg_sign_in:
                 startActivity(new Intent(this, LoginActivity.class));
                 break;
             case R.id.reg_create:
                 registerUser();
                 break;
         }
     }

     private void registerUser() {
        String name = reg_name.getText().toString().trim();
        String email = reg_email.getText().toString().trim();
        String password = reg_password.getText().toString().trim();

        if (name.isEmpty()) {
            reg_name.setError("Это обязательное поле!");
            reg_name.requestFocus();
            return;
        }

         if (email.isEmpty()) {
             reg_email.setError("Это обязательное поле!");
             reg_email.requestFocus();
             return;
         }

         if (password.isEmpty()) {
             reg_password.setError("Это обязательное поле!");
             reg_password.requestFocus();
             return;
         }

         if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
             reg_email.setError("Неправильный формат почты!");
             reg_email.requestFocus();
             return;
         }

         if (password.length() < 6) {
             reg_password.setError("Длина пароля не менее 6-ти символов");
             reg_password.requestFocus();
             return;
         }

         // здесь метод, который отправит данные в базу данных
         // далее перенаправление на Activity log-in или Profile
        this.register(name, email, password);
         progressBar.setVisibility(View.VISIBLE);
     }

     private void register(String name, String email, String password) {
         RegistrationParameters registerParameter = new RegistrationParameters(name, email, password);
         IAuthorizationApi api = retrofit.getRetrofit().create(IAuthorizationApi.class);
         api.register(registerParameter)
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