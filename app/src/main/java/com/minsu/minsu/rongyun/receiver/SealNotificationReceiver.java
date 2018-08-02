package com.minsu.minsu.rongyun.receiver;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import com.minsu.minsu.App;
import com.minsu.minsu.common.MainActivity;
import com.minsu.minsu.utils.StorageUtil;
import com.minsu.minsu.utils.ToastManager;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

public class SealNotificationReceiver extends PushMessageReceiver {
    private String targetId = "";

//用来接收服务器发来的通知栏消息(消息到达客户端时触发)，
    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        StorageUtil.setKeyValue(context,"istype","1");
       // ToastManager.show("推送消息"+message.getPushContent());
        return true;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
//        targetId =  message.getTargetId();
//        Message msg = new Message();
//        msg.arg1 = 1;
//        Bundle bundle = new Bundle();
//        bundle.putString("targetId", targetId);
//        msg.setData(bundle);
//        //App.getHandler().sendMessage(msg);
//
//        Intent intent = new Intent();
//        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri.Builder uriBuilder = Uri.parse("rong://" + context.getPackageName()).buildUpon();
//        uriBuilder.appendPath("push_message")
//                .appendQueryParameter("targetId", targetId)
//                .appendQueryParameter("pushData", message.getPushData())
//                .appendQueryParameter("pushId", message.getPushId())
//                .appendQueryParameter("extra", message.getExtra());
//        Uri uri=uriBuilder.build();
//        intent.setData(uri);
//        context.startActivity(intent);
//
  //      Log.i("aaaaaaaaaaaaa",message.getPushId());
        Intent intent=new Intent(context, MainActivity.class);
        intent.putExtra("selectview","1");
        StorageUtil.setKeyValue(context,"istype","1");
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        StorageUtil.setKeyValue(context,"selectview","2");
        return true;
    }
}