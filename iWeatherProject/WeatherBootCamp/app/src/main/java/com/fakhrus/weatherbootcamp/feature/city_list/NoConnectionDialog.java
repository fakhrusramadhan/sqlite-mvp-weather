package com.fakhrus.weatherbootcamp.feature.city_list;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.fakhrus.weatherbootcamp.R;

/**
 * Created by Fakhrus on 8/24/17.
 */

public class NoConnectionDialog {

    private Dialog dialog;
    private Activity activity;
    private ImageView iv_closeButton;

    public NoConnectionDialog(Activity activity) {
        this.activity = activity;
    }

    public void showNoConnectionDialog(){

        dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_no_connection);
        try {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception e){
            Log.e("Error",e.getMessage());
        }

        iv_closeButton = (ImageView) dialog.findViewById(R.id.iv_close_dialogNoConnection);
        iv_closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void dismissNoConnectionDialog(){
        dialog.dismiss();
    }

}
