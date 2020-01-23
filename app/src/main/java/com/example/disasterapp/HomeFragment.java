package com.example.disasterapp;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import static android.Manifest.permission.CALL_PHONE;

public class HomeFragment extends Fragment {
    Button btnEmergency, btnAlarm, btnStopAudio;
    MediaPlayer mp;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container, false);



        btnEmergency = view.findViewById(R.id.btnEmergency);
        btnAlarm = view.findViewById(R.id.btnAlarm);
        btnStopAudio = view.findViewById(R.id.btnStopAudio);
        btnStopAudio.setVisibility(View.INVISIBLE);
        final AudioManager audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

        mp = MediaPlayer.create(getContext(), R.raw.alarm);

        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + "123456789" ));

                if (ContextCompat.checkSelfPermission(getActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    startActivity(callIntent);
                } else {
                    requestPermissions(new String[]{CALL_PHONE}, 1);
                }
            }
        });

        btnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Increase Volume
                for (int i=0;i<5;i++){
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
                }

                //Start Playing audio
                try {
                    if (mp.isPlaying()) {
                        mp.stop();
                        mp.release();
                        mp = MediaPlayer.create(getContext(), R.raw.alarm);
                    } mp.start();
                    btnStopAudio.setVisibility(View.VISIBLE);
                } catch(Exception e) { e.printStackTrace(); }
            }
        });

        btnStopAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStopAudio.setVisibility(View.INVISIBLE);
                //Stop audio
                mp.stop();
                mp.release();
                mp = MediaPlayer.create(getContext(), R.raw.alarm);
            }
        });

        return view;
    }


}