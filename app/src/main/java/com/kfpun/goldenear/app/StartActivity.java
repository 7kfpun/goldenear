package com.kfpun.goldenear.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.google.analytics.tracking.android.EasyTracker;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class StartActivity extends ActionBarActivity {

    private MediaPlayer mediaPlayerCorrect;
    private MediaPlayer mediaPlayerIncorrect;
    private int state;

    @InjectView(R.id.textViewTitle)         TextView textViewTitle;
    @InjectView(R.id.textViewSubtitle)      TextView textViewSubtitle;
    @InjectView(R.id.buttonA)               Button buttonA;
    @InjectView(R.id.buttonB)               Button buttonB;
    @InjectView(R.id.buttonSelectA)         Button buttonSelectA;
    @InjectView(R.id.buttonSelectB)         Button buttonSelectB;
    @InjectView(R.id.adView)
    AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        // Set the hardware buttons to control the music
        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        ButterKnife.inject(this);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();
        state = intent.getIntExtra("EXTRA_STATE", 0);
        final int media = Utils.getMedias()[state];

        String subtitle = String.format(getString(R.string.subtitle_choose_better), getResources().getResourceEntryName(media));
        textViewSubtitle.setText(subtitle);

        mediaPlayerCorrect = MediaPlayer.create(this, R.raw.onclassical_wav);
        mediaPlayerCorrect.setVolume(getVolume(), getVolume());
        Log.d(Constants.FILE_TAG, "getTrackInfo mediaPlayerCorrect: " + mediaPlayerCorrect.getDuration());

        mediaPlayerIncorrect = MediaPlayer.create(this, media);
        mediaPlayerIncorrect.setVolume(getVolume(), getVolume());
        Log.d(Constants.FILE_TAG, "getTrackInfo mediaPlayerIncorrect: " + mediaPlayerIncorrect.getDuration());

        setButtons();
    }

    private void setButtons() {
        if (Math.random() < 0.5) {
            setButtonCorrect(buttonA);
            setButtonIncorrect(buttonB);
            setButtonSelectCorrect(buttonSelectA);
            setButtonSelectIncorrect(buttonSelectB);
        } else {
            setButtonCorrect(buttonB);
            setButtonIncorrect(buttonA);
            setButtonSelectCorrect(buttonSelectB);
            setButtonSelectIncorrect(buttonSelectA);
        }
    }

    private void setButtonCorrect (final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerCorrect.isPlaying()) {
                    pauseMediaPlayers();
                } else {
                    pauseMediaPlayers();
                    Drawable img = getResources().getDrawable( R.drawable.ic_pause );
                    button.setCompoundDrawables(img, null, null, null);
                    button.setText(getString(R.string.pause));
                    Log.d(Constants.EVENT_TAG, "play mediaPlayerCorrect");
                    mediaPlayerCorrect.start();
                }
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pauseMediaPlayers();
                Log.d(Constants.EVENT_TAG, "reset mediaPlayerCorrect");
                mediaPlayerCorrect.seekTo(0);
                return true;
            }
        });
    }

    private void setButtonIncorrect (final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayerIncorrect.isPlaying()) {
                    pauseMediaPlayers();
                } else {
                    pauseMediaPlayers();
                    Drawable img = getResources().getDrawable( R.drawable.ic_pause );
                    button.setCompoundDrawables(img, null, null, null);
                    button.setText(getString(R.string.pause));
                    Log.d(Constants.EVENT_TAG, "play mediaPlayerIncorrect");
                    mediaPlayerIncorrect.start();
                }
            }
        });

        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                pauseMediaPlayers();
                Log.d(Constants.EVENT_TAG, "reset mediaPlayerIncorrect");
                mediaPlayerIncorrect.seekTo(0);
                return true;
            }
        });
    }

    private void setButtonSelectCorrect (Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.EVENT_TAG, "click buttonSelectCorrect");
                pauseMediaPlayers();
                Toast.makeText(getApplication(), getString(R.string.right_answer), Toast.LENGTH_SHORT).show();

                Intent intent;
                int next_state = state + 1;
                if (next_state >= Utils.getMedias().length) {
                    Log.d(Constants.EVENT_TAG, "Game done!");
                    intent = new Intent(view.getContext(), ResultActivity.class);
                } else {
                    Log.d(Constants.EVENT_TAG, "next_state: " + next_state);
                    intent = new Intent(view.getContext(), StartActivity.class);
                }

                intent.putExtra("EXTRA_MESSAGE", "message");
                intent.putExtra("EXTRA_STATE", next_state);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // mediaPlayerCorrect.release();
                // mediaPlayerIncorrect.release();
                finish();
            }
        });
    }

    private void setButtonSelectIncorrect (Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(Constants.EVENT_TAG, "click buttonSelectIncorrect");
                pauseMediaPlayers();
                Toast.makeText(getApplication(), getString(R.string.wrong_answer), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(view.getContext(), ResultActivity.class);
                intent.putExtra("EXTRA_MESSAGE", "message");
                intent.putExtra("EXTRA_STATE", state);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                // mediaPlayerCorrect.release();
                // mediaPlayerIncorrect.release();
                finish();

            }
        });
    }

    private void pauseMediaPlayers() {

        Drawable img = getResources().getDrawable( R.drawable.ic_play );
        buttonA.setCompoundDrawables(img, null, null, null);
        buttonA.setText(getString(R.string.play_sound));
        buttonB.setCompoundDrawables(img, null, null, null);
        buttonB.setText(getString(R.string.play_sound));

        if (mediaPlayerCorrect.isPlaying()) {
            mediaPlayerCorrect.pause();
            Log.d(Constants.EVENT_TAG, "Stop mediaPlayerCorrect");
        }

        if (mediaPlayerIncorrect.isPlaying()) {
            mediaPlayerIncorrect.pause();
            Log.d(Constants.EVENT_TAG, "Stop mediaPlayerIncorrect");
        }
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
    public void onBackPressed() {
        pauseMediaPlayers();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //mediaPlayerCorrect.release();
                        //mediaPlayerIncorrect.release();
                        StartActivity.this.finish();
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
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    protected void onStop()
    {
        pauseMediaPlayers();
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
        Log.d(Constants.EVENT_TAG, "onStop");
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
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d(Constants.EVENT_TAG, "Click home back.");
                onBackPressed();
                return true;
            case R.id.action_shuffle:
                Toast.makeText(getApplication(), getString(R.string.shuffle), Toast.LENGTH_SHORT).show();
                pauseMediaPlayers();
                mediaPlayerCorrect.seekTo(0);
                mediaPlayerIncorrect.seekTo(0);
                setButtons();
                return true;
            case R.id.action_credits:
                Log.d(Constants.EVENT_TAG, "Click credits.");
                Utils.getCredits(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
