package com.example.zircon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.shashank.sony.fancytoastlib.FancyToast;

import org.w3c.dom.Text;

import java.util.List;

public class posts extends AppCompatActivity {
private LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);
        linearLayout = findViewById(R.id.linearlayout);
         final String s =getIntent().getStringExtra("username");
       // FancyToast.makeText(this , "Received"  +s  , Toast.LENGTH_SHORT , FancyToast.INFO,false).show();
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        ParseQuery<ParseObject> parseQuery = new ParseQuery<ParseObject>("Photo");
        parseQuery.whereEqualTo("username" , s);
        parseQuery.orderByDescending("createdAt");



        ProgressDialog progressDialog = new ProgressDialog(posts.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        parseQuery.findInBackground((objects, e) -> {
            if(objects.size() > 0 && e == null)
            {
                for(ParseObject parseObject : objects)
                {
                    TextView desc = new TextView(posts.this);
                    if (parseObject.get("Caption") != null) {
                        desc.setText(parseObject.get("Caption").toString());
                    } else {
                        desc.setText("");
                    }
                    ParseFile parseFile = (ParseFile) parseObject.get("Picture");
                    if(parseFile == null){posts.this.finish();}


                    parseFile.getDataInBackground((data, e1) -> {
                        if(data!=null && e1 == null)
                        {
                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
                            ImageView imageView = new ImageView(posts.this);


                            LinearLayout.LayoutParams params_image = new LinearLayout.LayoutParams( 750,500 );
                            params_image.setMargins(5 , 5 , 5 , 5);
                            imageView.setLayoutParams(params_image);
                            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                            imageView.setImageBitmap(bitmap);


                            LinearLayout.LayoutParams param_text_view = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT , ViewGroup.LayoutParams.WRAP_CONTENT);
                            param_text_view.setMargins(5 , 5 , 5 , 5);
                            desc.setLayoutParams(param_text_view);
                            desc.setGravity(Gravity.CENTER);
                            desc.setTextColor(Color.BLACK);
                            desc.setBackgroundColor(Color.BLUE);
                            desc.setTextSize(30f);


                            linearLayout.addView(imageView);
                            linearLayout.addView(desc);


                        }

                    });
                }
            }
            else
            {
                if (e!=null)
                FancyToast.makeText(posts.this , "Error Occured :" + e.getMessage() , Toast.LENGTH_SHORT ,FancyToast.ERROR , false).show();

                else if(objects.size() == 0)
                {
                    FancyToast.makeText(posts.this  , "No Posts to Display" , Toast.LENGTH_SHORT , FancyToast.INFO , false).show();
                }
                finish();
            }
            progressDialog.dismiss();
        });

    }
}
