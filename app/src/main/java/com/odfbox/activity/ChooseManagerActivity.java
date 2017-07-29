package com.odfbox.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.odfbox.OdfboxApplication;
import com.odfbox.R;
import com.odfbox.adapter.ChooseManagerAdapter;
import com.odfbox.entity.Managers;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseManagerActivity extends BaseActivity {


    @Bind(R.id.et_textUser)
    EditText etTextUser;
    @Bind(R.id.et_name)
    EditText etName;
    @Bind(R.id.search_button)
    Button searchButton;
    @Bind(R.id.listview)
    ListView listview;

    ChooseManagerAdapter adapter;
    ArrayList<Managers> adaptList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_manager);
        OdfboxApplication.addActivity(this);
        ButterKnife.bind(this);
        setTitleTextView("选择负责人", null);
        adapter = new ChooseManagerAdapter(mContext, adaptList);
        listview.setAdapter(adapter);
    }

    @OnClick(R.id.search_button)
    public void onViewClicked() {
        getList();
    }

    public void getList() {
        Managers magagers = new Managers();
        for (int i = 0; i < 5; i++) {
            adaptList.add(magagers);
        }
        adapter.notifyDataSetChanged();
    }
}
