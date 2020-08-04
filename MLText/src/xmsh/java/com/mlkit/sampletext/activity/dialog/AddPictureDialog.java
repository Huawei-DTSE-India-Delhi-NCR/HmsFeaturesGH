/**
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.mlkit.sampletext.activity.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.hms.mltext.R;


public class AddPictureDialog extends Dialog implements View.OnClickListener {
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_CUSTOM = 2;
    private TextView tvTakePicture;
    private TextView tvSelectImage;
    private TextView tvExtend;
    private Context context;
    private ClickListener clickListener;
    private int type;

    public interface ClickListener {
        /**
         * Take picture
         */
        void takePicture();

        /**
         * Select picture from local
         */
        void selectImage();

        /**
         * Extension method
         */
        void doExtend();
    }

    public AddPictureDialog(Context context, int type) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initViews();
    }



    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this.context);
        @SuppressLint("InflateParams")
        View view = inflater.inflate(R.layout.dialog_add_picture, null);
        this.setContentView(view);

        this.tvTakePicture = view.findViewById(R.id.take_photo);
        this.tvSelectImage = view.findViewById(R.id.select_image);
        this.tvExtend = view.findViewById(R.id.extend);
        if (type == TYPE_CUSTOM) {
            this.tvExtend.setText(R.string.video_frame);
        }
        this.tvTakePicture.setOnClickListener(this);
        this.tvSelectImage.setOnClickListener(this);
        this.tvExtend.setOnClickListener(this);

        this.setCanceledOnTouchOutside(true);
        Window dialogWindow = this.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM;
            dialogWindow.setAttributes(layoutParams);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        if(this.clickListener == null){
            return;
        }
        int id = v.getId();
        if (id == R.id.take_photo) {
            this.clickListener.takePicture();
        } else if (id == R.id.select_image) {
            this.clickListener.selectImage();
        } else if (id == R.id.extend) {
            this.clickListener.doExtend();
        }
    }
}

