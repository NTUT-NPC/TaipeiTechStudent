package com.Ntut.other;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.Ntut.BaseFragment;
import com.Ntut.R;
import com.Ntut.model.OtherInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Andy on 2017/4/28.
 */

public class OtherFragment extends BaseFragment {

    private View fragmentView;
    private RecyclerView recyclerView;
    private List<OtherInfo> otherInfoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_other, container, false);
        recyclerView = (RecyclerView) fragmentView.findViewById(R.id.other_list);
        initListData();
        OtherAdapter adapter = new OtherAdapter(otherInfoList, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        return fragmentView;
    }

    private void initListData() {
        otherInfoList = new ArrayList<>();
        //添加項目
        otherInfoList.add(new OtherInfo(getString(R.string.credit_text)));
        otherInfoList.add(new OtherInfo(getString(R.string.account_setting_text)));
        otherInfoList.add(new OtherInfo(getString(R.string.feedback_text)));
        otherInfoList.add(new OtherInfo(getString(R.string.etc_text)));
    }

    @Override
    public int getTitleColorId() {
        return R.color.blue;
    }

    @Override
    public int getTitleStringId() {
        return R.string.other_text;
    }
}
