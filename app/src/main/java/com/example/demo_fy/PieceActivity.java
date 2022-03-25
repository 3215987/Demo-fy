package com.example.demo_fy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PieceActivity extends AppCompatActivity {
    Piece p;
    private MediaRecorder recorder;
    private MediaPlayer player;
    private static String mFileName = null;
    private boolean recording = false;  // keeping track for togglers
    private boolean playing = false;    //^^
    private boolean recorderSetup = false;
    private static int numRecordings = 0;

    private RecyclerView recyclerView;  //for UI list
    private ArrayList<Session> recyclerList;
    private Uri selectedRecording;

    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piece);
        recyclerView = findViewById(R.id.recordingsView);
        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // create new piece p with name from textbox
        p = new Piece(message);
        recyclerList = new ArrayList<Session>();
        //setAdapter();
    }

    /**
     * configures adapter for recording recyclerview
     */
    private void setAdapter() {
        recyclerAdapterRecordings adapter = new recyclerAdapterRecordings(recyclerList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    /**
     * check audio recording permissions
     * @return returns true if permission is granted, false if not
     */
    public boolean CheckPermissions() {
        // this method is used to check permission
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * prompts user for permission to record
     */
    private void RequestPermissions() {
        // this method is used to request the
        // permission for audio recording and storage.
        ActivityCompat.requestPermissions(PieceActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    /**
     * begins taking input from mic
     */
    private void startRecording() {
        // check permission method is used to check
        // that the user has granted permission
        // to record nd store the audio.
            if (CheckPermissions()) {
                // we are here initializing our filename variable
                // with the path of the recorded audio file.
                mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                mFileName += "/AudioRecording" + numRecordings + ".3gp";

                // below method is used to initialize
                // the media recorder clss
                recorder = new MediaRecorder();

                // below method is used to set the audio
                // source which we are using a mic.
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

                // below method is used to set
                // the output format of the audio.
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

                // below method is used to set the
                // audio encoder for our recorded audio.
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                // below method is used to set the
                // output file location for our recorded audio
                recorder.setOutputFile(mFileName);
                try {
                    // below method will prepare
                    // our audio recorder class
                    recorder.prepare();
                } catch (IOException e) {
                    Log.e("TAG", "prepare() failed");
                }
                // start method will start
                // the audio recording.
                recorder.start();
                recording = true;
                recorderSetup = true;
            } else {
                // if audio recording permissions are
                // not granted by user below method will
                // ask for runtime permission for mic and storage.
                RequestPermissions();
            }

    }

    /**
     * stops recording
     */
    public void pauseRecording() {

        // below method will stop
        // the audio recording.
        recorder.stop();
        recorder.release();
        recorder = null;

        numRecordings++;
        recording = false;
        File f = new File(mFileName);
        Session s = new Session(("Recording " + numRecordings + ""), f);
        p.addSession(s);
        // add session with audio recording. name should be the recording number
        recyclerList.add(s);
        if(recorderSetup)
            setAdapter();
        // adding the session to the recycler screen as well

    }

    /**
     * toggles recording on / off
     */
    public void toggleRecording(View view){
        if(recording) {
            this.pauseRecording();
        }
        else {
            this.startRecording();
        }
    }

    /**
     * method to begin playing audio
     * TODO: allow selection of part to be played back
     */
    public void playAudio() {

        // for playing our recorded audio
        // we are using media player class.
        player = new MediaPlayer();
        try {
            // below method is used to set the
            // data source which will be our file name
            player.setDataSource(getApplicationContext(), selectedRecording);

            // below method will prepare our media player
            player.prepare();

            // below method will start our media player.
            player.start();
            playing = true;
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    /**
     * pauses audio playback by releasing media player
     */
    public void pausePlaying() {
        // this method will release the media player
        // class and pause the playing of our recorded audio.
        player.release();
        player = null;
        playing = false;

    }

    /**
     * toggles audio playing on / off
     */
    public void togglePlaying(View view) {
        if(playing){
            this.pausePlaying();
        } else
            this.playAudio();
    }

    /**
     * selecting which list element is being played
     * @param view the recyclerview element tapped upon
     */
    public void selectRecording(View view) {
        int pos = recyclerView.getChildAdapterPosition(view);
        this.selectedRecording = Uri.fromFile(recyclerList.get(pos).getRecording());
        // making an uri from the file in position pos of the array

        //below: sets it so that only the selected session is purple
        for(int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.teal_200));
        }
        view.setBackgroundColor(getResources().getColor(R.color.purple_200));
    }


}