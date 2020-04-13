package com.example.zircon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;

public class loggedin extends AppCompatActivity {

private Toolbar toolbar;
private ViewPager viewPager;
private TabLayout tabLayout;
private TabAdapter tabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loggedin);
        setTitle("Zicron");

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = findViewById(R.id.viewpager);
        tabLayout = findViewById(R.id.tablayout);



        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.PIitem)
        {

            if(Build.VERSION.SDK_INT >=23 && ActivityCompat.checkSelfPermission(this ,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 3000);
            }
            else
            {
                    captureImage();
            }
        }
        else if(R.id.Logoutitem == item.getItemId()){
           ParseUser.getCurrentUser().logOut();
           Intent it = new Intent(this , MainActivity.class);
           startActivity(it);
           finish();

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode , permissions , grantResults);

        if(requestCode == 3000)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                captureImage();
            }
        }

    }

    private void captureImage() {

        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 4000);
    }
    Bitmap receivedimage;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 4000 && resultCode == RESULT_OK && data!=null)
        {
            try{
                Uri selectedimage = data.getData();
               receivedimage = MediaStore.Images.Media.getBitmap(this.getContentResolver() , selectedimage);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                receivedimage.compress(Bitmap.CompressFormat.PNG , 100 , byteArrayOutputStream);
                byte[] bytes = byteArrayOutputStream.toByteArray();

                ParseFile parseFile = new ParseFile("Img.png" , bytes);
                ParseObject parseObject = new ParseObject("Photo");
                parseObject.put("Picture" , parseFile);
                parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Loading");
                progressDialog.setCancelable(false);
                progressDialog.show();
                parseObject.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                        {
                            FancyToast.makeText(loggedin.this, "Saved" , Toast.LENGTH_SHORT , FancyToast.SUCCESS , false).show();

                        }
                        else
                        {
                            FancyToast.makeText(loggedin.this , ""+e.getMessage() , Toast.LENGTH_SHORT , FancyToast.ERROR , false).show();
                        }
                        progressDialog.dismiss();
                    }
                });


            }catch (Exception e)
            {
                Toast.makeText(this , "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        
    }
}
