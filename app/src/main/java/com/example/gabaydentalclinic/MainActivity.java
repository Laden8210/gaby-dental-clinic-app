package com.example.gabaydentalclinic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gabaydentalclinic.model.LoginResponse;
import com.example.gabaydentalclinic.repository.LoginUserRepository;
import com.example.gabaydentalclinic.service.ILoginUserService;
import com.example.gabaydentalclinic.util.SessionManager;
import com.example.gabaydentalclinic.view.HeroActivity;
import com.example.gabaydentalclinic.view.RegisterActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private MaterialButton btnLogin;
    private TextView tvRegister;
    private TextInputLayout tEmailPhone, tPassword;
    private SessionManager sessionManager;
    private ILoginUserService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        if (SessionManager.getInstance(this).getUserId() != null) {
            startActivity(new Intent(MainActivity.this, HeroActivity.class));
            finish();
        }

        apiService = LoginUserRepository.getClient().create(ILoginUserService.class);

        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tEmailPhone = findViewById(R.id.tEmailPhone);
        tPassword = findViewById(R.id.tPassword);

        tvRegister.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        btnLogin.setOnClickListener(v -> {
            String emailOrPhone = tEmailPhone.getEditText().getText().toString().trim();
            String password = tPassword.getEditText().getText().toString().trim();

            if (emailOrPhone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email/phone and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(emailOrPhone, password);
            }
        });
    }

    private void loginUser(String emailOrPhone, String password) {
        Call<LoginResponse> call = apiService.loginUser(emailOrPhone, password);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d("LoginResponse", "onResponse: " + response.body());
                if (!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Login failed: " + response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginResponse loginResponse = response.body();
                if (loginResponse == null) {
                    Toast.makeText(MainActivity.this, "Server returned an empty response.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if ("success".equals(loginResponse.getStatus())) {
                    sessionManager.saveUserId(loginResponse.getUserId());
                    startActivity(new Intent(MainActivity.this, HeroActivity.class));
                    finish();
                } else {
                    Toast.makeText(MainActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginError", "onFailure: " + t.getMessage(), t);
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show()
                );
            }
        });
    }
}
