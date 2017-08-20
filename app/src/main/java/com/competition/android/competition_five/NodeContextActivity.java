package com.competition.android.competition_five;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.competition.android.competition_five.Adapter.NodecontextRecycleViewAdapter;
import com.competition.android.competition_five.Adapter.OcrRecycViewAdapter;
import com.competition.android.competition_five.Entity.OcrList;

import java.util.ArrayList;

/**
 * Created by hasee on 2017/8/20.
 */

public class NodeContextActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodecontext_layout);
        OcrList ocrList = (OcrList) getIntent().getSerializableExtra("ocrlist");
        ArrayList<String> arrayList = ocrList.getArrayList();
        mRecyclerView = (RecyclerView) findViewById(R.id.nodecontext_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        NodecontextRecycleViewAdapter nodecontextRecycleViewAdapter= new NodecontextRecycleViewAdapter(this,arrayList);
        mRecyclerView.setAdapter(nodecontextRecycleViewAdapter);
        TextView textView = (TextView) findViewById(R.id.ocr_title_textview);
        textView.setText(ocrList.getList_name());


    }
}
