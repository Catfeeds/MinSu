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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String name;
    private String mobile;
    private String address;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        toolbarTitle.setText("编辑地址");
        tokenId = StorageUtil.getTokenId(this);
        ivLeft.setVisibility(View.VISIBLE);
        address_id = getIntent().getStringExtra("address_id");
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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
                if (etUserName!=null&&etUserName.length()!=0)
                {
                    Pattern p = Pattern.compile(".*\\d+.*");
                    Matcher m = p.matcher(etUserName);
                    if (m.matches())
                    {
                        ToastManager.show("请输入合法的名字");
                        return;
                    }
                }else {
                    ToastManager.show("请输入名字");
                    return;
                }
                if (address_id.equals("")){
                    if (etUserPhone.length()==11)
                    {
                        MinSuApi.addAddress(EditAddressActivity.this,0x001,tokenId,etUserName,etUserPhone,etUserAddress,callBack);
                    }else {
                        ToastManager.show("请输入正确的电话号码");
                    }

                }else{
                    if (userPhone.length()==11)
                    {
                        if (etUserAddress.equals(address)&&etUserName.equals(name)&&etUserPhone.equals(mobile))
                        {
                           ToastManager.show("信息没有修改");
                           return;
                        }else {
                            MinSuApi.editAddressSubmit(EditAddressActivity.this,0x003,tokenId,
                                    Integer.parseInt(address_id),etUserName,etUserPhone,etUserAddress,callBack);
                        }
                    }else {
                        ToastManager.show("请输入正确电话号码");
                    }


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
                        name = data.getString("name");
                        mobile = data.getString("mobile");
                        address = data.getString("address");
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


    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
}
