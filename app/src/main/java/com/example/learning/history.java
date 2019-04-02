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

import java.io.File;

public class history extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ListView txt = (ListView) findViewById(R.id.savedNotes1);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, MainActivity.addArray);
        txt.setAdapter(adapter);
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
