package com.kfpun.goldenear.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ResultActivity extends Activity {

    @InjectView(R.id.textViewTitle)         TextView textViewTitle;
    @InjectView(R.id.textViewSubtitle)      TextView textViewSubtitle;
    @InjectView(R.id.buttonRestart)         Button buttonRestart;
    @InjectView(R.id.adView)                AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.inject(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();
        int last_state = intent.getIntExtra("EXTRA_STATE", 0);

        if (last_state >= Utils.getMedias().length) {
            Log.d(Constants.EVENT_TAG, "Finish game");
            textViewTitle.setText(getString(R.string.title_all_done));
            textViewSubtitle.setText(getString(R.string.subtitle_all_done));
        } else {
            Log.d(Constants.EVENT_TAG, "Lost at game: " + last_state);
            textViewTitle.setText(getString(R.string.title_fail));

            final int media = Utils.getMedias()[last_state];
            String result = String.format(getString(R.string.subtitle_fail), getResources().getResourceEntryName(media));
            textViewSubtitle.setText(result);
        }

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), StartActivity.class);

                intent.putExtra("EXTRA_MESSAGE", "message");
                intent.putExtra("EXTRA_STATE", 0);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ResultActivity.this.finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.result, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_credits) {
            Log.d(Constants.EVENT_TAG, "Click credits.");
            Utils.getCredits(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
