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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
    private EditText txtEditor;
    private Toolbar toolbar;
    TextView txt;
    TextView txt_topic;
    SeekBar seekBar;
    Handler handler;
    Runnable runnable;
    MediaPlayer mp;
    public static ArrayList<String> addArray=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        remoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(true).build());
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
        txt_topic=(TextView)findViewById(R.id.topic);
        String max1 =(String) remoteConfig.getString("topic");
        txt_topic.setText(max1);
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

    /*//Play audio on play button click
    public void Playaudio(View view)
    {
        //set up MediaPlayer
        mp = MediaPlayer.create(MainActivity.this, R.raw.audio);
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mp.start();
            seekBar.setMax(mp.getDuration());
    }*/
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
            Toast.makeText(this, "file created!",Toast.LENGTH_LONG).show();
            //FileOutputStream fos = openFileOutput("notes.txt",MODE_PRIVATE);
            out = openFileOutput("notes.txt",MODE_APPEND);
            out.write(s.getBytes());
            out.close();
            //Toast.makeText(this, "Notes Saved", Toast.LENGTH_LONG).show();
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
/*Opens the saved file
    public void Open(String fileName) {
        String content = "";
        if (FileExists()) {
            try {
                InputStream in = openFileInput(fileName);
                if ( in != null) {
                    InputStreamReader tmp = new InputStreamReader( in );
                    BufferedReader reader = new BufferedReader(tmp);
                    String str;
                    StringBuilder buf = new StringBuilder();
                    while ((str = reader.readLine()) != null) {
                        buf.append(str + "\n");
                    } in .close();
                    content = buf.toString();
                    Toast.makeText(this, content,Toast.LENGTH_LONG).show();
                }
            } catch (java.io.FileNotFoundException e) {} catch (Throwable t) {
                Toast.makeText(this, "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
            }
        }
        txt=(TextView)findViewById(R.id.answer1);
        txt.setText(content);
        //return content;
    }*/
}
