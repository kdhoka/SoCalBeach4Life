package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LogInRegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String pwd;
    private TextView nameView;
    private TextView emailView;
    private TextView pwdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        mAuth = FirebaseAuth.getInstance();
        name = "";
        email = "";
        pwd = "";

        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.email);
        pwdView = findViewById(R.id.pwd);
    }

    public void onClickBack(android.view.View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickNext(android.view.View view){

        name = nameView.getText().toString();
        email = emailView.getText().toString();
        pwd = pwdView.getText().toString();

        if (name.isEmpty()) {
            nameView.setError("Name is required!");
            return;
        }

        if(email.isEmpty()){
            emailView.setError("Email is required!");
            return;
        }

        if(pwd.isEmpty()){
            pwdView.setError("Password is required!");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailView.setError("Please provide valid email");
            return;
        }

        if(pwd.length() < 8){
            pwdView.setError("Password must be at least 8 characters long!");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name, email, pwd);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(LogInRegisterActivity.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        //user is registered, call log in, redirect to main page.
                                    }else{
                                        Toast.makeText(LogInRegisterActivity.this, "Failed to register! Try again!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                });
//        Intent intent = new Intent(this, UserReviewActivity.class);
//        startActivity(intent);//send current user key to next page:D That's how page and page communicate!
    }

}