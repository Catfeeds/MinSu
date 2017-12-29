/*
 * Copyright 2016 jeasonlzy(廖子尧)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zhuye.minsu.api.callback;

import android.app.Activity;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;
import com.zhuye.minsu.widget.CustomProgressDialog;

/**
 * ================================================
 * 加载弹框
 * ================================================
 */
public abstract class StringDialogCallback extends StringCallback {

    private CustomProgressDialog dialog;

    public StringDialogCallback(Activity activity, String content) {
//        dialog = new ProgressDialog(activity);
        dialog = new CustomProgressDialog(activity, content);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.layout_progress);
        dialog.setCanceledOnTouchOutside(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//        dialog.setMessage(content);
    }

    @Override
    public void onStart(Request<String, ? extends Request> request) {
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
