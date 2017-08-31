package com.fakhrus.weatherbootcamp.feature.main;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;

import com.fakhrus.weatherbootcamp.R;

/**
 * Created by Fakhrus on 8/24/17.
 */

public class LoadingDialog {

    private Dialog dialog;
    private Activity activity;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
    }

    public void showLoadingDialog(){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_loading);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e){
            Log.e("Error",e.getMessage());
        }


        dialog.show();
    }

    public void dismissLoadingDialog(){
        dialog.dismiss();
    }
}
