package com.minsu.minsu.rongyun;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.RoomDetailActivity;
import com.minsu.minsu.common.bean.MessageEvent;
import com.minsu.minsu.utils.StorageUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * User: Daidingkang(ddk19941017@Gmail.com)
 * Date: 2017-05-03
 * Time: 16:51
 * FIXME
 */
public class ConversationListActivity extends BaseFragment
{
    private String rongtoken;
    private String tokenId;
    private String chat_id;
    private String chat_title;
    private View view;
    private ConversationListFragment fragment;
    private String nickkk;
    private String imgkkk;
    private SmartRefreshLayout refreshLayout;

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.conversationlist, container, false);
        return view;
    }

    @Override
    protected void initListener()
    {
        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.autoRefresh();
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                getRongyunToken();
                refreshlayout.finishRefresh(10000, false);
            }
        });
    }

    @Override
    protected void initData()
    {
        rongtoken = StorageUtil.getValue(getActivity(), "rongtoken");
        chat_id = StorageUtil.getValue(getActivity(), "chat_id");
        chat_title = StorageUtil.getValue(getActivity(), "chat_title");
        tokenId = StorageUtil.getTokenId(getActivity());

//        Map<String, Boolean> map = new HashMap<>();
//        map.put(Conversation.ConversationType.PRIVATE.getName(), false); // 会话列表需要显示私聊会话, 第二个参数 true 代表私聊会话需要聚合显示
//        map.put(Conversation.ConversationType.GROUP.getName(), false);  // 会话列表需要显示群组会话, 第二个参数 false 代表群组会话不需要聚合显示

        //RongIM.getInstance().startConversationList(getContext(), map);
    }

    private void getRongyunToken()
    {
        // MinSuApi.getRongyunToken(getActivity(), 0x001, tokenId, Integer.parseInt(chat_id), callBack);
        MinSuApi.getToken(tokenId, 1, callBack);
    }

    /**
     * 判断消息是否是 push 消息
     */
    private void isReconnect(String token)
    {

        Intent intent = getActivity().getIntent();


        //push，通知或新消息过来
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong"))
        {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("push") != null
                    && intent.getData().getQueryParameter("push").equals("true"))
            {

                reconnect(token);
            } else
            {
                //程序切到后台，收到消息后点击进入,会执行这里
                if (RongIM.getInstance() == null || RongIM.getInstance().getRongIMClient() == null)
                {

                    reconnect(token);
                } else
                {
                    enterFragment();
                }
            }
        } else
        {
            reconnect(token);
        }
//        }else if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)
//        {
//            reconnect(token);
//        }else if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED){
//            enterFragment();
//        }
    }

    /**
     * 重连
     *
     * @param token
     */
    private void reconnect(String token)
    {

        if (getActivity().getApplicationInfo().packageName.equals(App.getCurProcessName(getActivity().getApplicationContext())))
        {

            RongIM.connect(token, new RongIMClient.ConnectCallback()
            {
                @Override
                public void onTokenIncorrect()
                {

                }

                @Override
                public void onSuccess(String s)
                {

                    enterFragment();
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode)
                {

                }
            });
        }
    }

    /**
     * 加载 会话列表 ConversationListFragment
     */
    private void enterFragment()
    {

        if (fragment == null)
        {
            fragment = new ConversationListFragment();

        }
        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")
                .appendQueryParameter(Conversation.ConversationType.NONE.getName(), "false")
                .build();
      try
      {
          fragment.setUri(uri);
          FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
          transaction.add(R.id.rong_content, fragment);
          transaction.commit();
          if (fragment!=null)
          {
              refreshLayout.setEnableRefresh(false);
          }
      }catch (IllegalStateException e)
      {

      }
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener()
        {
            @Override
            public Message onSend(Message message)
            {
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode)
            {
                return false;
            }

        });
        RongIM.getInstance().setConversationListBehaviorListener(new RongIM.ConversationListBehaviorListener()
        {
            @Override
            public boolean onConversationPortraitClick(Context context, Conversation.ConversationType conversationType, String s)
            {
                return false;
            }

            @Override
            public boolean onConversationPortraitLongClick(Context context, Conversation.ConversationType conversationType, String s)
            {
                return false;
            }

            @Override
            public boolean onConversationLongClick(Context context, View view, UIConversation uiConversation)
            {
                return false;
            }

            @Override
            public boolean onConversationClick(Context context, View view, UIConversation uiConversation)
            {
                RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, uiConversation.getConversationTargetId(), new RongIMClient.ResultCallback<Boolean>()
                {
                    @Override
                    public void onSuccess(Boolean aBoolean)
                    {
                        //chatListAdapter.notifyItemRangeChanged(0, chatBean.data.size());
                        RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                        {
                            @Override
                            public void onSuccess(Integer integer)
                            {
                                EventBus.getDefault().post(new MessageEvent(integer));
                               // StorageUtil.setKeyValue(getActivity(), "charm", integer + "");
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode)
                            {

                            }
                        });
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode)
                    {

                    }
                });
                return false;
            }
        });
    }


    private CallBack callBack = new CallBack()
    {
        @Override
        public void onSuccess(int what, Response<String> result)
        {
            switch (what)
            {
                case 0x001:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            refreshLayout.finishRefresh();
                            JSONObject data1 = jsonObject.getJSONObject("data1");
                            JSONObject jsonObject1 = new JSONObject(data1.toString());
                            String token = jsonObject1.getString("token");
                            String userId = jsonObject1.getString("userId");
                            nickkk = StorageUtil.getValue(getActivity(), "nickkk");
                            imgkkk = StorageUtil.getValue(getActivity(), "imgkkk");
//                            RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
//
//                                @Override
//                                public UserInfo getUserInfo(String userId) {
//
//                                    return new UserInfo(rongtoken, nickkk, Uri.parse(imgkkk));//根据 userId 去你的用户系统里查询对应的用户信息返回给融云 SDK。
//                                }
//
//                            }, true);
//                            RongIM.getInstance().refreshUserInfoCache(new UserInfo(userId, nickkk,Uri.parse(imgkkk)));
                            isReconnect(token);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        @Override
        public void onFail(int what, Response<String> result)
        {

        }

        @Override
        public void onFinish(int what)
        {

        }
    };
}
