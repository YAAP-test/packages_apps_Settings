/*
 * Copyright (C) 2016 The Android Open Source Project
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

package com.android.settings.gestures;

import static android.provider.Settings.Secure.DOZE_PICK_UP_GESTURE;

import android.annotation.UserIdInt;
import android.content.Context;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.settings.core.TogglePreferenceController;
import com.android.settings.R;

public class PickupGesturePreferenceController extends TogglePreferenceController {

    private static final int ON = 1;
    private static final int OFF = 0;

    private static final String SECURE_KEY = DOZE_PICK_UP_GESTURE;

    private final String mPickUpPrefKey;

    private AmbientDisplayConfiguration mAmbientConfig;
    @UserIdInt
    private final int mUserId;

    public PickupGesturePreferenceController(Context context, String key) {
        super(context, key);
        mUserId = UserHandle.myUserId();
        mPickUpPrefKey = key;
    }

    public PickupGesturePreferenceController setConfig(AmbientDisplayConfiguration config) {
        mAmbientConfig = config;
        return this;
    }

    @Override
    public int getAvailabilityStatus() {
        // No hardware support for Pickup Gesture
        if (!getAmbientConfig().dozePickupSensorAvailable()) {
            return UNSUPPORTED_ON_DEVICE;
        }

        return AVAILABLE;
    }

    @Override
    public boolean isSliceable() {
        return TextUtils.equals(getPreferenceKey(), "gesture_pick_up");
    }

    @Override
    public boolean isPublicSlice() {
        return true;
    }

    @Override
    public boolean isChecked() {
        return getAmbientConfig().pickupGestureEnabled(mUserId);
    }

    @Override
    public String getPreferenceKey() {
        return mPickUpPrefKey;
    }

    @Override
    public boolean setChecked(boolean isChecked) {
        return Settings.Secure.putInt(mContext.getContentResolver(), SECURE_KEY,
                isChecked ? ON : OFF);
    }

    private AmbientDisplayConfiguration getAmbientConfig() {
        if (mAmbientConfig == null) {
            mAmbientConfig = new AmbientDisplayConfiguration(mContext);
        }

        return mAmbientConfig;
    }

    @Override
    public int getSliceHighlightMenuRes() {
        return R.string.menu_key_yasp;
    }
}
