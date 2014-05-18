package com.kfpun.goldenear.app;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;


public class MainActivity extends ActionBarActivity {

    private MediaPlayer mediaPlayerWav;

    @InjectView(R.id.buttonWav)         Button buttonWav;
    @InjectView(R.id.buttonOk)          Button buttonOk;
    @InjectView(R.id.adView)            AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mediaPlayerWav = MediaPlayer.create(this, R.raw.onclassical_wav);
        mediaPlayerWav.setVolume(getVolume(), getVolume());
        Log.d(Constants.FILE_TAG, "getTrackInfo A: " + mediaPlayerWav.getDuration());

        buttonWav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerWav.isPlaying()) {
                    Log.d(Constants.EVENT_TAG, "pause A");
                    buttonWav.setText(getString(R.string.play_sound));
                    mediaPlayerWav.pause();
                } else {
                    Log.d(Constants.EVENT_TAG, "play A");
                    buttonWav.setText(getString(R.string.pause));
                    mediaPlayerWav.start();
                }
            }
        });

        buttonWav.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d(Constants.EVENT_TAG, "reset A");
                mediaPlayerWav.seekTo(0);
                return true;
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.EVENT_TAG, "play Ok");
                Intent intent = new Intent(view.getContext(), StartActivity.class);
                intent.putExtra("EXTRA_MESSAGE", "message");
                intent.putExtra("EXTRA_STATE", 0);
                startActivity(intent);
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
        if (mediaPlayerWav.isPlaying()) {
            mediaPlayerWav.pause();
            buttonWav.setText(getString(R.string.play_sound));
        }
        super.onStop();
        Log.d(Constants.EVENT_TAG, "onStop");

        EasyTracker.getInstance(this).activityStop(this);
    }

    private float getVolume () {
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        float actualVolume = (float) audioManager
                .getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = (float) audioManager
                .getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        return actualVolume / maxVolume;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_credits:
                Log.d(Constants.EVENT_TAG, "Click credits.");
                Utils.getCredits(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
