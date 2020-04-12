package com.example.zircon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import static android.view.KeyEvent.KEYCODE_ENTER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private EditText username , password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        password.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if(i == KeyEvent.KEYCODE_ENTER  && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                   onClick(findViewById(R.id.signIn));
                return false;
            }

        });
        findViewById(R.id.signuptxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainActivity.this , Sign_Up.class);
                clear();
                startActivity(it);
            }
        });
        getSupportActionBar().hide();
        if(ParseUser.getCurrentUser() != null) {
            Intent it = new Intent(MainActivity.this, loggedin.class);
            startActivity(it);
        }
    }
public void clear(){
        username.setText("");
        password.setText("");
}


public void click(View view) {
    try {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }catch (Exception e)
    {
        e.printStackTrace();
    }
}
    @Override
    public void onClick(View view) {
        String Username = this.username.getText().toString();
        String Password = this.password.getText().toString();
        ParseUser.logInInBackground(Username, Password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if(user !=null && e == null)
                {
                    Intent i = new Intent(MainActivity.this , loggedin.class);
                    clear();
                    startActivity(i);
                }
                else
                    FancyToast.makeText(MainActivity.this , e.getMessage() , Toast.LENGTH_SHORT , FancyToast.ERROR , false).show();

            }
        });

    }
}
