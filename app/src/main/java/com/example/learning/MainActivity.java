package com.example.learning;
//package com.google.firbase;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.media.MediaPlayer;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    private EditText txtEditor;
    private Toolbar toolbar;
    //Firebase fb;
    TextView txt;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //fb.initializeApp(MainActivity.this);
        //database = FirebaseDatabase.getInstance();
        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());
        HashMap<String, Object> defaults=new HashMap<>();
        txt=(TextView)findViewById(R.id.answer);
        defaults.put("answer","there");
        remoteConfig.setDefaults(defaults);

        final Task<Void> fetch=remoteConfig.fetch(0);
        fetch.addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                remoteConfig.activateFetched();
                updateText();
            }
        });

        setContentView(R.layout.activity_main);
        //toolbar=(Toolbar)findViewById(R.id.history);
        //setSupportActionBar(toolbar);
        handler=new Handler();
        seekBar=(SeekBar) findViewById(R.id.seekbar);
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
        txtEditor=(EditText)findViewById(R.id.textbox);

    }
    public void updateText()
    {
        txt=(TextView)findViewById(R.id.answer);
        String max =(String) remoteConfig.getString("answer");
        txt.setText(max);
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

    public void Playaudio(View view)
    {
        //set up MediaPlayer
        mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);

        //if(mp.isPlaying()){
        //    mp.seekTo(0);
        //}
        //else
        //{
            mp.start();
            seekBar.setMax(mp.getDuration());
        //}
        /*try {
            //mp.setDataSource(path + File.separator + fileName);
            Toast.makeText(this, "Audio started 1", Toast.LENGTH_LONG).show();
            mp.setDataSource("C:\\Users\\nehar\\AndroidStudioProjects\\Learning\\Audio");
            Toast.makeText(this, "Audio started 2", Toast.LENGTH_LONG).show();
            mp.prepare();
            mp.start();
            Toast.makeText(this, "Audio started 3", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public void Save(View view) throws FileNotFoundException {

        try{
            OutputStreamWriter out = new OutputStreamWriter(openFileOutput("notes.txt",0));
            out.write(txtEditor.getText().toString());
            out.close();
            Toast.makeText(this, "Notes Saved", Toast.LENGTH_LONG).show();
        }
        catch (Throwable t)
        {
            Toast.makeText(this, "Exception:"+t.toString(), Toast.LENGTH_LONG).show();
        }
        FileExists();
    }

    public void FileExists()
    {
        File file=getBaseContext().getFileStreamPath("notes.txt");
        String s=((File) file).getAbsolutePath();
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();
    }


}
