package com.example.comment_mb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout inputEmail, inputPassword;
    Button btnLogin;
    ProgressDialog mLoadingBar;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword= findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btn_login);
        mLoadingBar= new ProgressDialog(this);
        mAuth= FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtamptLogin();
            }
        });

    }

    private void AtamptLogin() {
        String email= inputEmail.getEditText().getText().toString();
        String password = inputPassword.getEditText().getText().toString();
        mLoadingBar.setTitle("Loggin in");
        mLoadingBar.setCanceledOnTouchOutside(false);
        mLoadingBar.show();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    mLoadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Loggin is Succesfull", Toast.LENGTH_SHORT).show();
                    Intent intent= new Intent(LoginActivity.this,CommentPost.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
                else {
                    mLoadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Boo shit", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}