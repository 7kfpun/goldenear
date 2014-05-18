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

import javax.xml.transform.Result;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ResultActivity extends Activity {

    @InjectView(R.id.textViewSubtitle)          TextView textViewSubtitle;
    @InjectView(R.id.buttonRestart)             Button buttonRestart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        int last_state = intent.getIntExtra("EXTRA_STATE", 0);
        if (last_state >= Constants.getMedias().length) {
            Log.d(Constants.EVENT_TAG, "Finish game");
            textViewSubtitle.setText(getString(R.string.all_done));
        } else {
            Log.d(Constants.EVENT_TAG, "Lost at game: " + last_state);
            final int media = Constants.getMedias()[last_state];
            textViewSubtitle.setText(
                "You could not distinguish difference of WAV format and "
                + getResources().getResourceEntryName(media) + " bit of MP3."
            );
        }

        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResultActivity.this.finish();
            }
        });
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
