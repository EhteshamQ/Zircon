package com.example.zircon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.List;

public class posts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
         final String s =getIntent().getStringExtra("username");
        FancyToast.makeText(this , "Received"  +s  , Toast.LENGTH_SHORT , FancyToast.INFO,false).show();
        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username" , s);
        parseQuery.orderByDescending("createdAt");

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        parseQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(objects.size() >0 && e == null)
                {
                    for(ParseObject parseObject : objects)
                    {
                        TextView desc = new TextView(posts.this);
                        desc.setText(parseObject.get("Caption") + "");

                    }
                }
            }
        });
    }
}
