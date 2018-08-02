package com.minsu.minsu.common.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.lzy.okgo.model.Response;
import com.minsu.minsu.App;
import com.minsu.minsu.R;
import com.minsu.minsu.api.MinSuApi;
import com.minsu.minsu.api.callback.CallBack;
import com.minsu.minsu.base.BaseFragment;
import com.minsu.minsu.common.MainActivity;
import com.minsu.minsu.common.adapter.CharRecordAdapter;
import com.minsu.minsu.common.adapter.ChatListAdapter;
import com.minsu.minsu.common.bean.ChatBean;
import com.minsu.minsu.common.bean.MessageEvent;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;
import com.minsu.minsu.widget.ListSlideView;
import com.minsu.minsu.widget.RoundedCornerImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by hpc on 2017/12/6.
 */

public class ChatRecordFragment extends BaseFragment
{
    private RecyclerView recyclerView;
    Unbinder unbinder;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private View view;
    private String tokenId;
    private ChatListAdapter chatListAdapter;
    // private MyAdapter chatListAdapter;
    private static final String TAG = "ChatRecordFragment";
    private int item;
    private String tarid;
    private ViewPager viewPager;
    private int item1;
    private String user_d;

    public void setViewPage(ViewPager viewPager)
    {
        this.viewPager = viewPager;
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container)
    {
        view = inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        // 设置菜单创建器。
        //  recyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
//        MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
//        if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)
//        {
//            RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
//            {
//                @Override
//                public void onSuccess(Integer integer)
//                {
//                    EventBus.getDefault().post(new MessageEvent(integer));
//                    StorageUtil.setKeyValue(getActivity(), "charm", integer + "");
//                    charMessage.charmessage(integer);
//                }
//
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode)
//                {
//
//                }
//            });
////
//        }
    }

    @Override
    protected void initListener()
    {
        Map<String,Boolean> put=new HashMap<>();
        put.put(Conversation.ConversationType.PRIVATE.getName(),false);
        RongIM.getInstance().startConversationList(getActivity(),put);
        Log.i("MainActivity1", getActivity().toString());
        tokenId = StorageUtil.getTokenId(getActivity());
        refreshLayout.setOnRefreshListener(new OnRefreshListener()
        {
            @Override
            public void onRefresh(RefreshLayout refreshlayout)
            {
                MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
                if (StorageUtil.getValue(getActivity(),"networks")!=null&&
                        StorageUtil.getValue(getActivity(),"networks").equals("no"))
                {
                    refreshlayout.finishRefresh(3000,false);
                }
            }
        });

    }

    private void getRongyunToken(String chat_id)
    {
        MinSuApi.getRongyunTokenq(getActivity(), 0x002, tokenId, Integer.parseInt(chat_id), callBack);
    }

    @Override
    protected void initData()
    {
//        MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
//        StorageUtil.setKeyValue(getActivity(),"istype","22");
//        StorageUtil.setKeyValue(getActivity(),"selectview","22");
//        IntentFilter myIntentFilter = new IntentFilter();
//        myIntentFilter.addAction("asd");
//        //注册广播
//        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);
//        if (chatListAdapter != null)
//        {
//            chatListAdapter.notifyDataSetChanged();
//        }

    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals("asd"))
            {
                tarid = intent.getExtras().getString("id");
                MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
                //Toast.makeText(getActivity(), "有新消息了", Toast.LENGTH_LONG).show();
              if (chatListAdapter!=null)
              {
                  chatListAdapter.notifyDataSetChanged();
              }
                RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                {
                    @Override
                    public void onSuccess(Integer integer)
                    {
                        EventBus.getDefault().post(new MessageEvent(integer));
                        StorageUtil.setKeyValue(getActivity(), "charm", integer + "");
                        charMessage.charmessage(integer);
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode)
                    {

                    }
                });
            }
        }

    };


    private void isReconnect(String token)
    {
        if (getActivity().getApplicationInfo().packageName.equals(App.getCurProcessName(getActivity().getApplicationContext())))
        {

            RongIM.connect(token, new RongIMClient.ConnectCallback()
            {
                @Override
                public void onTokenIncorrect()
                {
                    Log.i(TAG, "onTokenIncorrect: " + "融云连接失败");
                }

                @Override
                public void onSuccess(final String user_id)
                {
                    Log.i(TAG, "onSuccess: " + "融云连接成功");

                    RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                    {
                        @Override
                        public void onSuccess(Integer integer)
                        {
                            if (integer > 0)
                            {
                                EventBus.getDefault().post(new MessageEvent(integer));
                                charMessage.charmessage(integer);
                            }
                        }


                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode)
                        {
                            Log.i("aa", errorCode.getMessage());
                        }
                    });
                }
                @Override
                public void onError(RongIMClient.ErrorCode errorCode)
                {

                }
            });
        }
    }


    ChatBean chatBean;
    private CharRecordAdapter adapter;
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
                        if (refreshLayout != null)
                        {
                            refreshLayout.finishRefresh();
                        }
                        if (code == 200)
                        {
                            chatBean = new Gson().fromJson(result.body(), ChatBean.class);
                            chatListAdapter = new ChatListAdapter(R.layout.item_chat, chatBean.data);

                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                            //  adapter=new CharRecordAdapter(getActivity(),chatBean.data);
                            // recyclerView.getItemAnimator().setChangeDuration(0);
                            recyclerView.setAdapter(chatListAdapter);
                            ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
                            if (chatBean.data.size() == 0)
                            {
                                chatListAdapter.setEmptyView(R.layout.empty, recyclerView);
                            }
                            if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus()
                                    == RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)
                            {
                                RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                                {
                                    @Override
                                    public void onSuccess(Integer integer)
                                    {
                                        if (integer > 0)
                                        {
                                            EventBus.getDefault().post(new MessageEvent(integer));
                                            charMessage.charmessage(integer);
                                        }
                                    }


                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode)
                                    {
                                        Log.i("aa", errorCode.getMessage());
                                    }
                                });
                            }

                            chatListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener()
                            {
                                @Override
                                public void onItemClick(BaseQuickAdapter adapter, View view, final int position)
                                {

                                    if (RongIM.getInstance().getRongIMClient().getCurrentConnectionStatus() == RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)
                                    {
                                        isReconnect(tokenId);
                                        item = position;
                                        StorageUtil.setKeyValue(getActivity(), "chat_id", chatListAdapter.getItem(position).id + "");
                                        StorageUtil.setKeyValue(getActivity(), "chat_title", chatListAdapter.getItem(position).nickname);
                                        //RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE,chatBean.data.get(position).user_id+"",true);
                                        // MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
                                        getRongyunToken(chatListAdapter.getItem(item).id + "");
                                    } else
                                    {
                                        item = position;
                                        StorageUtil.setKeyValue(getActivity(), "chat_id", chatListAdapter.getItem(position).id + "");
                                        StorageUtil.setKeyValue(getActivity(), "chat_title", chatListAdapter.getItem(position).nickname);
                                        //RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE,chatBean.data.get(position).user_id+"",true);
                                        // MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
                                        getRongyunToken(chatListAdapter.getItem(position).id + "");
                                    }
                                }

                            });



                            chatListAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener()
                            {
                                @Override
                                public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position)
                                {
                                    item1 = position;
                                    LayoutInflater li = LayoutInflater.from(getActivity());
                                    View promptsView = li.inflate(R.layout.delete_message, null);
                                    promptsView.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    // set prompts.xml to alertdialog builder
                                    alertDialogBuilder.setView(promptsView);
                                    // create alert dialog
                                    final AlertDialog alertDialog = alertDialogBuilder.create();
                                    promptsView.setOnClickListener(new View.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(View view)
                                        {
                                            user_d = chatListAdapter.getItem(position).user_id;
                                            int id = chatListAdapter.getItem(position).id;
                                            MinSuApi.delete_char(id, 101, callBack);
                                            alertDialog.dismiss();
                                        }
                                    });
                                    alertDialogBuilder
                                            .setCancelable(true);
                                    // show it
                                    alertDialog.show();
                                    return false;
                                }
                            });
//

                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 0x002:
                    try
                    {
                        JSONObject jsonObject = new JSONObject(result.body());
                        int code = jsonObject.getInt("code");
                        if (code == 200)
                        {
                            JSONObject data1 = jsonObject.getJSONObject("data1");
                            JSONObject jsonObject1 = new JSONObject(data1.toString());
                            String token = jsonObject1.getString("token");
                            JSONObject data2 = jsonObject.getJSONObject("data2");
                            JSONObject jsonObject2 = new JSONObject(data2.toString());
                            String userId = jsonObject2.getString("userId");

                            RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, chatBean.data.get(item).user_id, new RongIMClient.ResultCallback<Boolean>()
                            {
                                @Override
                                public void onSuccess(Boolean aBoolean)
                                {
                                    //chatListAdapter.notifyItemRangeChanged(0, chatBean.data.size());
                                    chatListAdapter.notifyDataSetChanged();
                                    RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                                    {
                                        @Override
                                        public void onSuccess(Integer integer)
                                        {
                                            EventBus.getDefault().post(new MessageEvent(integer));
                                            charMessage.charmessage(integer);
                                            StorageUtil.setKeyValue(getActivity(), "charm", integer + "");
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
                            //RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE,userId,true);
                            isReconnect(token, userId);
                        }
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case 101:
                    try
                    {
                        JSONObject object = new JSONObject(result.body());
                        String code = object.getString("code");
                        if (code.equals("200"))
                        {
                            chatListAdapter.remove(item1);
                            chatListAdapter.notifyDataSetChanged();
                            RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, user_d, new RongIMClient.ResultCallback<Boolean>()
                            {
                                @Override
                                public void onSuccess(Boolean aBoolean)
                                {
                                    //chatListAdapter.notifyItemRangeChanged(0, chatBean.data.size());
                                    chatListAdapter.notifyDataSetChanged();
                                    RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                                    {
                                        @Override
                                        public void onSuccess(Integer integer)
                                        {
                                            EventBus.getDefault().post(new MessageEvent(integer));
                                            charMessage.charmessage(integer);
                                            StorageUtil.setKeyValue(getActivity(), "charm", integer + "");
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
                          //  MinSuApi.chatList(getActivity(), 0x001, tokenId, callBack);
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser)
    {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity!=null)
        {
            MinSuApi.chatList(activity,1,tokenId,callBack);
        }
    }
private Activity activity;
    @Override
    public void onAttach(Context context)
    {
        this.activity= (Activity) context;
        super.onAttach(context);
    }

    private void isReconnect(String token, final String userId)
    {
        if (getActivity().getApplicationInfo().packageName.equals(App.getCurProcessName(getActivity().getApplicationContext())))
        {

//            String chat_title = StorageUtil.getValue(getActivity(), "chat_title");
//            RongIM.getInstance().startPrivateChat(getActivity(), userId, chat_title);

            RongIM.connect(token, new RongIMClient.ConnectCallback()
            {
                @Override
                public void onTokenIncorrect()
                {

                }

                @Override
                public void onSuccess(String s)
                {
                    RongIM.getInstance().enableUnreadMessageIcon(true);//显示未读消息数目
//                UserInfo userInfo = new UserInfo(user_id,StorageUtil.getValue(getActivity(),"nickname"),Uri.parse(StorageUtil.getValue(getActivity(),"head_pic")));
//                RongIM.getInstance().setCurrentUserInfo(userInfo);
                    String chat_title = StorageUtil.getValue(getActivity(), "chat_title");
                    StorageUtil.setKeyValue(getActivity(), "tarid", userId);
                    RongIM.getInstance().startPrivateChat(getActivity(), userId, chat_title);

                    RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener()
                    {
                        @Override
                        public Message onSend(Message message)
                        {
                            Log.i(TAG, "onSend: " + message.getSenderUserId() + message.getTargetId());
                            //MessageContent content= message.getContent();
                            RongIMClient.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.PRIVATE, chatBean.data.get(item).user_id, new RongIMClient.ResultCallback<Boolean>()
                            {
                                @Override
                                public void onSuccess(Boolean aBoolean)
                                {
                                    RongIMClient.getInstance().getTotalUnreadCount(new RongIMClient.ResultCallback<Integer>()
                                    {
                                        @Override
                                        public void onSuccess(Integer integer)
                                        {
                                            EventBus.getDefault().post(new MessageEvent(integer));
                                            if (integer>0)
                                            {
                                                charMessage.charmessage(integer);
                                            }
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
                            return message;
                        }

                        @Override
                        public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode)
                        {
                            Log.i(TAG, "onSent: " + message + sentMessageErrorCode);
                            return false;
                        }
                    });
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode)
                {

                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        // RongIM.getInstance().disconnect();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private CharMessage    charMessage;

    public interface CharMessage
    {
        void charmessage(int number);
    }

    public void setOnCharMessageLinster(CharMessage charMessage)
    {
        this.charMessage = charMessage;
    }

}
