package com.minsu.minsu.common.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.adapter.ChatListAdapter;
import com.minsu.minsu.common.bean.ChatBean;
import com.minsu.minsu.utils.StorageUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hpc on 2017/12/6.
 */

public class ChatRecordFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private View view;
    private String tokenId;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_all_order, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        tokenId = StorageUtil.getTokenId(getActivity());
    }

    @Override
    protected void initData() {
        MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
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
                            ChatBean chatBean = new Gson().fromJson(result.body(), ChatBean.class);
                            ChatListAdapter chatListAdapter = new ChatListAdapter(R.layout.item_chat, chatBean.data);
                            recyclerView = view.findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(chatListAdapter);
                            if (chatBean.data.size() == 0) {
                                chatListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
