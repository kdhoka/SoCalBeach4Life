package com.example.beachtrip;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class Login_Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);
    }

    private void onClickLog(android.view.View view){
        String name = findViewById(R.id.name).toString();
        String email = findViewById(R.id.email).toString();
        String password = findViewById(R.id.password).toString();

        String msg = "";
        if (name.equals("")){
            msg = "empty name. Please enter again.";
        } else if (email.equals("")){
            msg = "empty email. Please enter again.";
        } else if (password.equals("")){
            msg = "empty password. Please enter again.";
        }

        if (!isValid(password)){
            msg = "Invalid password. Password must be at least 8 characters and contains at least 1 number, 1 alphabet, and 1 special character in [! * .]";
        }
    }

    //implements non-functional requirement 2
    private boolean isValid(String pwd){
        boolean res = pwd.length() >= 8;
        if (res){
            boolean hasNum  = false;
            boolean hasAlpha = false;
            boolean hasSpecialChar = false;
            int i = 0;
            while(!(hasNum && hasAlpha && hasSpecialChar) && i < pwd.length()){
                    i++;
            }
        }
        //TODO:
        return false;
    }
    public void onClickReg(android.view.View view){
        String name = findViewById(R.id.name).toString();
        String email = findViewById(R.id.email).toString();
        String password = findViewById(R.id.password).toString();
        Review[] reviews = new Review[0];

        User u = new User(name, email, password, reviews);
        String reg_msg = toRegister(u);
        if (reg_msg.equals("success")){
            //redirect to homepage with log in status
        } else {
            //display msg on current page
        }
    }

    private String toLog(User u){
        User dbU = getUser(u.getEmail());
        //if dbu is null
        //u is not even register
            //return "user not register"
        //else
            //do further checking
        //if name & password match
            //return "success"
        //else
            //return "name or password wrong'
        return "null";//TODO: change to reflect correct status
    }

    private String toRegister(User u){
        return "null";//TODO: change to reflect correct status
    }

    private User getUser(String email){
        return null;
    }

}