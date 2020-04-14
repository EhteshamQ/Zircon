package com.example.zircon;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText name , bio , hobbies, profession;
    private Button update;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFrag newInstance(String param1, String param2) {
        profileFrag fragment = new profileFrag();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        name = view.findViewById(R.id.name);
        bio= view.findViewById(R.id.bio);
        hobbies = view.findViewById(R.id.hobbies);
        profession = view.findViewById(R.id.profession);
        update = view.findViewById(R.id.updateinfo);
        final ParseUser parseUser = ParseUser.getCurrentUser();

        if (parseUser.get("profilename") != null) {
            name.setText(parseUser.get("profilename").toString());
        } else {
            name.setText("");
        }

        if (parseUser.get("bio") != null) {
            bio.setText(parseUser.get("bio").toString());
        } else {
            bio.setText("");
        }
        if (parseUser.get("profession") != null) {
            profession.setText(parseUser.get("profession").toString());
        } else {
            profession.setText("");
        }
        if (parseUser.get("Hobbies") != null) {
            hobbies.setText(parseUser.get("Hobbies").toString());
        } else {
            hobbies.setText("");
        }
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parseUser.put("profilename" , name.getText().toString());
                parseUser.put("bio" , bio.getText().toString());
                parseUser.put("profession" , profession.getText().toString());
                parseUser.put("Hobbies",hobbies.getText().toString());

                parseUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e == null)
                            FancyToast.makeText(getContext() , "Updated" , Toast.LENGTH_SHORT , FancyToast.SUCCESS , false).show();
                            else FancyToast.makeText(getContext() ,"Failed"+ e.getMessage() , Toast.LENGTH_SHORT , FancyToast.ERROR , false).show();
                    }
                });
            }
        });




        return view;


    }
}
