package com.example.learning;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.learning.MainActivity;
import android.widget.ListView;
import android.text.method.ScrollingMovementMethod;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import android.widget.TextView;

public class history extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FileInputStream fis=null;
        //ListView txt = (ListView) findViewById(R.id.savedNotes1);
        TextView txt_notes = (TextView) findViewById(R.id.savedNotes);
        txt_notes.setMovementMethod(new ScrollingMovementMethod());
        try{ fis = openFileInput("notes.txt");
            InputStreamReader isr=new InputStreamReader(fis);
            BufferedReader br=new BufferedReader(isr);
            StringBuilder sb=new StringBuilder();
            String text;

            while ((text=br.readLine())!=null){
                sb.append(text).append("\n");
            }
            //txt.setText(sb.toString());
            txt_notes.setText(sb.toString());
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //ListView txt = (ListView) findViewById(R.id.savedNotes1);
        //ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.addArray);
        //txt.setAdapter(adapter);
    }
    @Override
    public  boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return  true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.history:
                Intent intent = new Intent(this, history.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void FileExists()
    {
        File file=getBaseContext().getFileStreamPath("notes.txt");
        String s=((File) file).getAbsolutePath();
        Toast.makeText(this, s,Toast.LENGTH_LONG).show();
    }
}
