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

package com.hms.landmarklocations.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.util.Log;

import com.hms.landmarklocations.LandMarkActivity;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.mlsdk.MLAnalyzerFactory;
import com.huawei.hms.mlsdk.common.MLFrame;
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmark;
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmarkAnalyzer;
import com.huawei.hms.mlsdk.landmark.MLRemoteLandmarkAnalyzerSetting;
import com.mlkit.sampletext.camera.FrameMetadata;
import com.mlkit.sampletext.transactor.BaseTransactor;
import com.mlkit.sampletext.util.Constant;
import com.mlkit.sampletext.views.graphic.RemoteLandmarkGraphic;
import com.mlkit.sampletext.views.overlay.GraphicOverlay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandmarkTransactor extends BaseTransactor<List<MLRemoteLandmark>> {

    private static final String TAG = "LandmarkTransactor";

    private final MLRemoteLandmarkAnalyzer detector;

    private Handler handler;

    LandMarkActivity.GetLocationDetails getLocationDetails;

    public LandmarkTransactor(Handler handler, LandMarkActivity.GetLocationDetails getLocationDetails) {
        super();
        this.detector = MLAnalyzerFactory.getInstance().getRemoteLandmarkAnalyzer();
        this.handler = handler;
        this.getLocationDetails=getLocationDetails;
    }

    public LandmarkTransactor(MLRemoteLandmarkAnalyzerSetting options) {
        super();
        this.detector = MLAnalyzerFactory.getInstance().getRemoteLandmarkAnalyzer(options);
    }

    @Override
    public void stop() {
        super.stop();
        try {
            this.detector.close();
        } catch (IOException e) {
            Log.e(LandmarkTransactor.TAG, "Exception thrown while trying to close remote landmark transactor: " + e.getMessage());
        }
    }

    @Override
    protected Task<List<MLRemoteLandmark>> detectInImage(MLFrame image) {
        return this.detector.asyncAnalyseFrame(image);
    }

    @Override
    protected void onSuccess(
            Bitmap originalCameraImage,
            List<MLRemoteLandmark> results,
            FrameMetadata frameMetadata, GraphicOverlay graphicOverlay) {

        ArrayList<String> hospitalTitle= new ArrayList<String>();
        ArrayList<String> hospitalLat=new ArrayList<String>();
        ArrayList<String> hospitalLong=new ArrayList<String>();

        if (results != null && !results.isEmpty()) {
            graphicOverlay.clear();
            for (MLRemoteLandmark landmark : results) {
                RemoteLandmarkGraphic landmarkGraphic = new RemoteLandmarkGraphic(graphicOverlay, landmark);
                graphicOverlay.addGraphic(landmarkGraphic);

                hospitalTitle.add(landmark.getLandmark());
                hospitalLat.add(landmark.getPositionInfos().size()>0?landmark.getPositionInfos().get(0).getLat()+"":null);
                hospitalLong.add(landmark.getPositionInfos().size()>0?landmark.getPositionInfos().get(0).getLng()+"":null);
            }
            graphicOverlay.postInvalidate();
        }

        getLocationDetails.getLocationTitles(hospitalTitle);
        getLocationDetails.getLocationLat(hospitalLat);
        getLocationDetails.getLocationLong(hospitalLong);

        this.handler.sendEmptyMessage(Constant.GET_DATA_SUCCESS);
    }

    @Override
    protected void onFailure(Exception e) {
        Log.e(LandmarkTransactor.TAG, "Remote landmark detection failed: " + e.getMessage());
        this.handler.sendEmptyMessage(Constant.GET_DATA_FAILED);
    }
}
