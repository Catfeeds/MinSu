package com.zhuye.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.lzy.okgo.model.Response;
import com.zhuye.minsu.App;
import com.zhuye.minsu.R;
import com.zhuye.minsu.api.Constant;
import com.zhuye.minsu.api.MinSuApi;
import com.zhuye.minsu.api.callback.CallBack;
import com.zhuye.minsu.base.BaseActivity;
import com.zhuye.minsu.user.camera.CropUtils;
import com.zhuye.minsu.user.camera.FileUtil;
import com.zhuye.minsu.user.camera.PermissionUtil;
import com.zhuye.minsu.utils.StorageUtil;
import com.zhuye.minsu.utils.ToastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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
    EditText userPhone;
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

    @Override
    protected void processLogic() {
        MinSuApi.renzhenPage(this, 0x002, token, callBack);
    }

    @Override
    protected void setListener() {
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
            uri = FileProvider.getUriForFile(App.getInstance(), "com.zhuye.minsu", file);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
                if (user_address.equals("")) {
                    ToastManager.show("请输入姓名");
                    return;
                }
                if (id_card.equals("")) {
                    ToastManager.show("请输入身份证号");
                    return;
                }
                if (user_phone.equals("")) {
                    ToastManager.show("请输入电话");
                    return;
                }
                if (user_address.equals("")) {
                    ToastManager.show("请输入地址");
                    return;
                }
                if (frontFile == null) {
                    ToastManager.show("身份证正面照不能为空");
                    return;
                }
                if (backFile == null) {
                    ToastManager.show("身份证反面照不能为空");
                    return;
                }
                MinSuApi.shimingSubmit(this, 0x001, token, userName, id_card, user_phone, user_address, frontFile, backFile, callBack);
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
        if (resultCode != -1) {
            return;
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
            Glide.with(this).load(uri).into(idFront);
            frontFile = FileUtil.getFileByUri(uri, this);
            StorageUtil.setKeyValue(NameAuthenticationActivity.this, "front_img", fileSrc);
        } else if (photoType == 2) {
            Glide.with(this).load(uri).into(idBack);
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
        intent.putExtra("aspectY", 1);// x:y=1:1
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
                            int is_name = jsonObject1.getInt("is_name");
                            if (is_name == 2) {
                                username.setEnabled(false);
                                userPhone.setEnabled(false);
                                userAddress.setEnabled(false);
                                idCard.setEnabled(false);
                                idFront.setClickable(false);
                                idBack.setClickable(false);
                                submit.setClickable(false);
                                ToastManager.show("审核中");
                            } else if (is_name == 1) {
                                username.setEnabled(false);
                                userPhone.setEnabled(false);
                                userAddress.setEnabled(false);
                                idCard.setEnabled(false);
                                idFront.setClickable(false);
                                idBack.setClickable(false);
                                submit.setClickable(false);
                                ToastManager.show("认证已通过，请勿重复提交");
                            } else {
                                ToastManager.show("审核失败");
                            }
                            if (username != null) {
                                username.setText(n_name);
                            }
                            if (userPhone != null) {
                                userPhone.setText(n_mobile);
                            }
                            if (userAddress != null) {
                                userAddress.setText(n_address);
                            }
                            if (idCard != null) {
                                idCard.setText(n_card);
                            }
                            if (n_zheng_pic != null) {
                                Glide.with(NameAuthenticationActivity.this)
                                        .load(Constant.BASE2_URL + n_zheng_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                        .into(idFront);
                            }
                            if (n_fan_pic != null) {
                                Glide.with(NameAuthenticationActivity.this)
                                        .load(Constant.BASE2_URL + n_fan_pic)
//                                    .placeholder(R.mipmap.ic_launcher)
                                        .into(idBack);
                            }
//                            username.setText(n_name);
//                            userPhone.setText(n_mobile);
//                            userAddress.setText(n_address);
//                            idCard.setText(n_card);
//                            Glide.with(NameAuthenticationActivity.this)
//                                    .load(Constant.BASE2_URL + n_zheng_pic)
////                                    .placeholder(R.mipmap.ic_launcher)
//                                    .into(idFront);
//                            Glide.with(NameAuthenticationActivity.this)
//                                    .load(Constant.BASE2_URL + n_fan_pic)
////                                    .placeholder(R.mipmap.ic_launcher)
//                                    .into(idBack);
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
}
