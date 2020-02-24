package com.rdk.ridicariv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mFireBaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFireBaseAuth = FirebaseAuth.getInstance();
        final EditText edtxEmail = findViewById(R.id.email);
        final EditText edtxPassword = findViewById(R.id.password);
        final Button btnLogin = findViewById(R.id.logInButton);
        final Button btnRegister = findViewById(R.id.registerButton);
        final ProgressBar spinnerLoading = findViewById(R.id.spinner_loading);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtxEmail.getText().toString();
                String pass = edtxPassword.getText().toString();

                if (!(email.isEmpty()) && !(pass.isEmpty())){
                    spinnerLoading.setVisibility(ProgressBar.VISIBLE);
                    mFireBaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            spinnerLoading.setVisibility(ProgressBar.INVISIBLE);
                            if (!task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Sign Up Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(MainActivity.this, HomeActivity.class));

                            }
                        }
                    });
                } else if (email.isEmpty()) {
                    edtxEmail.setError("Please enter an E-mail");
                    edtxEmail.requestFocus();
                } else if (pass.isEmpty()) {
                    edtxPassword.setError("Please enter a Password");
                } else if (email.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

}
