package com.minsu.minsu.common.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by hpc on 2017/12/6.
 */

public class ChatRecordFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Unbinder unbinder;
    private View view;
    private String tokenId;
    private ChatListAdapter chatListAdapter;
    private static final String TAG = "ChatRecordFragment";

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container) {
        view = inflater.inflate(R.layout.fragment_all_order, container, false);
        return view;
    }

    @Override
    protected void initListener() {
        tokenId = StorageUtil.getTokenId(getActivity());

    }

    private void getRongyunToken(String chat_id) {
        MinSuApi.getRongyunToken(getActivity(), 0x002, tokenId, Integer.parseInt(chat_id), callBack);
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

                            chatListAdapter = new ChatListAdapter(R.layout.item_chat, chatBean.data);
                            recyclerView = view.findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            recyclerView.setAdapter(chatListAdapter);
                            if (chatBean.data.size() == 0) {
                                chatListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            chatListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                                    StorageUtil.setKeyValue(getActivity(), "chat_id", chatListAdapter.getItem(position).id + "");
                                    StorageUtil.setKeyValue(getActivity(), "chat_title", chatListAdapter.getItem(position).nickname);
                                    getRongyunToken(chatListAdapter.getItem(position).id + "");


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
                        if (code == 200) {
                            JSONObject data1 = jsonObject.getJSONObject("data1");
                            JSONObject jsonObject1 = new JSONObject(data1.toString());
                            String token = jsonObject1.getString("token");
                            JSONObject data2 = jsonObject.getJSONObject("data2");
                            JSONObject jsonObject2 = new JSONObject(data2.toString());
                            String userId = jsonObject2.getString("userId");
                            isReconnect(token, userId);
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

    private void isReconnect(String token, final String userId) {
        if (getActivity().getApplicationInfo().packageName.equals(App.getCurProcessName(getActivity().getApplicationContext()))) {

            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(final String user_id) {
                    Log.i(TAG, "onSuccess: " + "融云连接成功");
                    String chat_title = StorageUtil.getValue(getActivity(), "chat_title");
                    if (RongIM.getInstance() != null) {
                        RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目

//                        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//                            @Override
//                            public UserInfo getUserInfo(String s) {
//                                return findUserById(s);
//                            }
//                        },true);
                        UserInfo userInfo = new UserInfo(user_id,StorageUtil.getValue(getActivity(),"nickname"),Uri.parse(StorageUtil.getValue(getActivity(),"head_pic")));
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        RongIM.getInstance().startPrivateChat(getActivity(), userId, chat_title);
                    }

                    RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
                        @Override
                        public Message onSend(Message message) {
                            Log.i(TAG, "onSend: " + message.getSenderUserId() + message.getTargetId());
                            return message;
                        }

                        @Override
                        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                            Log.i(TAG, "onSent: " + message + sentMessageErrorCode);
                            return false;
                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        RongIM.getInstance().disconnect();
    }
}
