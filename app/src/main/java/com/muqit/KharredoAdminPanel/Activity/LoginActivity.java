package com.muqit.KharredoAdminPanel.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.muqit.KharredoAdminPanel.API.RetrofitAPI;
import com.muqit.KharredoAdminPanel.API.RetrofitClient;
import com.muqit.KharredoAdminPanel.Models.LoginResponse;
import com.muqit.KharredoAdminPanel.R;
import com.muqit.KharredoAdminPanel.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    EditText mPassword;
    MaterialCheckBox mRememberChkBox;
    EditText mUsername;
    ProgressDialog progressDialog;
    Retrofit retrofit;
    SessionManager sessionManager;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_login);
        SessionManager sessionManager2 = new SessionManager(this);
        String str = "";
        if (!sessionManager2.getStringData(NotificationCompat.CATEGORY_EMAIL).equals(str) && !sessionManager2.getStringData("password").equals(str)) {
            startActivity(new Intent(this, MainAdminActivity.class));
            finish();
        }
        this.mUsername = (EditText) findViewById(R.id.ed_username);
        this.mPassword = (EditText) findViewById(R.id.ed_password);
        this.mRememberChkBox = (MaterialCheckBox) findViewById(R.id.chk_remember);
        ProgressDialog progressDialog2 = new ProgressDialog(this);
        this.progressDialog = progressDialog2;
        progressDialog2.setProgressStyle(0);
        this.progressDialog.setMessage("Logging In......");
        this.progressDialog.setCancelable(false);
        this.retrofit = new RetrofitClient().getRetrofitClient();
    }

    public boolean validation() {
        if (this.mUsername.getText().toString().isEmpty()) {
            this.mUsername.setError("Enter Email");
            return false;
        } else if (this.mPassword.getText().toString().isEmpty()) {
            this.mPassword.setError("Enter Password");
            return false;
        } else if (validEmail(this.mUsername.getText().toString())) {
            return true;
        } else {
            this.mUsername.setError("Enter Valid Email");
            return false;
        }
    }

    public void LoginBTN(View view) {
        if (validation()) {
            this.progressDialog.show();
            ((RetrofitAPI) this.retrofit.create(RetrofitAPI.class)).Login(this.mUsername.getText().toString().trim(), this.mPassword.getText().toString().trim()).enqueue(new Callback<LoginResponse>() {
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    LoginResponse loginResponse = (LoginResponse) response.body();
                    if (loginResponse == null) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Invalid Email or Password.....", 1).show();
                        LoginActivity.this.progressDialog.dismiss();
                    } else if (loginResponse.getStatus().equals("success") && loginResponse.getMessage().equals("User found.")) {
                        Toast.makeText(LoginActivity.this.getApplicationContext(), "Login Successfully", 1).show();
                        Intent intent = new Intent(LoginActivity.this.getApplicationContext(), MainAdminActivity.class);
                        String trim = LoginActivity.this.mUsername.getText().toString().trim();
                        String str = NotificationCompat.CATEGORY_EMAIL;
                        intent.putExtra(str, trim);
                        if (LoginActivity.this.mRememberChkBox.isChecked()) {
                            LoginActivity.this.sessionManager = new SessionManager(LoginActivity.this.getApplicationContext());
                            LoginActivity.this.sessionManager.setStringData(str, LoginActivity.this.mUsername.getText().toString().trim());
                            LoginActivity.this.sessionManager.setStringData("password", LoginActivity.this.mPassword.getText().toString().trim());
                        }
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.progressDialog.dismiss();
                        LoginActivity.this.finish();
                    }
                }

                public void onFailure(Call<LoginResponse> call, Throwable th) {
                    Toast.makeText(LoginActivity.this.getApplicationContext(), "Something Went Wrong......", 1).show();
                    LoginActivity.this.progressDialog.dismiss();
                }
            });
        }
    }

    private boolean validEmail(String str) {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches();
    }
}
