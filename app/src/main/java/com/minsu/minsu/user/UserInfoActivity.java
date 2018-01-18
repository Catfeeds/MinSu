package com.minsu.minsu.user;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.dialog.entity.DialogMenuItem;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalListDialog;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.Constant;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.user.camera.CropUtils;
import com.minsu.minsu.user.camera.FileUtil;
import com.minsu.minsu.user.camera.PermissionUtil;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.widget.RoundedCornerImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
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
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_CODE_CROUP_PHOTO = 3;
    private Uri uri;
    private File file;

    @Override
    protected void processLogic() {

    }

    @Override
    protected void setListener() {
        token = StorageUtil.getTokenId(this);
        toolbarTitle.setText("个人资料");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        MinSuApi.userInfo(this, 0x001, token, callBack);
        mMenuItems.add(new DialogMenuItem("男", 0));
        mMenuItems.add(new DialogMenuItem("女", 0));
        rlSex.setOnClickListener(this);
        rlBirthday.setOnClickListener(this);
        rlNickname.setOnClickListener(this);
        rlEmail.setOnClickListener(this);
        rlAuthentication.setOnClickListener(this);
        rlLandlordAuthentication.setOnClickListener(this);
        rlAvatar.setOnClickListener(this);
        init();//建立相机存储的缓存的路径
    }

    /**
     * 建立相机存储的缓存的路径
     */
    private void init() {
        file = new File(FileUtil.getCachePath(this), "user-avatar.jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            uri = FileProvider.getUriForFile(App.getInstance(), "com.minsu.minsu.FileProvider", file);
        }
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
                            if (is_house == 1) {
                                landlordAuthentication.setText("认证通过");
                            } else if (is_house == 2) {
                                landlordAuthentication.setText("审核中...");
                            } else if (is_house == 1) {
                                landlordAuthentication.setText("审核失败");
                            }
                            if (is_name == 1) {
                                authentication.setText("认证通过");
                            } else if (is_name == 2) {
                                authentication.setText("审核中...");
                            } else if (is_name == 1) {
                                authentication.setText("审核失败");
                            }
                            email.setText(mEmail);
                            birthday.setText(mBirthday);
                            nickname.setText(mNickname);
                            sex.setText((mSex == 1) ? "男" : "女");

                            if (mHead_pic.contains("http")) {
                                Glide.with(UserInfoActivity.this)
                                        .load(mHead_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                        .into(imgAvatar);
                            } else {
                                Glide.with(UserInfoActivity.this)
                                        .load(Constant.BASE2_URL + mHead_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                        .into(imgAvatar);
                            }
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
                case 0x006:
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
                        MinSuApi.birthdayChange(0x003, token, year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, callBack);
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
                startActivity(new Intent(UserInfoActivity.this, LandlordAuthenticationActivity.class));
                break;
            case R.id.rl_avatar:
                chooseType();
                break;
        }
    }

    private void chooseType() {
        final String[] stringItems = {"拍照", "从相册选取"};
        final ActionSheetDialog dialog = new ActionSheetDialog(mContext, stringItems, null);
        dialog.title("选择头像").titleTextSize_SP(14.5f).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    //拍照
                    case 0:
                        if (PermissionUtil.hasCameraPermission(UserInfoActivity.this)) {
                            uploadAvatarFromPhotoRequest();
                            dialog.dismiss();
                        }
                        break;
                    //相册
                    case 1:
                        if (PermissionUtil.hasReadExternalStoragePermission(UserInfoActivity.this)) {
                            uploadAvatarFromAlbumRequest();
                            dialog.dismiss();
                        }
                        break;
                    default:
                        break;
                }

            }
        });
    }

    /**
     * photo
     */
    private void uploadAvatarFromPhotoRequest() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CODE_TAKE_PHOTO);
    }

    /**
     * album
     */
    private void uploadAvatarFromAlbumRequest() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_CODE_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            return;
        }
        if (requestCode == REQUEST_CODE_ALBUM && data != null) {
            Uri newUri;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                newUri = Uri.parse("file:///" + CropUtils.getPath(this, data.getData()));
            } else {
                newUri = data.getData();
            }
            if (newUri != null) {
                startPhotoZoom(newUri);
            } else {
                Toast.makeText(this, "没有得到相册图片", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            uploadAvatarFromPhoto();
        }
    }

    private void uploadAvatarFromPhoto() {
        compressAndUploadAvatar(file.getPath());
    }

    private void compressAndUploadAvatar(String fileSrc) {
        final File cover = FileUtil.getSmallBitmap(this, fileSrc);
        //加载本地图片
        Uri uri = Uri.fromFile(cover);
        Glide.with(this).load(uri).into(imgAvatar);
        File file = FileUtil.getFileByUri(uri, this);
        StorageUtil.setKeyValue(this, "avatar", fileSrc);
        //上传文件
        MinSuApi.avatarChange(this, 0x006, token, file, callBack);
    }

    /**
     * 裁剪拍照裁剪
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra("crop", "true");// crop=true 有这句才能出来最后的裁剪页面.
        intent.putExtra("aspectX", 1);// 这两项为裁剪框的比例.
        intent.putExtra("aspectY", 1);// x:y=1:1
//        intent.putExtra("outputX", 400);//图片输出大小
//        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
    }

}
