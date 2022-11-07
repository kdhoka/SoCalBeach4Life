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
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
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
    private FirebaseDatabase root;
    private DatabaseReference Users;

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

        root = FirebaseDatabase.getInstance();
        Users = root.getReference("Users");

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
                            Toast.makeText(LogInRegisterActivity.this, "Success. User " + user.getEmail() +"is signed in.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LogInRegisterActivity.this, "User not exist or wrong credentials.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("Uid", user.getUid());
            startActivity(intent);
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

        //TODO: check pwd pattern

        return true;
    }

    public void onClickReview(View view) {
        startActivity(new Intent(this, BeachReviewActivity.class));
    }

    public void onClickRegister(View view) {
        name = nameView.getText().toString();
        email = emailView.getText().toString();
        password = pwdView.getText().toString();

        if (isValid(name, email, password)){
            Register(name, email, password);
        }
    }

    private void Register(String name, String email, String password) {
        final boolean[] isNotRegistered = {true};
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                           @Override
                                           public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                               isNotRegistered[0] = task.getResult().getSignInMethods().isEmpty();

                                               if (!isNotRegistered[0]){
                                                    Toast.makeText(LogInRegisterActivity.this, "User already exists!", Toast.LENGTH_SHORT).show();
                                               }
                                           }
                                       });

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                registerInDB(name, email, password, user.getUid());
                                Log.d(TAG, "createUserWithEmail:success. Automatically signed in.");
                                Toast.makeText(LogInRegisterActivity.this, "Success! Auto-sign in...",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(LogInRegisterActivity.this, "Registration failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


    }

    private void registerInDB(String name, String email, String password, String uid) {
        //create new user node in DB
        //create a new child node with register
        Users.child(uid).push();
        DatabaseReference user = Users.child(uid);
        //insert attributes name, email, password into the new child
        user.child("name").push();
        user.child("name").setValue(name);

        user.child("email").push();
        user.child("email").setValue(email);

        user.child("password").push();
        user.child("password").setValue(password);
    }
}