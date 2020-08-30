package project.game.reeldeal.model;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.SoundPool;

import project.game.reeldeal.R;

public final class SoundEffect {

    public static int FOUND;
    public static int SCAN;

    private SoundEffect() {
        throw new IllegalAccessError("Cannot access constructor");
    }

    public static SoundPool buildSoundPool(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        return new SoundPool.Builder()
                .setAudioAttributes(audioAttributes)
                .build();
    }

    public static void loadSounds(Context context, SoundPool soundPool){
        FOUND = soundPool.load(context, R.raw.sonar_high, 1);
        SCAN = soundPool.load(context, R.raw.sonar_low, 1);
    }

    public static void playSound(SoundPool soundPool, int sound){
        soundPool.play(sound, 1, 1, 0, 0, 1);
    }
}
