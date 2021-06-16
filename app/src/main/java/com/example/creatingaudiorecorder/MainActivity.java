package com.example.creatingaudiorecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int RECORD_REQUEST_CODE = 101;
    private static final int STORAGE_REQUEST_CODE = 102;

    private static MediaRecorder mediaRecorder;
    private static MediaPlayer mediaPlayer;

    private static String audioFilePath;
    private Button stopButton, playButton, recordButton;

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        audioSetup();

    }

    private void audioSetup() {

        recordButton = findViewById(R.id.btnRecord);
        playButton = findViewById(R.id.btnPlay);
        stopButton = findViewById(R.id.btnStop);

        if (!hasMicrophone()){
            stopButton.setEnabled(false);
            playButton.setEnabled(false);
            recordButton.setEnabled(false);
        } else {
            playButton.setEnabled(false);
            stopButton.setEnabled(false);
        }
        audioFilePath = this.getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/myaudio.3gp";
        requestPermission(Manifest.permission.RECORD_AUDIO,RECORD_REQUEST_CODE);
    }

    public void recordAudio(View view){
        isRecording = true;
        stopButton.setEnabled(true);
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(audioFilePath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
        } catch (Exception e){
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    public void stopAudio(View view){
        stopButton.setEnabled(false);
        playButton.setEnabled(true);

        if (isRecording){
            recordButton.setEnabled(false);
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
        else {
            mediaPlayer.release();
            mediaPlayer = null;
            recordButton.setEnabled(true);
        }
        Log.d("Stop", "stopAudio: "+ audioFilePath);
    }
    public void playAudio(View view) throws IOException {
        playButton.setEnabled(false);
        recordButton.setEnabled(false);
        stopButton.setEnabled(true);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(audioFilePath);
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    protected boolean hasMicrophone(){
        PackageManager packageManager = this.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    protected void requestPermission(String permissionType, int requestcode){
        int permission = ContextCompat.checkSelfPermission(this,permissionType);
        if(permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissionType}, requestcode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    recordButton.setEnabled(false);
                    Toast.makeText(this, "Record Permission Required", Toast.LENGTH_LONG).show();
                } else {
                    requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_REQUEST_CODE);
                }
                return;
            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    recordButton.setEnabled(false);
                    Toast.makeText(this, "External Storage Permission Required", Toast.LENGTH_LONG).show();
                }
            }
        }


    }
}


























































