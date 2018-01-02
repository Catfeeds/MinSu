package com.zhuye.minsu.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.NormalListDialog;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.user.setting.LandlordAuthentication;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;
import com.zhuye.minsu.widget.RoundedCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.img_avatar)
    RoundedCornerImageView imgAvatar;
    @BindView(R.id.tag_arrow)
    ImageView tagArrow;
    @BindView(R.id.rl_avatar)
    RelativeLayout rlAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.rl_nickname)
    RelativeLayout rlNickname;
    @BindView(R.id.set_tag1)
    View setTag1;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.rl_sex)
    RelativeLayout rlSex;
    @BindView(R.id.set_tag2)
    View setTag2;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.rl_birthday)
    RelativeLayout rlBirthday;
    @BindView(R.id.set_tag3)
    View setTag3;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.rl_email)
    RelativeLayout rlEmail;
    @BindView(R.id.set_tag4)
    View setTag4;
    @BindView(R.id.tv_authentication)
    TextView tvAuthentication;
    @BindView(R.id.rl_authentication)
    RelativeLayout rlAuthentication;
    @BindView(R.id.set_tag5)
    View setTag5;
    @BindView(R.id.tv_landlord_authentication)
    TextView tvLandlordAuthentication;
    @BindView(R.id.rl_landlord_authentication)
    RelativeLayout rlLandlordAuthentication;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.birthday)
    TextView birthday;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.authentication)
    TextView authentication;
    @BindView(R.id.landlord_authentication)
    TextView landlordAuthentication;
    private String token;
    private ArrayList<DialogMenuItem> mMenuItems = new ArrayList<>();
    private int sexType = 1;//性别默认

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = StorageUtil.getTokenId(this);
        MinSuApi.userInfo(this, 0x001, token, callBack);
        mMenuItems.add(new DialogMenuItem("男", 0));
        mMenuItems.add(new DialogMenuItem("女", 0));
        rlSex.setOnClickListener(this);
        rlBirthday.setOnClickListener(this);
        rlNickname.setOnClickListener(this);
        rlEmail.setOnClickListener(this);
        rlAuthentication.setOnClickListener(this);
        rlLandlordAuthentication.setOnClickListener(this);
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_user_info);
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
                        if (code == 200) {
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String mUser_id = jsonObject1.getString("user_id");
                            String mNickname = jsonObject1.getString("nickname");
                            String mHead_pic = jsonObject1.getString("head_pic");
                            String mBirthday = jsonObject1.getString("birthday");
                            String mEmail = jsonObject1.getString("email");
                            int mSex = jsonObject1.getInt("sex");
                            int is_name = jsonObject1.getInt("is_name");
                            int is_house = jsonObject1.getInt("is_house");
                            email.setText(mEmail);
                            birthday.setText(mBirthday);
                            nickname.setText(mNickname);
                            sex.setText((mSex == 1) ? "男" : "女");
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
                            MinSuApi.userInfo(UserInfoActivity.this, 0x001, token, callBack);
                        } else if (code == 111) {
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
                            MinSuApi.userInfo(UserInfoActivity.this, 0x001, token, callBack);
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x004:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.userInfo(UserInfoActivity.this, 0x001, token, callBack);
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x005:
                    try {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        String msg = jsonObject.getString("msg");
                        if (code == 200) {
                            ToastManager.show(msg);
                            MinSuApi.userInfo(UserInfoActivity.this, 0x001, token, callBack);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_sex:
                final NormalListDialog dialog = new NormalListDialog(UserInfoActivity.this, mMenuItems);
                dialog.title("请选择")//
                        .titleTextSize_SP(18)//
                        .titleBgColor(Color.parseColor("#409ED7"))//
                        .itemPressColor(Color.parseColor("#85D3EF"))//
                        .itemTextColor(Color.parseColor("#303030"))//
                        .itemTextSize(14)//
                        .cornerRadius(0)//
                        .widthScale(0.8f)//
                        .show();

                dialog.setOnOperItemClickL(new OnOperItemClickL() {
                    @Override
                    public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String mOperName = mMenuItems.get(position).mOperName;
                        if (mOperName.equals("男")) {
                            sexType = 1;
                        } else if (mOperName.equals("女")) {
                            sexType = 2;
                        }
                        MinSuApi.sexChange(0x002, token, sexType, callBack);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.rl_birthday:
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        ToastManager.show("您选择了：" + year + "年" + monthOfYear
                                + "月" + dayOfMonth + "日");
                        MinSuApi.birthdayChange(0x003, token, year + "-" + monthOfYear + "-" + dayOfMonth, callBack);
                    }

                });
                datePickerDialog.show();
                break;
            case R.id.rl_nickname:
                LayoutInflater li = LayoutInflater.from(this);
                View promptsView = li.inflate(R.layout.nichen_dialog, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);
                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                final EditText inputName = promptsView.findViewById(R.id.input_name);
                final TextView mCancel = promptsView.findViewById(R.id.cancel);
                final TextView mSure = promptsView.findViewById(R.id.sure);


                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
                mSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = inputName.getText().toString();
                        MinSuApi.nicknameChange(0x004, token, name, callBack);
                        alertDialog.dismiss();
                    }
                });
                alertDialogBuilder
                        .setCancelable(true);
                // show it
                alertDialog.show();
                break;
            case R.id.rl_email:
                LayoutInflater liEmail = LayoutInflater.from(this);
                View emailView = liEmail.inflate(R.layout.email_dialog, null);

                final AlertDialog.Builder alertDialogBuilderEmail = new AlertDialog.Builder(
                        this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilderEmail.setView(emailView);
                // create alert dialog
                final AlertDialog alertDialogEmail = alertDialogBuilderEmail.create();
                final EditText EmailInputName = emailView.findViewById(R.id.input_name);
                final TextView EmailCancel = emailView.findViewById(R.id.cancel);
                final TextView EmailSure = emailView.findViewById(R.id.sure);


                EmailCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogEmail.dismiss();
                    }
                });
                EmailSure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String email = EmailInputName.getText().toString();
                        MinSuApi.emailChange(0x005, token, email, callBack);
                        alertDialogEmail.dismiss();
                    }
                });
                alertDialogBuilderEmail
                        .setCancelable(true);
                // show it
                alertDialogEmail.show();
                break;
            case R.id.rl_authentication:
                startActivity(new Intent(UserInfoActivity.this, NameAuthenticationActivity.class));
                break;
            case R.id.rl_landlord_authentication:
                startActivity(new Intent(UserInfoActivity.this, LandlordAuthentication.class));
        }
    }


}
