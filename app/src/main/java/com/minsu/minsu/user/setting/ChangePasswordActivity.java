package com.minsu.minsu.user.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.MainActivity;
import com.minsu.minsu.utils.CheckUtil;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePasswordActivity extends BaseActivity {


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
    @BindView(R.id.original_password)
    EditText originalPassword;
    @BindView(R.id.new_password)
    EditText newPassword;
    @BindView(R.id.next)
    Button next;
    private String tokenId;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        tokenId = StorageUtil.getTokenId(this);
        toolbarTitle.setText("修改密码");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mOldPassword = originalPassword.getText().toString();
                String mNewPassword = newPassword.getText().toString();
                if (mOldPassword.equals("")) {
                    ToastManager.show("旧密码不能为空");
                    return;
                }
                if (mNewPassword.equals("")) {
                    ToastManager.show("新密码不能为空");
                    return;
                }
                boolean password = CheckUtil.isPassword(mOldPassword);
                boolean password1 = CheckUtil.isPassword(mNewPassword);
                if (!password){
                    ToastManager.show("旧密码输入格式不对");
                    return;
                }
                if (!password1){
                    ToastManager.show("新密码输入格式不对");
                    return;
                }
                MinSuApi.changePassword(ChangePasswordActivity.this, 0x001, tokenId, mOldPassword, mNewPassword, callBack);
            }
        });
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_change_password);
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
                            ToastManager.show(msg);
                            startActivity(new Intent(ChangePasswordActivity.this, MainActivity.class));
                        } else if (code == 201) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
