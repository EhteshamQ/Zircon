package com.example.zircon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

public class Sign_Up extends AppCompatActivity implements View.OnClickListener{
private EditText username , userpassword , useremail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        username = findViewById(R.id.editText);
        useremail = findViewById(R.id.editText2);
        userpassword = findViewById(R.id.editText3);
        userpassword.setOnKeyListener(new View.OnKeyListener() {
                                          @Override
                                          public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                              if (i == KeyEvent.KEYCODE_ENTER && keyEvent.getAction() == KeyEvent.ACTION_DOWN)
                                                  onClick(findViewById(R.id.signup));
                                              return false;
                                          }
                                      }
            );

        if(ParseUser.getCurrentUser() != null)
        {
            Intent it = new Intent(Sign_Up.this , loggedin.class);
            startActivity(it);
        }
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
//Registering a new User
    @Override
    public void onClick(View view) {
        //checking if any edittext are null or empty
        if(useremail.getText().toString().equalsIgnoreCase("") ||  userpassword.getText().toString().equalsIgnoreCase("")
      ||  username.getText().toString().equalsIgnoreCase(""))
        {
            FancyToast.makeText(Sign_Up.this , "Failed to Create User:" + "Empty Fields" , Toast.LENGTH_SHORT ,
                    FancyToast.ERROR , false).show();
            return;
        }



        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(username.getText().toString());
        parseUser.setEmail(useremail.getText().toString());
        parseUser.setPassword(userpassword.getText().toString());

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up...");
        progressDialog.show();
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null)
                {
                    FancyToast.makeText(Sign_Up.this , "Successfully Created" , Toast.LENGTH_SHORT , FancyToast.SUCCESS , false).show();
                    finish();
                }
                else
                    FancyToast.makeText(Sign_Up.this , "Failed to Create User:" + e.getMessage() , Toast.LENGTH_SHORT ,
                            FancyToast.ERROR , false).show();
                progressDialog.dismiss();
            }
        });

    }
}
