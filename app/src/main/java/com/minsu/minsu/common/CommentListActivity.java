package com.minsu.minsu.common;

import android.content.Context;
import android.os.Bundle;

import com.minsu.minsu.R;
import com.minsu.minsu.base.BaseActivity;

public class CommentListActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_comment_list);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }
}
