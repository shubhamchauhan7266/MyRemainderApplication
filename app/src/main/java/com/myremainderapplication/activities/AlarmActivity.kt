package com.myremainderapplication.activities

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.myremainderapplication.R
import kotlinx.android.synthetic.main.activity_alarm.*
import android.media.MediaPlayer
import android.net.Uri
import android.media.RingtoneManager
import android.media.AudioManager
import java.io.IOException


class AlarmActivity : AppCompatActivity() {
    private var mMediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        btStopAlarm.setOnClickListener{
            mMediaPlayer!!.stop()
            finish()
        }

        playSound(this, getAlarmUri())
    }

    private fun playSound(context: AlarmActivity, alarmUri: Uri?) {
        mMediaPlayer = MediaPlayer()
        try {
            mMediaPlayer!!.setDataSource(context, alarmUri)
            val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer!!.setAudioStreamType(AudioManager.STREAM_ALARM)
                mMediaPlayer!!.prepare()
                mMediaPlayer!!.start()
            }
        } catch (e: IOException) {
            println("OOPS")
        }

    }

    private fun getAlarmUri(): Uri? {
        var alert: Uri? = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM)
        if (alert == null) {
            alert = RingtoneManager
                    .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            if (alert == null) {
                alert = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_RINGTONE)
            }
        }
        return alert
    }
}
