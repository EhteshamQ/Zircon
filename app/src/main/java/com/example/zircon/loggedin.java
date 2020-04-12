package com.example.zircon;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class loggedin extends AppCompatActivity implements View.OnClickListener {
private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        textView = findViewById(R.id.textView);
        textView.setText(ParseUser.getCurrentUser().getUsername().toString() + ", Welcome . Click Me to Logout");
    }

    @Override
    public void onClick(View view) {
        ParseUser.logOutInBackground(new LogOutCallback() {
            @Override
            public void done(ParseException e) {
                finish();
            }
        });
    }
}
