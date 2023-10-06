package com.jxy.realTimeTranslation;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.jxy.realTimeTranslation.service.MediaService;

public class JxyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        startService(new Intent(this, MediaService.class));
    }
}
