package com.example.zircon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    public TabAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position)
        {   case 0: profileFrag profilFrag = new profileFrag();
                    return profilFrag;
            case 1:
                return  new UserFragment();

            case 2 :
                return new ShareImages();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0: return "Profile";
            case 1 : return "Users";
            case 2: return "Share Posts";
            default:
                return null;
        }




    }
}
