package com.prade.parsestarterapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import static com.parse.ParseUser.logInInBackground;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {

    Boolean signupModeActive = true;
    EditText edTPassword;
    TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout layout = (LinearLayout)findViewById(R.id.layoutMain);
        ImageView image=(ImageView)findViewById(R.id.imgInsta);
         txtLogin = (TextView)findViewById(R.id.tvChangeSignUpText);
         edTPassword = (EditText) findViewById(R.id.editTxtPassword);

         edTPassword.setOnKeyListener(this);
         txtLogin.setOnClickListener(this);
         layout.setOnClickListener(this);
         image.setOnClickListener(this);

         if(ParseUser.getCurrentUser() !=null) // already user loggedin
         {
             showUserList();
         }

    }

    public void signup(View view) {
        EditText edTUsername = (EditText) findViewById(R.id.editTxtName);
         edTPassword = (EditText) findViewById(R.id.editTxtPassword);

        if (edTUsername.getText().toString().matches("") || edTPassword.getText().toString().matches("")) {
            Toast.makeText(this, "A username and password are required", Toast.LENGTH_SHORT).show();
        } else {
            if (signupModeActive) { // sign-up mode
                ParseUser user = new ParseUser();
                user.setUsername(edTUsername.getText().toString());
                user.setPassword(edTPassword.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(MainActivity.this, "Signup successful", Toast.LENGTH_SHORT).show();
                            showUserList();
                        } else {
                            Log.i("signup failed", e.getLocalizedMessage());
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                });
            } else // login mode
            {
                ParseUser.logInInBackground(edTUsername.getText().toString(), edTPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser user, ParseException e) {
                        if(user!=null){
                            showUserList();
                        }else {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }

        }

    }

    @Override
    public void onClick(View view) {
        Button signupButton = (Button) findViewById(R.id.buttonSignUp);

        if (view.getId() == R.id.tvChangeSignUpText) {

            if (signupModeActive) {
                signupModeActive = false;
                signupButton.setText("Login");
                txtLogin.setText("or, Signup");
            }
            else
            {
                signupModeActive = true;
                signupButton.setText("Signup");
                txtLogin.setText("or, Login ");
            }
        } else if(view.getId()==R.id.layoutMain || view.getId() == R.id.imgInsta) // Close the keyboard
        {
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
        }
    }


    //everytime key edit
    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        if(i==keyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            signup(view);
        }

        return false;
    }

    public void showUserList() {
        Intent intent = new Intent(getApplicationContext(),UserListActivity.class);
        startActivity(intent);
    }
}
