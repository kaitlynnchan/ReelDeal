package project.game.reeldeal.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import project.game.reeldeal.R;

public class SettingsActivity extends AppCompatActivity {

    public static Intent makeLaunchIntent(Context context){
        return new Intent(context, SettingsActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setupToggleButton();
        setupBackButton();
    }

    private void setupToggleButton() {
        ToggleButton toggleSoundEffects = findViewById(R.id.toggle_sound_effects);
        final AudioManager manager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        toggleSoundEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    manager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_ALARM,
                            AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
                            AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_RING,
                            AudioManager.ADJUST_UNMUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_DTMF,
                            AudioManager.ADJUST_UNMUTE, 0);
                } else {
                    manager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
                            AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_ALARM,
                            AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION,
                            AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_RING,
                            AudioManager.ADJUST_MUTE, 0);
                    manager.adjustStreamVolume(AudioManager.STREAM_DTMF,
                            AudioManager.ADJUST_MUTE, 0);
                }
            }
        });

        toggleSoundEffects.setChecked(!manager.isStreamMute(AudioManager.STREAM_MUSIC));
    }

    private void setupBackButton() {
        Button buttonBack = findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}