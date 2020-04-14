package com.example.zircon;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFragment extends Fragment implements AdapterView.OnItemClickListener , AdapterView.OnItemLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
private TextView load;
    public UserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
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
        arrayList = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter(getContext() , android.R.layout.simple_list_item_1 , arrayList);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        listView = view.findViewById(R.id.ListView);
        listView.setOnItemClickListener(UserFragment.this);
        load = view.findViewById(R.id.textAnimate);
        listView.setOnItemLongClickListener(UserFragment.this);

        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();

        parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
                parseQuery.findInBackground(new FindCallback<ParseUser>() {
                    @Override
                    public void done(List<ParseUser> objects, ParseException e) {
                        if(e == null)
                        {
                            if(objects.size() > 0)
                            {
                                for(ParseUser user : objects)
                                {

                                    arrayList.add(user.getUsername());
                                }
                                listView.setAdapter(arrayAdapter);
                                load.animate().alpha(0f).setDuration(2000);
                                listView.setVisibility(View.VISIBLE);


                            }
                        }
                    }
                });

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext() , posts.class);
        intent.putExtra("username" , arrayList.get(position).toString());
        startActivity(intent);


    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
        parseQuery.whereEqualTo("username" , arrayList.get(position));
       // FancyToast.makeText(getContext() , arrayList.get(position) , Toast.LENGTH_SHORT , FancyToast.ERROR , false).show();
        parseQuery.getFirstInBackground((user, e) -> {
            if(user !=null && e == null)
            {
                String bio = user.get("bio")!=null ? user.get("bio").toString() :"";
                String Hobbies = user.get("Hobbies")!=null ? user.get("Hobbies").toString() : "";
                String Profession = user.get("profession")!=null ? user.get("profession").toString(): "";
                final PrettyDialog prettyDialog = new PrettyDialog(getContext());

                prettyDialog.setTitle(arrayList.get(position).toString())
                        .setMessage("Bio :" + bio + "\n" + "Hobbies :" + Hobbies + "\n" + "Profession:" + Profession)
                .addButton("dismiss", R.color.colorAccent, R.color.jetblack, () -> prettyDialog.dismiss()).show();


            }

        });



        return true;
    }
}
