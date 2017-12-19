package com.zhuye.minsu.common;

import android.content.Context;
import android.os.Bundle;

import com.zhuye.minsu.R;
import com.zhuye.minsu.base.BaseActivity;

import butterknife.ButterKnife;

public class RoomDetailActivity extends BaseActivity {

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_room_detail);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }
}
