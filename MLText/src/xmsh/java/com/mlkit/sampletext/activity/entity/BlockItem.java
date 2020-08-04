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

package com.mlkit.sampletext.activity.entity;
import android.graphics.Rect;

/**
 * Re-encapsulate the return result of OCR in Block
 *
 * @since 2020-03-12
 */
public class BlockItem {
    public final String text;
    public final Rect rect;

    public BlockItem(String text, Rect rect) {
        this.text = text;
        this.rect = rect;
    }
}
