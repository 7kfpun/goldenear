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
import android.widget.RadioButton;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends ActionBarActivity {

    private MediaPlayer mediaPlayerWav;

    @InjectView(R.id.buttonWav)               Button buttonWav;
    @InjectView(R.id.buttonOk)              Button buttonOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        mediaPlayerWav = MediaPlayer.create(this, R.raw.onclassical_wav);
        mediaPlayerWav.setVolume(getVolume(), getVolume());
        Log.d(Constants.FILE_TAG, "getTrackInfo A: " + mediaPlayerWav.getDuration());

        buttonWav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.EVENT_TAG, "play A");
                mediaPlayerWav.start();
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
                Log.d(Constants.EVENT_TAG, "play A");
                Intent intent = new Intent(view.getContext(), StartActivity.class);
                intent.putExtra("EXTRA_MESSAGE", "message");
                intent.putExtra("EXTRA_STATE", 0);
                startActivity(intent);
            }
        });
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
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
