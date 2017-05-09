package com.green.goodsamaritan;

import com.parse.Parse;

/**
 * Created by faizahmed on 09/05/17.
 */

public class ApplicationSamaritan extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("32fPtajnvjAqna01SXG2x0WZoCMklarbqytxohGh")
                .clientKey("OTnlt4fsuZkrIss2nFQBV6i2WKJK6tsGxjbsYLY1")
                .server("https://parseapi.back4app.com/").build()
        );

    }
}