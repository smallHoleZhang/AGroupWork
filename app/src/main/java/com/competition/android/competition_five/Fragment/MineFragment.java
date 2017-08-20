package com.competition.android.competition_five.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.competition.android.competition_five.R;
import com.competition.android.competition_five.Uilt.OpenUile;

/**
 * Created by hasee on 2017/8/18.
 */

public class MineFragment extends Fragment {
    public static Fragment newInstance(){
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_layout,container,false);
        OpenUile.setPrimaryDark(getActivity(),R.color.greyed);
        return view;
    }
}