package com.minsu.minsu.common;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.CommentReplyAdapter;
import com.minsu.minsu.common.bean.CommentReplyBean;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.widget.RoundedCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentReplyActivity extends BaseActivity {


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
    @BindView(R.id.comment_user_img)
    RoundedCornerImageView commentUserImg;
    @BindView(R.id.comment_name)
    TextView commentName;
    @BindView(R.id.comment_content)
    TextView commentContent;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.comment_time)
    TextView commentTime;
    @BindView(R.id.send)
    TextView send;
    private String comment_id;
    private String tokenId;

    @Override
    protected void processLogic() {
        MinSuApi.replyPage(0x001, tokenId, Integer.parseInt(comment_id), callBack);
    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("评论回复");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        comment_id = getIntent().getStringExtra("comment_id");
        tokenId = StorageUtil.getTokenId(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                if (content.equals("")) {
                    ToastManager.show("内容不能为空");
                    return;
                }
                MinSuApi.commentReply(0x002, tokenId, Integer.parseInt(comment_id), content, callBack);
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_comment_reply);
    }

    private CallBack callBack = new CallBack() {
        @Override
        public void onSuccess(int what, Response<String> result) {
            switch (what) {
                case 0x001:
                    CommentReplyBean commentReplyBean = new Gson().fromJson(result.body(), CommentReplyBean.class);
                    if (commentReplyBean.code == 200) {
                        commentContent.setText(commentReplyBean.data1.content);
                        commentName.setText(commentReplyBean.data1.nickname);
                        commentTime.setText(commentReplyBean.data1.add_time);
                        if (commentReplyBean.data1.head_pic.contains("http")) {
                            Glide.with(CommentReplyActivity.this).load(commentReplyBean.data1.head_pic).into(commentUserImg);
                        } else {
                            Glide.with(CommentReplyActivity.this).load(Constant.BASE2_URL + commentReplyBean.data1.head_pic).into(commentUserImg);
                        }
                        CommentReplyAdapter commentReplyAdapter = new CommentReplyAdapter(R.layout.item_comment, commentReplyBean.data2);
                        recyclerView.setAdapter(commentReplyAdapter);
                    }
                    break;
                case 0x002:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.replyPage(0x001, tokenId, Integer.parseInt(comment_id), callBack);
                            etContent.setText("");
                        } else if (code == 111) {
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
    protected Context getActivityContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
