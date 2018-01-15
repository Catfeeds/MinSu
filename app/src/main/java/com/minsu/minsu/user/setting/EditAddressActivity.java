package com.minsu.minsu.user.setting;

import android.content.Context;
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
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAddressActivity extends BaseActivity {


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
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.user_phone)
    EditText userPhone;
    @BindView(R.id.user_address)
    EditText userAddress;
    @BindView(R.id.config)
    Button config;
    private  String tokenId;
    private String address_id;
    private String sub_type;
    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("编辑地址");
        tokenId = StorageUtil.getTokenId(this);

        address_id = getIntent().getStringExtra("address_id");

        if (address_id.equals("")){
            sub_type="create";
        }else {
            sub_type="edit";
            MinSuApi.editAddress(this,0x002,tokenId,Integer.parseInt(address_id),callBack);
        }
        config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String etUserName = username.getText().toString();
                String etUserPhone = userPhone.getText().toString();
                String etUserAddress = userAddress.getText().toString();
                if (address_id.equals("")){
                    MinSuApi.addAddress(EditAddressActivity.this,0x001,tokenId,etUserName,etUserPhone,etUserAddress,callBack);
                }else{
                    MinSuApi.editAddressSubmit(EditAddressActivity.this,0x003,tokenId,Integer.parseInt(address_id),etUserName,etUserPhone,etUserAddress,callBack);
                }

            }
        });
    }
private CallBack callBack=new CallBack() {
    @Override
    public void onSuccess(int what, Response<String> result) {
        switch (what){
            case 0x001:
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code==200){
                        ToastManager.show(msg);
                        finish();
                    }else if (code==111){
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
                    if (code==200){
                        JSONObject data = jsonObject.getJSONObject("data");
                        String name = data.getString("name");
                        String mobile = data.getString("mobile");
                        String address = data.getString("address");
                        username.setText(name);
                        userPhone.setText(mobile);
                        userAddress.setText(address);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case 0x003:
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code==200){
                        ToastManager.show("编辑完成");
                        finish();
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
    protected void loadViewLayout() {
        setContentView(R.layout.activity_edit_address);
    }

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
