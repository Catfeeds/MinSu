package com.minsu.minsu.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseActivity;
import com.minsu.minsu.common.adapter.AddressListAdapter;
import com.minsu.minsu.user.bean.AddressListBean;
import com.minsu.minsu.user.setting.EditAddressActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressActivity extends BaseActivity implements View.OnClickListener {

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
    @BindView(R.id.edit_address)
    TextView editAddress;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private String token;
    private boolean isPause = false;
    private NormalDialog dialog;
    private int post;
    private AddressListBean addressListBean;
    private AddressListAdapter addressListAdapter;

    @Override
    protected void processLogic() {
        MinSuApi.addressList(this, 0x001, token,callBack);
    }

    @Override
    protected void setListener() {
        token = StorageUtil.getTokenId(this);
        toolbarTitle.setText("送货地址");
        ivLeft.setVisibility(View.VISIBLE);
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.addressList(AddressActivity.this, 0x001, token,callBack);
                if (StorageUtil.getValue(AddressActivity.this,"networks")!=null&&
                        StorageUtil.getValue(AddressActivity.this,"networks").equals("no"))
                {
                    refreshlayout.finishRefresh(3000,false);
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        editAddress.setOnClickListener(this);
    }
private CallBack callBack=new CallBack() {
    @Override
    public void onSuccess(int what, Response<String> result) {
        switch (what){
            case 0x001:
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    if (code == 200) {
                        addressListBean = new Gson().fromJson(result.body(), AddressListBean.class);
                        addressListAdapter = new AddressListAdapter(R.layout.item_address, addressListBean.data);
                        recyclerView.setAdapter(addressListAdapter);
                        if (addressListBean.data.size()==0){
                            addressListAdapter.setEmptyView(R.layout.empty,recyclerView);
                        }
                        addressListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                                post = position;
                                switch (view.getId()){
                                    case R.id.address_edit:
                                        Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
                                        intent.putExtra("address_id", addressListAdapter.getItem(position).address_id+"");
                                        startActivity(intent);
                                        break;
                                    case R.id.address_delete:
                                        dialog = new NormalDialog(AddressActivity.this);
                                        dialog.isTitleShow(false)
                                                .bgColor(Color.parseColor("#ffffff"))
                                                .cornerRadius(5)
                                                .content("确定要删除吗")
                                                .contentGravity(Gravity.CENTER)
                                                .contentTextColor(Color.parseColor("#99000000"))
                                                .dividerColor(Color.parseColor("#55000000"))
                                                .btnTextSize(15.5f, 15.5f)//
                                                .btnTextColor(Color.parseColor("#99000000"), Color.parseColor("#CCEA4F05"))
                                                .widthScale(0.85f)
                                                .show();

                                        dialog.setOnBtnClickL(
                                                new OnBtnClickL() {
                                                    @Override
                                                    public void onBtnClick() {
                                                        dialog.dismiss();
                                                    }
                                                },
                                                new OnBtnClickL() {
                                                    @Override
                                                    public void onBtnClick() {
                                                        dialog.dismiss();
                                                        //确定删除
                                                      MinSuApi.deleteAddress(AddressActivity.this,0x002,token, addressListAdapter.getItem(position).address_id,callBack);
                                                    }
                                                });
                                        break;

                                    case R.id.default_address:
                                         MinSuApi.set_default_address(AddressActivity.this,0x003,token, addressListAdapter.getItem(position).address_id,callBack);
                                        break;
                                }
                            }
                        });
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
                        ToastManager.show("删除成功");
                        MinSuApi.addressList(AddressActivity.this, 0x001, token,callBack);
                    }else if (code==211){
                        ToastManager.show(msg);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                break;

            case 0x003:
                try {
                    JSONObject jsonObject = new JSONObject(result.body());
                    int code = jsonObject.getInt("code");
                    String msg = jsonObject.getString("msg");
                    if (code == 200) {
                        for (int i=0;i<addressListBean.data.size();i++)
                        {
                            addressListBean.data.get(i).is_default=0;
                        }
                        addressListBean.data.get(post).is_default=1;


                        recyclerView.scrollTo(post,0);
                        addressListAdapter.notifyDataSetChanged();

                    }else if (code==211){
                        ToastManager.show(msg);
                    }
                }catch (JSONException e){
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
        setContentView(R.layout.activity_address);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_address:
                Intent intent = new Intent(AddressActivity.this, EditAddressActivity.class);
                intent.putExtra("address_id","");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPause) {
            isPause = false;
            //加载数据
            MinSuApi.addressList(this, 0x001, token,callBack);
        }
    }
}
