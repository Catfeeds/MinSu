package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;

public class NameAuthenticationActivity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.idCard)
    EditText idCard;
    @BindView(R.id.user_phone)
    TextView userPhone;
    @BindView(R.id.user_address)
    EditText userAddress;
    @BindView(R.id.id_front)
    ImageView idFront;
    @BindView(R.id.id_back)
    ImageView idBack;
    @BindView(R.id.submit)
    Button submit;
    private File file;
    private Uri uri;
    private String token;
    private static final int REQUEST_CODE_TAKE_PHOTO = 1;
    private static final int REQUEST_CODE_ALBUM = 2;
    private static final int REQUEST_CODE_CROUP_PHOTO = 3;
    private int photoType = 1;//默认正面
    private File frontFile;
    private File backFile;
    private int ttt=0;
    private File file1=null ,file2=null;
    private int is_name;
    private String is_name1;

    @Override
    protected void processLogic() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        MinSuApi.renzhenPage(this, 0x002, token, callBack);
    }

    @Override
    protected void setListener() {
        idCard.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
        try
        {
            ttt=getIntent().getIntExtra("ttt",0);
        }catch (Exception e)
        {

        }
        userPhone.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
               if (b)
               {
                   userPhone.setText("");
               }
            }
        });
        userAddress.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View view, boolean b)
            {
            if (b)
            {

            }
            }
        });
        is_name1 = StorageUtil.getValue(NameAuthenticationActivity.this,"is_name");
        token = StorageUtil.getTokenId(this);
        toolbarTitle.setText("实名认证");
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        idFront.setOnClickListener(this);
        idBack.setOnClickListener(this);
        userPhone.setOnClickListener(this);
        submit.setOnClickListener(this);
        init();//建立相机存储的缓存的路径
    }

    @Override
    protected void loadViewLayout() {
        setContentView(R.layout.activity_name_authentication);
    }

    @Override
    protected Context getActivityContext() {
        return this;
    }

    /**
     * 建立相机存储的缓存的路径
     */
    private void init() {
        file = new File(FileUtil.getCachePath(this), "idcard.jpg");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            //通过FileProvider创建一个content类型的Uri(android 7.0需要这样的方法跨应用访问)
            uri = FileProvider.getUriForFile(App.getInstance(), "com.minsu.minsu.FileProvider", file);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy()
    {
        StorageUtil.setKeyValue(NameAuthenticationActivity.this,"phones","");
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.user_phone:
                Intent intent=new Intent(NameAuthenticationActivity.this,PhineYanActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("house","1");
                startActivityForResult(intent,101);
                break;
            case R.id.id_front:
                photoType = 1;
                chooseType();
                break;
            case R.id.id_back:
                photoType = 2;
                chooseType();
                break;
            case R.id.submit:
                String userName = username.getText().toString();
                String id_card = idCard.getText().toString();
                String user_phone = userPhone.getText().toString();
                String user_address = userAddress.getText().toString();
                if (is_name==1)
                {
                    if (user_phone.length()!=11)
                    {
                        ToastManager.show("请输入正确的手机号");
                        return;
                    }
                    break;
                }else {
                    if (userName.equals("")) {
                        ToastManager.show("请输入姓名");
                        return;
                    }else {
                        Pattern p = Pattern.compile(".*\\d+.*");
                        Matcher m = p.matcher(userName);
                        if (m.matches())
                        {
                            ToastManager.show("请输入合法的名字");
                            return;
                        }
                    }
                    if (id_card.equals("")) {
                        ToastManager.show("请输入身份证号");
                        return;
                    }else {
                        if (id_card.length()!=18)
                        {
                            ToastManager.show("请输入正确的身份证号码");
                            return;
                        }
                    }
                    if (user_phone.equals("")) {
                        ToastManager.show("请输入电话");
                        return;
                    }else {
                        if (user_phone.length()!=11)
                        {
                            ToastManager.show("请输入正确的电话号码");
                            return;
                        }
                    }
                    if (user_address.equals("")) {
                        ToastManager.show("请输入地址");
                        return;
                    }
                    if (frontFile==null)
                    {
                        frontFile=file1;
                    }
                    if (frontFile == null) {
                        ToastManager.show("身份证正面照不能为空");
                        return;
                    }
                    if (backFile==null)
                    {
                        backFile=file2;
                    }
                    if (backFile == null) {
                        ToastManager.show("身份证反面照不能为空");
                        return;
                    }
                    StorageUtil.setKeyValue(NameAuthenticationActivity.this,"phones","");
                    MinSuApi.shimingSubmit(this, 0x001, token, userName, id_card, user_phone, user_address, frontFile, backFile, callBack);
                    break;
                }

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
                        if (PermissionUtil.hasCameraPermission(NameAuthenticationActivity.this)) {
                            uploadAvatarFromPhotoRequest();
                            dialog.dismiss();
                        }
                        break;
                    //相册
                    case 1:
                        if (PermissionUtil.hasReadExternalStoragePermission(NameAuthenticationActivity.this)) {
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

        if (requestCode==101)
        {
            String phone=StorageUtil.getValue(NameAuthenticationActivity.this,"phones");
            if (phone!=null&&phone.length()>4)
            {
                userPhone.setText(phone);
            }
        }
        //相册
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
            //相机
        } else if (requestCode == REQUEST_CODE_TAKE_PHOTO) {
            startPhotoZoom(uri);
        } else if (requestCode == REQUEST_CODE_CROUP_PHOTO) {
            uploadAvatarFromPhoto();
        }
    }

    private void uploadAvatarFromPhoto() {
        compressAndUploadAvatar(file.getPath());
    }

    /**
     * 压缩加载头像
     *
     * @param fileSrc
     */
    private void compressAndUploadAvatar(String fileSrc) {
        final File cover = FileUtil.getSmallBitmap(this, fileSrc);
        //加载本地图片
        Uri uri = Uri.fromFile(cover);
        if (photoType == 1) {
            Glide.with(App.getInstance()).load(uri).into(idFront);
            frontFile = FileUtil.getFileByUri(uri, this);
            StorageUtil.setKeyValue(NameAuthenticationActivity.this, "front_img", fileSrc);
        } else if (photoType == 2) {
            Glide.with(App.getInstance()).load(uri).into(idBack);
            backFile = FileUtil.getFileByUri(uri, this);
            StorageUtil.setKeyValue(NameAuthenticationActivity.this, "back_img", fileSrc);
        }


        //上传文件

//        DreamApi.uploadAvator(this, 0x002, token, file, uploadCallBack);
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
        intent.putExtra("aspectY", 0.8);// x:y=1:1
//        intent.putExtra("outputX", 400);//图片输出大小
//        intent.putExtra("outputY", 400);
        intent.putExtra("output", Uri.fromFile(file));
        intent.putExtra("outputFormat", "JPEG");// 返回格式
        startActivityForResult(intent, REQUEST_CODE_CROUP_PHOTO);
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
                            userAddress.setFocusable(false);
                            userPhone.setFocusable(false);
                            username.setFocusable(false);
                            idCard.setFocusable(false);
                            idCard.setFocusableInTouchMode(false);
                            userAddress.setFocusableInTouchMode(false);
                            userPhone.setFocusableInTouchMode(false);
                            username.setFocusableInTouchMode(false);
                            idFront.setEnabled(false);
                            idBack.setEnabled(false);
                            StorageUtil.setKeyValue(NameAuthenticationActivity.this,"is_name","shz");
                            finish();

                        } else if (code == 111) {
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
                            JSONObject data = jsonObject.getJSONObject("data");
                            JSONObject jsonObject1 = new JSONObject(data.toString());
                            String n_name = jsonObject1.getString("n_name");
                            String n_card = jsonObject1.getString("n_card");
                            String n_mobile = jsonObject1.getString("n_mobile");
                            String n_address = jsonObject1.getString("n_address");
                            String n_zheng_pic = jsonObject1.getString("n_zheng_pic");
                            String n_fan_pic = jsonObject1.getString("n_fan_pic");
                            is_name = jsonObject1.getInt("is_name");
                           // String error_message=jsonObject1.getString("error_message");
//                            username.setText(n_name);
//                            idCard.setText(n_card);
//                            userPhone.setText(n_mobile);
//                            userAddress.setText(n_address);
                            if (is_name == 2) {
                                username.setEnabled(false);
                                userPhone.setEnabled(false);
                                userAddress.setEnabled(false);
                                idCard.setEnabled(false);
                                idFront.setClickable(false);
                                idBack.setClickable(false);
                                submit.setClickable(false);
                                StorageUtil.setKeyValue(NameAuthenticationActivity.this,"statu","1");
                                StorageUtil.setKeyValue(NameAuthenticationActivity.this, "is_name", "shz");
                                ToastManager.show("审核中");
                                submit.setBackgroundResource(R.drawable.frame_button_22);
                                submit.setText("审核中");
                            } else if (is_name == 1) {
                                    username.setEnabled(false);
                                    userPhone.setEnabled(true);
                                    userAddress.setEnabled(false);
                                    toolbarTitle.setVisibility(View.GONE);
                                    idCard.setEnabled(false);
                                    idFront.setClickable(false);
                                    idBack.setClickable(false);
                                    StorageUtil.setKeyValue(NameAuthenticationActivity.this, "is_name", "isname");
                                    StorageUtil.setKeyValue(NameAuthenticationActivity.this,"statu","0");
                                    submit.setClickable(false);
                                submit.setBackgroundResource(R.drawable.frame_button_22);
                                submit.setText("已认证");
                                    ToastManager.show("认证已通过，请勿重复提交");

                            } else if (is_name==-1){
//                                if (error_message!=null)
//                                {
//                                    Toast.makeText(NameAuthenticationActivity.this,"审核失败："+error_message,Toast.LENGTH_LONG).show();
//                                }
                                StorageUtil.setKeyValue(NameAuthenticationActivity.this,"statu","-1");
                                StorageUtil.setKeyValue(NameAuthenticationActivity.this, "is_name", "shsb");
                                username.setEnabled(true);
                                userPhone.setEnabled(true);
                                userAddress.setEnabled(true);
                                idCard.setEnabled(true);
                                idFront.setClickable(true);
                                idBack.setClickable(true);
                                submit.setClickable(true);
                                idBack.setBackgroundResource(R.mipmap.back003);
                                idFront.setBackgroundResource(R.mipmap.back002);
                            }

                            if (!n_name.equals("null")) {
                                username.setText(n_name);
                            }
                            if (!n_mobile.equals("null")) {
                                n_mobile=n_mobile.substring(0,3)+"*****"+n_mobile.substring(n_mobile.length()-3,n_mobile.length());
                                userPhone.setText(n_mobile);
                            }
                            if (!n_address.equals("null")) {
                                userAddress.setText(n_address);
                            }
                            if (!n_card.equals("null")) {
                                n_card=n_card.substring(0,3)+"******"+n_card.substring(n_card.length()-3,n_card.length());
                                idCard.setText(n_card);
                            }
                            if (!n_zheng_pic.equals("null")) {
                                Glide.with(App.getInstance())
                                        .load(Constant.BASE2_URL + n_zheng_pic)
                                        .error(R.mipmap.back002)
                                        .into(idFront);
                                OkGo.<File>post(Constant.BASE2_URL+n_zheng_pic).execute(new FileCallback()
                                {
                                    @Override
                                    public void onSuccess(Response<File> response)
                                    {
                                        file1 = response.body();
                                        if (file1==null)
                                        {
                                            idFront.setBackgroundResource(R.mipmap.back002);
                                        }
                                    }
                                });
                            }
                            if (!n_fan_pic.equals("null")) {
                                Glide.with(App.getInstance())
                                        .load(Constant.BASE2_URL + n_fan_pic)
                                        .error(R.mipmap.back003)
                                        .into(idBack);
                                OkGo.<File>post(Constant.BASE2_URL+n_fan_pic).execute(new FileCallback()
                                {
                                    @Override
                                    public void onSuccess(Response<File> response)
                                    {
                                        file2 = response.body();
                                        if (file2==null)
                                        {
                                            idBack.setBackgroundResource(R.mipmap.back003);
                                        }
                                    }
                                });
                            }
                        } else if (code == 111) {
                            ToastManager.show(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case 0x003:
                    try
                    {
                        JSONObject jsonObject=new JSONObject(result.body());
                        if (jsonObject.getString("code").equals("200"))
                        {
                            ToastManager.show("更换成功");
                        }
                    } catch (JSONException e)
                    {
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
}
