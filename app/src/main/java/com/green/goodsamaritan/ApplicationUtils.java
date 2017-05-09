package com.green.goodsamaritan;

import android.app.Activity;
import android.content.Context;
import android.view.View;

/**
 * Created by faizahmed on 09/05/17.
 */

public class ApplicationUtils {
    public static void showLoading(Context c, boolean b) {
        ((Activity) c).findViewById(R.id.rl_loading).setVisibility(b ? View.VISIBLE : View.GONE);
    }
}
