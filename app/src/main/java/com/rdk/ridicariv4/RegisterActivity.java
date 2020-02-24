package com.rdk.ridicariv4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import io.opencensus.tags.Tag;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    public EditText edtxEmail, edtxPassword, edtxName, edtxCar;
    public Button btnRegister;
    FirebaseAuth mFireBaseAuth;
    FirebaseFirestore db;
    private static final String TAG = "RegisterActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFireBaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        edtxName = findViewById(R.id.personNameET);
        edtxCar = findViewById(R.id.carET);
        edtxEmail = findViewById(R.id.emailEt);
        edtxPassword = findViewById(R.id.pwdEt);
        btnRegister = findViewById(R.id.RegBtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String name = edtxName.getText().toString();
                final String car = edtxCar.getText().toString();
                final String email = edtxEmail.getText().toString();
                String pass = edtxPassword.getText().toString();

                if (email.isEmpty()) {
                    edtxEmail.setError("Please enter an E-mail");
                    edtxEmail.requestFocus();
                } else if (pass.isEmpty()) {
                    edtxPassword.setError("Please enter a Password");
                } else if (name.isEmpty()) {
                    edtxName.setError("Please enter your Name");
                } else if (car.isEmpty()) {
                    edtxCar.setError("Please enter your Car ID");
                } else if (email.isEmpty() && pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                } else if (!(email.isEmpty()) && !(pass.isEmpty())) {
                    mFireBaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                            } else {
                                // Create a new user with a first and last name
                                String uID = mFireBaseAuth.getCurrentUser().getUid().toString();

                                Map<String, Object> user = new HashMap<>();
                                user.put("name", name);
                                user.put("email", email);
                                user.put("carID", car);

                                db.collection("users").document(uID)
                                        .set(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.w(TAG, "Error writing document", e);
                                            }
                                        });


                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

                            }
                        }
                    });


                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
