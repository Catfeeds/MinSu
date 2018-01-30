package com.minsu.minsu.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.CommentListAdapter;
import com.minsu.minsu.common.bean.CommentBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentActivity extends BaseActivity {


    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right)
    ImageView ivRight;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.iv_left)
    ImageView ivLeft;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private String tokenId;
    private String house_id;

    @Override
    protected void processLogic() {
        MinSuApi.roomCommentList(this, 0x001, tokenId, house_id, callBack);
    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("评论列表");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tokenId = StorageUtil.getTokenId(this);
        house_id = getIntent().getStringExtra("house_id");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            CommentBean commentBean = new Gson().fromJson(result.body(), CommentBean.class);
                            final CommentListAdapter commentListAdapter = new CommentListAdapter(R.layout.item_comment, commentBean.data,CommentActivity.this);
                            recyclerView.setAdapter(commentListAdapter);
                            commentListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                @Override
                                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                    switch (view.getId()) {
                                        case R.id.comment_zan:
                                            if (commentListAdapter.getItem(position).u_coll == 1) {
                                                //已点赞
                                                MinSuApi.roomCancelDianzan(0x002, tokenId, commentListAdapter.getItem(position).comment_id, callBack);

                                            } else {
                                                //未点赞
                                                MinSuApi.roomDianzan(0x003, tokenId, commentListAdapter.getItem(position).comment_id, callBack);
                                            }
                                            break;
                                        case R.id.comment_reply_count:
                                            Intent intent = new Intent(CommentActivity.this, CommentReplyActivity.class);
                                            intent.putExtra("comment_id", commentListAdapter.getItem(position).comment_id+"");
                                            startActivity(intent);
                                            break;
                                    }
                                }
                            });
                            if (commentBean.data.size() == 0) {
                                commentListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                        } else if (code == 289) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.roomCommentList(CommentActivity.this, 0x001, tokenId, house_id, callBack);
                        } else if (code == 211) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.roomCommentList(CommentActivity.this, 0x001, tokenId, house_id, callBack);
                        } else if (code == 211) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result) {

        }

        @Override
        public void onFinish(int what) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        MinSuApi.roomCommentList(this, 0x001, tokenId, house_id, callBack);
    }
}
