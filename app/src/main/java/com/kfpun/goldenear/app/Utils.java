package com.kfpun.goldenear.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


public class Utils {
    public static Integer[] getMedias() {
        return new Integer[] {
            R.raw.onclassical_low,
            R.raw.onclassical_low,
            R.raw.onclassical_high,
            R.raw.onclassical_high,
        };
    }

    public static void getCredits(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Credits")
                .setMessage("Music from...")
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
