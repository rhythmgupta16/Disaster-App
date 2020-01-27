package com.example.disasterapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import static android.Manifest.permission.CALL_PHONE;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class FirstHomeFragment extends Fragment {
    Button btnEmergency, btnAlarm, btnStopAudio, btnContinue, btnVoice;
    MediaPlayer mp;
    private Camera mCamera;
    private Camera.Parameters parameters;
    private CameraManager camManager;
    private Context context;

    private final int REQ_CODE = 100;


    //Sensor
    private SensorManager mSensorManager;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    AudioManager audioManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_home,container, false);


        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA},
                    50); }


        //Sensor
        mSensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        Objects.requireNonNull(mSensorManager).registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 10f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        btnEmergency = view.findViewById(R.id.btnEmergency);
        btnAlarm = view.findViewById(R.id.btnAlarm);
        btnStopAudio = view.findViewById(R.id.btnStopAudio);
        btnContinue = view.findViewById(R.id.btnContinue);
        btnVoice = view.findViewById(R.id.btnVoice);
        btnStopAudio.setVisibility(View.INVISIBLE);
        audioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);

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
            startAlarm();

            }
        });

        btnVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voiceRecognition();
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

                //Flash off
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    CameraManager camManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
                    String cameraId = null;
                    try {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, false);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Main2Activity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void voiceRecognition()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Need to speak");
        try {
            startActivityForResult(intent, REQ_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Sorry your device not supported",
                    Toast.LENGTH_SHORT).show();
        }
    }


    private void startAlarm(){
        //Flash on
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            CameraManager camManager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
            String cameraId = null;
            try {
                cameraId = camManager.getCameraIdList()[0];
                camManager.setTorchMode(cameraId, true); //Turn ON
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        //Increase Volume
        for (int i=0;i<20;i++){
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

    //Sensor
    private final SensorEventListener mSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta;
            if (mAccel > 12) {
                Toast.makeText(getContext(), "Shake Detected", Toast.LENGTH_SHORT).show();
                Log.i(TAG, "onSensorChanged: SHAKE DETECTED");
                startAlarm();
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    public void onResume() {
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        super.onResume();
    }
    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Toast.makeText(getContext(),"Add action for "+result.get(0) ,Toast.LENGTH_SHORT).show();
                    //textView.setText(result.get(0));
                }
                break;
            }
        }
    }

}