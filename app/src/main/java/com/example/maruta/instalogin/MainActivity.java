package com.example.maruta.instalogin;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    //Ui
    private ConstraintLayout background;
    private ImageView logoImage;
    private Button mainBtn, logInBtn;
    private EditText userField, passField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();
        mainBtn.setOnClickListener((View event) -> verifySignIn());
        logInBtn.setOnClickListener((View event) -> logIn());

        passField.setOnKeyListener(new EnterKey());

        background.setOnClickListener(new ClickAway());
        logoImage.setOnClickListener(new ClickAway());

        ParseUser currentUser = ParseUser.getCurrentUser();

        if(currentUser != null){

            Intent it = new Intent(MainActivity.this, InstaActivity.class);
            startActivity(it);
        }

        ParseAnalytics.trackAppOpenedInBackground(getIntent());
    }

    private void verifySignIn() {

        String username = userField.getText().toString();
        String password = passField.getText().toString();

        userField.setText("");
        passField.setText("");

        ParseUser tempUser = new ParseUser();

        tempUser.setUsername(username);
        tempUser.setPassword(password);

        tempUser.signUpInBackground(new SignUpCallback() {

            @Override
            public void done(ParseException e) {

                if(e == null){

                    Toast.makeText(MainActivity.this, "You signed up", Toast.LENGTH_SHORT).show();

                    Intent it = new Intent(MainActivity.this, InstaActivity.class);

                    it.putExtra("Username", username);
                    startActivity(it);

                } else {

                    Toast.makeText(MainActivity.this, "There already is a user with this username", Toast.LENGTH_SHORT).show();
                }

            }

        });
    }

    private void logIn() {

        String username = userField.getText().toString();
        String password = passField.getText().toString();

        ParseUser.logInInBackground(username, password, new LogInCallback() {

            @Override
            public void done(ParseUser user, ParseException e) {

                if(user != null && e == null){

                    Intent it = new Intent(MainActivity.this, InstaActivity.class);

                    it.putExtra("Username", username);
                    startActivity(it);

                }else {

                    Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initUi() {

        mainBtn = findViewById(R.id.signUpBtn);
        userField = findViewById(R.id.userField);
        passField = findViewById(R.id.passField);
        logInBtn = findViewById(R.id.logInBtn);
        background = findViewById(R.id.backGround);
        logoImage = findViewById(R.id.logoImage);

    }

    public class EnterKey implements View.OnKeyListener{

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {


            if(event.getAction() == KeyEvent.ACTION_DOWN){

                if(keyCode == KeyEvent.KEYCODE_ENTER){

                    logIn();
                }

            }

            return false;
        }
    }

    public class ClickAway implements View.OnClickListener{

        @Override
        public void onClick(View v) {

            InputMethodManager inp = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            inp.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }
}
