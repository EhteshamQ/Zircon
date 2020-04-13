package com.example.zircon;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.io.ByteArrayOutputStream;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShareImages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShareImages extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private EditText caption;
    private ImageView imageView;
    private Bitmap bitmap ;
    private Button sharebutton;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShareImages() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareImages.
     */
    // TODO: Rename and change types and number of parameters
    public static ShareImages newInstance(String param1, String param2) {
        ShareImages fragment = new ShareImages();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_share_images, container, false);

        imageView = view.findViewById(R.id.imageView);
        caption = view.findViewById(R.id.caption);
        sharebutton = view.findViewById(R.id.share_image);

        imageView.setOnClickListener(this);
        sharebutton.setOnClickListener(this);
        BitmapDrawable  bmp =(BitmapDrawable) imageView.getDrawable();
        bitmap = bmp.getBitmap();



    return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId())
        {
            case R.id.imageView:
                if(Build.VERSION.SDK_INT >=23 && ActivityCompat.checkSelfPermission(getContext() ,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE} , 1000);
                }
                else
                {
                    getChosenImage();
                }
                            break;
            case R.id.share_image:
                if(receivedimage !=null)
                {
                    if(caption.getText().toString().equals(""))
                    {
                        FancyToast.makeText(getContext() , "Caption Can't be Null" , Toast.LENGTH_SHORT , FancyToast.INFO , false).show();
                    }
                    else
                    {
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        receivedimage.compress(Bitmap.CompressFormat.PNG , 100  , byteArrayOutputStream  );
                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("Img.png" , bytes);
                        ParseObject parseObject = new ParseObject("Photo");
                        parseObject.put("Picture" , parseFile);
                        parseObject.put("Caption" , caption.getText().toString());
                        parseObject.put("username" , ParseUser.getCurrentUser().getUsername());
                        final ProgressDialog progressDialog = new ProgressDialog(getContext());
                        progressDialog.setMessage("Uploading...");
                        progressDialog.show();
                        progressDialog.setCancelable(false);
                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e == null)
                                {
                                    FancyToast.makeText(getContext() , "Saved" , Toast.LENGTH_SHORT , FancyToast.SUCCESS , false).show();
                                    imageView.setImageBitmap(bitmap);
                                    caption.setText("");

                                }
                                else
                                {
                                    FancyToast.makeText(getContext() , e.getMessage() , Toast.LENGTH_SHORT , FancyToast.ERROR , false).show();
                                    imageView.setImageBitmap(bitmap);
                                    caption.setText("");
                                }
                                progressDialog.dismiss();
                            }
                        });
                    }
                }
                else
                {
                    FancyToast.makeText(getContext() ,  "Select an Image", Toast.LENGTH_SHORT , FancyToast.INFO , false).show();
                }

                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1000)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getChosenImage();
            }
        }


    }

    private void getChosenImage() {
       // FancyToast.makeText(getContext() , "Choose image" , Toast.LENGTH_SHORT , FancyToast.SUCCESS , false).show();

        Intent intent = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent , 2000);

    }
   private Bitmap receivedimage;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                    try{
                        Uri selectedimage = data.getData();
                        String[] filePath = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getActivity().getContentResolver().query(selectedimage,filePath,null , null , null);
                        cursor.moveToFirst();
                        int columnindex = cursor.getColumnIndex(filePath[0]);
                        String picturepath = cursor.getString(columnindex);
                        cursor.close();
                        receivedimage = BitmapFactory.decodeFile(picturepath);
                        imageView.setImageBitmap(receivedimage);

                    }catch (Exception e)
                    {
                       Toast.makeText(getContext()  , "Error "+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }
}
