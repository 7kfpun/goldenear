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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class StartActivity extends ActionBarActivity {

    private MediaPlayer mediaPlayerCorrect;
    private MediaPlayer mediaPlayerNotCorrect;

    @InjectView(R.id.layoutCorrect)             LinearLayout layoutCorrect;
    @InjectView(R.id.layoutIncorrect)           LinearLayout layoutIncorrect;
    @InjectView(R.id.buttonCorrect)             Button buttonCorrect;
    @InjectView(R.id.buttonIncorrect)           Button buttonIncorrect;
    @InjectView(R.id.buttonSelectCorrect)       Button buttonSelectCorrect;
    @InjectView(R.id.buttonSelectIncorrect)     Button buttonSelectIncorrect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ButterKnife.inject(this);

        Intent intent = getIntent();
        final int state = intent.getIntExtra("EXTRA_STATE", 0);
        final int incorrect = getIncorrects()[state];

        mediaPlayerCorrect = MediaPlayer.create(this, R.raw.onclassical_wav);
        mediaPlayerCorrect.setVolume(getVolume(), getVolume());
        Log.d(Constants.FILE_TAG, "getTrackInfo mediaPlayerCorrect: " + mediaPlayerCorrect.getDuration());

        mediaPlayerNotCorrect = MediaPlayer.create(this, incorrect);
        mediaPlayerNotCorrect.setVolume(getVolume(), getVolume());
        Log.d(Constants.FILE_TAG, "getTrackInfo mediaPlayerNotCorrect: " + mediaPlayerNotCorrect.getDuration());

        buttonCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerNotCorrect.isPlaying()) {
                    Log.d(Constants.EVENT_TAG, "mediaPlayerNotCorrect was playing and now is stop");
                    mediaPlayerNotCorrect.pause();
                }
                Log.d(Constants.EVENT_TAG, "play mediaPlayerCorrect");
                mediaPlayerCorrect.start();
            }
        });

        buttonCorrect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mediaPlayerNotCorrect.isPlaying()) {
                    Log.d(Constants.EVENT_TAG, "mediaPlayerNotCorrect is playing and now is stop");
                    mediaPlayerNotCorrect.pause();
                }
                Log.d(Constants.EVENT_TAG, "reset mediaPlayerCorrect");
                mediaPlayerCorrect.seekTo(0);
                return true;
            }
        });

        buttonIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerCorrect.isPlaying()) {
                    Log.d(Constants.EVENT_TAG, "mediaPlayerCorrect is playing and now is stop");
                    mediaPlayerCorrect.pause();
                }
                Log.d(Constants.EVENT_TAG, "play mediaPlayerNotCorrect");
                mediaPlayerNotCorrect.start();
            }
        });

        buttonIncorrect.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mediaPlayerCorrect.isPlaying()) {
                    Log.d(Constants.EVENT_TAG, "mediaPlayerCorrect is playing and now is stop");
                    mediaPlayerCorrect.pause();
                }
                Log.d(Constants.EVENT_TAG, "reset mediaPlayerNotCorrect");
                mediaPlayerNotCorrect.seekTo(0);
                return true;
            }
        });

        buttonSelectCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerCorrect.pause();
                mediaPlayerNotCorrect.pause();
                Log.d(Constants.EVENT_TAG, "click buttonSelectCorrect");

                int next_state = state + 1;
                if (next_state >= getIncorrects().length) {
                    Log.d(Constants.EVENT_TAG, "Game done!");
                } else {
                    Log.d(Constants.EVENT_TAG, "next_state: " + next_state);
                    Intent intent = new Intent(view.getContext(), StartActivity.class);
                    intent.putExtra("EXTRA_MESSAGE", "message");
                    intent.putExtra("EXTRA_STATE", next_state);
                }
            }
        });

        buttonSelectIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayerCorrect.pause();
                mediaPlayerNotCorrect.pause();
                Log.d(Constants.EVENT_TAG, "click buttonSelectIncorrect");
            }
        });

        shuffleButtons();
    }

    private void shuffleButtons(){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) buttonIncorrect.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        params.addRule(RelativeLayout.LEFT_OF, R.id.buttonCorrect);

        buttonIncorrect.setLayoutParams(params);
    }

    private Integer[] getIncorrects() {
        Integer[] games = {R.raw.onclassical_low, R.raw.onclassical_high, R.raw.onclassical_flac};
        return games;
    }

    private float getVolume() {
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
        getMenuInflater().inflate(R.menu.start, menu);
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
