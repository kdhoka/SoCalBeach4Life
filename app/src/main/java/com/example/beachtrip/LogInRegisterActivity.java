package com.example.beachtrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class LogInRegisterActivity extends AppCompatActivity {
    private static final String TAG = "email password";
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String password;
    private TextView nameView;
    private TextView emailView;
    private TextView pwdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
        name = "";
        email = "";
        password = "";

        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.email);
        pwdView = findViewById(R.id.pwd);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            currentUser.reload();
        }
    }
    public void onClickBeachReview(android.view.View view){
        Intent intent = new Intent(this, BeachReviewActivity.class);
        startActivity(new Intent());
    }

    public void onClickBack(android.view.View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickNext(android.view.View view){

        name = nameView.getText().toString();
        email = emailView.getText().toString();
        password = pwdView.getText().toString();

        if (isValid(name, email, password)){
            signIn(email, password);
        }
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LogInRegisterActivity.this, "Success. User is signed in by 310",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInRegisterActivity.this, "Authentication failed. User not exist.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    private boolean isValid(String name, String email, String password) {
        if(name.isEmpty()){
            nameView.setError("Name is required!");
            return false;
        }

        if(email.isEmpty()){
            emailView.setError("Email is required!");
            return false;
        }

        if(password.isEmpty()){
            pwdView.setError("Password is required");
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailView.setError("Please provide valid email!");
            return false;
        }

        if(password.length() < 6){
            pwdView.setError("Password must have min length 6!");
            return false;
        }

        return true;
    }

    public void onClickReview(View view) {
        startActivity(new Intent(this, BeachReviewActivity.class));
    }
}