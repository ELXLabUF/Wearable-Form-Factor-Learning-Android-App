package com.example.learning;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.MenuItem;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.Query;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    DatabaseReference databaseRef;
    private EditText txtEditor;
    private Toolbar toolbar;
    TextView txt;
    TextView txt_topic;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;
    MediaPlayer mp=new MediaPlayer();
    String g;
    int audioFile;
    public static DataSnapshot dbSnapShot ;
    public static ArrayList<String> addArray=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());
        //databaseRef=FirebaseDatabase.getInstance().getReference("database");
        HashMap<String, Object> defaults=new HashMap<>();
        txt=(TextView)findViewById(R.id.answer);

        txt_topic=(TextView)findViewById(R.id.topic);
        txtEditor=(EditText)findViewById(R.id.textbox);
        defaults.put("answer","We are here to help!");
        remoteConfig.setDefaults(defaults);

        final Task<Void> fetch=remoteConfig.fetch(0);
        fetch.addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                remoteConfig.activateFetched();
                //updateText();
                //to update the topic according to the firebase remote config value
                txt_topic=(TextView)findViewById(R.id.topic);
                String max1 =(String) remoteConfig.getString("topic");
                txt_topic.setText(max1);
                //query for fetching answer based on topic// equivalent sql query: SELECT * FROM database WHERE topic="xyz"

                Query query=FirebaseDatabase.getInstance().getReference("database").orderByChild("topic").equalTo(max1);
                //value event listener to data changed events
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren())
                            {
                                dbSnapShot=snapshot;
                                database db = snapshot.getValue(database.class);
                                //mp = MediaPlayer.create(MainActivity.this, audioFile);
                                //String str=db.getTopic();
                                g = db.getAnswer();
                                //audioFile=db.getAudio();
                               String audioFromBD=db.getAudio();//to fetch audio dynmically based on db audio value
                                if(audioFromBD.isEmpty() || audioFromBD==" ") //Checks if there is an audio key available in the database
                                {
                                    break;
                                }
                                audioFile=Integer.parseInt(audioFromBD);
                                //audioFile=R.raw.audio;
                                Playaudio(audioFile);
                            }
                            txt=(TextView)findViewById(R.id.answer);
                            txt.setText(g);
                            txt.setMovementMethod(ScrollingMovementMethod.getInstance());
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.d("DBVALUE","ERROR in database connection");
                    }
                });
    }
});
        setContentView(R.layout.activity_main);
        handler = new Handler();
        seekBar=(SeekBar) findViewById(R.id.seekbar);
        //int audiofile=R.raw.audio;

/*
        mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                playCycle();
                mp.start();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b){
                if(b)
                {
                    mp.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
            }
        });
*/
        txtEditor=(EditText)findViewById(R.id.textbox);
    }

    //to update the topic according to the firebase remote config value
    public void updateText()
    {
        //txt=(TextView)findViewById(R.id.answer);
        //String max =(String) remoteConfig.getString("answer");
        //txt.setText(max);
        txt_topic=(TextView)findViewById(R.id.topic);
        String max1 =(String) remoteConfig.getString("topic");
        txt_topic.setText(max1);
    }
    public void Playaudio(int audioFile)
    {
        //mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp = MediaPlayer.create(MainActivity.this, audioFile);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                seekBar.setMax(mp.getDuration());
                playCycle();
                mp.start();
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b){
                if(b)
                {
                    mp.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        //int id=item.getItemId();
       Intent intent=new Intent(this,history.class);
        startActivity(intent);
        //return true;
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected  void onResume(){
        super.onResume();
        mp.start();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        mp.stop();
    }

    @Override
    protected  void onDestroy(){
        super.onDestroy();
        mp.release();
        handler.removeCallbacks(runnable);
    }

    public void playCycle(){
        seekBar.setProgress(mp.getCurrentPosition());
        if(mp.isPlaying())
        {
            runnable=new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }
    /*
    //Play audio on play button click
    public void Playaudio(View view)
    {
        //set up MediaPlayer
        mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.start();
            seekBar.setMax(mp.getDuration());
    }*/

    public void pause(View view)
    {
        if(mp.isPlaying()) {
            mp.pause();
        }
        else
            mp.start();

    }

    //Method to save the notes taken and ands the note taken to the array which reflects the items into listview of history tab
    public void Save(View view) throws FileNotFoundException {
        FileOutputStream out=null;
        OutputStreamWriter outStreamWriter=null;
        try{
            String getTopic=txt_topic.getText().toString();
            String getAnswer=txt.getText().toString();
            String s=new String();
            s=s.concat(getTopic+"\n");
            s=s.concat(getAnswer+"\n");
            s=s.concat("Note:");
            String getInput=txtEditor.getText().toString();
            s=s.concat(getInput+"\n"+"\n");
            addArray.add(s);
            //out = openFileOutput("notes.txt", MODE_PRIVATE);
            Toast.makeText(this, "Answer is saved!",Toast.LENGTH_LONG).show();
            //FileOutputStream fos = openFileOutput("notes.txt",MODE_PRIVATE);
            out = openFileOutput("notes.txt",MODE_APPEND);//MODE_APPEND adds the string to the end of the file
            out.write(s.getBytes());
            //String f=MainActivity.this.getFilesDir().getAbsolutePath();//getAbsoluteFile();
            //Toast.makeText(this,f ,Toast.LENGTH_LONG).show();
            out.close();
            dbSnapShot.child("notes").getRef().setValue(getInput);
        }
        catch (Throwable t)
        {
            Toast.makeText(this, "Exception:"+t.toString(), Toast.LENGTH_LONG).show();
        }
            FileExists();
    }
    /*
    //loads the the answer and note in the Edit Text view
    public void load(View v)
    {
        FileInputStream fis=null;
        txtEditor.setText("loading");
            try{ fis = openFileInput("notes.txt");
                InputStreamReader isr=new InputStreamReader(fis);
                BufferedReader br=new BufferedReader(isr);
                StringBuilder sb=new StringBuilder();
                String text;
                txtEditor.setText("loading");
                while ((text=br.readLine())!=null){
                    sb.append(text).append("\n");
                }
                txtEditor.setText(sb.toString());
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }*/
//Checks if the saved file exists and adds to array
    public boolean FileExists()
    {
        File file=getBaseContext().getFileStreamPath("notes.txt");
        String s=((File) file).getAbsolutePath();
        //Toast.makeText(this, s,Toast.LENGTH_LONG).show();
        File file1= getBaseContext().getFileStreamPath("notes.txt");
        return file1.exists();
    }
}
