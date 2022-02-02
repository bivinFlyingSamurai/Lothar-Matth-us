package com.Matthaus.Lothar.keyboard.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.Matthaus.Lothar.keyboard.R;

/**
 * Created by Akshay Raj on 28/10/20 at 2:00 am.
 * support@snowcorp.org
 * www.snowcorp.org
 */

public class SnAudioPlayer {
    private Context mContext;
    private MediaPlayer mMediaPlayer;
    private boolean isPlaying = false;

    public SnAudioPlayer(Context context) {
        this.mContext = context;
    }

    public void stop() {
        isPlaying = false;
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play() {
        if (!isPlaying) {
            isPlaying = true;
            mMediaPlayer = MediaPlayer.create(mContext, R.raw.sn_gesture);
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> stop());
            mMediaPlayer.start();
        }
    }

}