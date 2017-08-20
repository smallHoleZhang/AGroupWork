package com.competition.android.competition_five.Entity;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by hasee on 2017/8/20.
 */

public class OcrList {


    private String list_name;
    public String getList_name() {
        return list_name;
    }

    public void setList_name(String list_name) {
        this.list_name = list_name;
    }
    private ArrayList <String>  mArrayList = new ArrayList<>();

    public ArrayList<String> getArrayList() {
        return mArrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        mArrayList = arrayList;
    }
    public void AddArraylist(String list_name)
    {
        mArrayList.add(list_name);
    }
}
