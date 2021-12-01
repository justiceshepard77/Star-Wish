package com.example.star_wish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


    }

    public void register_user(View view) {
        // Get data
        Log.v("starwish","Pressed register button.");
        EditText txtName = findViewById(R.id.name);
        EditText txtBday = findViewById(R.id.birthday);
        EditText txtEmail = findViewById(R.id.emailText);
        EditText txtPassword = findViewById(R.id.passwordText);
        EditText txtConfirmPassword = findViewById(R.id.confirmPassword);

        String name = txtName.getText().toString();
        String bday = txtBday.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String confirmPassword = txtConfirmPassword.getText().toString();
        // Check parameters
        if (name.matches("")) {
            Toast.makeText(this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (bday.matches("")) {
            Toast.makeText(this, "Please enter your birthday", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.matches("")) {
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.matches("")) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (confirmPassword.matches("")) {
            Toast.makeText(this, "Please enter your confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.matches(emailPattern))
        {
            Toast.makeText(getApplicationContext(),"Please enter a valid email address",Toast.LENGTH_SHORT).show();
            return;
        }
        if (!password.equals(confirmPassword)) {
            // Passwords do not match! Show error and return
            Toast.makeText(this, "Your passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create User
        mAuth = FirebaseAuth.getInstance();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String uid = user.getUid();

                    addUserInfoToDB(uid, name,email,bday);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addUserInfoToDB(String uid, String name, String bday, String email) {


        HashMap<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("email", email);
        map.put("bday", bday);

        FirebaseDatabase.getInstance().getReference().child("users").child(uid).updateChildren(map);

        Toast.makeText(SignUpActivity.this, "Successfully " +
                "Created Account!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, MainScreen.class);
        startActivity(intent);
        finish();
    }
}