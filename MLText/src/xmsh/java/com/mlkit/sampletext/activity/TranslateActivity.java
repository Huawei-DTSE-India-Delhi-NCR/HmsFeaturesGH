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

package com.mlkit.sampletext.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.hms.availabletoalllbraries.BaseActivity;
import com.hms.mltext.R;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.hms.mlsdk.common.MLApplication;


public class TranslateActivity extends BaseActivity implements View.OnClickListener {

    public TranslateActivity() {
        super(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate);

        AGConnectServicesConfig config = AGConnectServicesConfig.fromContext(getApplication());
        MLApplication.getInstance().setApiKey(config.getString("client/api_key"));

        findViewById(R.id.rl_online).setOnClickListener(this);
        findViewById(R.id.rl_offline).setOnClickListener(this);
        findViewById(R.id.back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.rl_online){
            startActivity(new Intent(TranslateActivity.this, RemoteTranslateActivity.class));
        }else if(view.getId() == R.id.rl_offline){
            startActivity(new Intent(TranslateActivity.this, LocalTranslateActivity.class));
        }else if(view.getId() == R.id.back){
            finish();
        }
    }
}
