package com.example.user.mymusicplayer;

import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    ListView lvMusic;
    Button btnStop,btnPause;
    ImageButton ibtnSkipBack,ibtnSkipForward,ibtnPrevious,ibtnNext;
    MediaPlayer mp;
    String name[],path[];
    int cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvMusic = findViewById(R.id.lvMusic);
        btnStop = findViewById(R.id.btnStop);
        btnPause = findViewById(R.id.btnPause);
        ibtnSkipBack = findViewById(R.id.ibtnSkipBack);
        ibtnSkipForward = findViewById(R.id.ibtnSkipForward);
        ibtnPrevious = findViewById(R.id.ibtnPrevious);
        ibtnNext = findViewById(R.id.ibtnNext);
        mp = new MediaPlayer();

        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);


        int i = 0;
        if (cursor.getCount() > 0) {
            name = new String[cursor.getCount()];
            path = new String[cursor.getCount()];

            cursor.moveToFirst();

            do {
                int i_name = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                int i_path = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                name[i] = cursor.getString(i_name);
                path[i] = cursor.getString(i_path);
                i++;
            } while (cursor.moveToNext()) ;
    }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,name);

        lvMusic.setAdapter(arrayAdapter);

        lvMusic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    cs = i;
                    String p = path[i];
                    mp.reset();
                    mp.setDataSource(p);
                    mp.prepare();
                    mp.start();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying())
                {
                    mp.stop();

                }
            }
        });


        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying())
                {
                    mp.pause();
                    btnPause.setText("Resume");
                }
                else
                {
                    mp.start();
                    btnPause.setText("Pause");
                }
            }
        });

        ibtnSkipBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition() - 3000);
            }
        });

        ibtnSkipForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mp.seekTo(mp.getCurrentPosition()+3000);
            }
        });


        ibtnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ns =cs -1;
                if(ns<0)
                    Toast.makeText(MainActivity.this, "first song", Toast.LENGTH_SHORT).show();
                else
                {
                    try {
                        cs = ns;
                        String p = path[ns];
                        mp.reset();
                        mp.setDataSource(p);
                        mp.prepare();
                        mp.start();
                    }catch (IOException e )
                    {
                        e.printStackTrace();
                    }
                }
            }
        });


        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int ns =cs + 1;
                if(ns<0)
                    Toast.makeText(MainActivity.this, "last song", Toast.LENGTH_SHORT).show();
                else
                {
                    try {
                        cs = ns;
                        String p = path[ns];
                        mp.reset();
                        mp.setDataSource(p);
                        mp.prepare();
                        mp.start();
                    }catch (IOException e )
                    {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
