package com.example.music;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;



public class MainActivity extends AppCompatActivity {
   public static final int REQUEST_CODE = 1;
   static ArrayList<musicfile> Musicfiles;
   static boolean shufflech=false,repeatones=false;






    //String arr[] ={"krishna","and","me","4","5","5","5","5","5","5","5"};
    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.recyclerview1);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,REQUEST_CODE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        musicadapter ad = new musicadapter(this,Musicfiles);
        Collections.sort(Musicfiles, new Comparator<musicfile>() {
            @Override
            public int compare(musicfile o1, musicfile o2) {
                return o1.getTitle().compareTo(o2.getTitle());
            }
        });
        recyclerView.setAdapter(ad);






       // recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));

        //listView = findViewById(R.id.listview);
        //ArrayAdapter ad = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arr);
        //listView.setAdapter(ad);


    }



    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Musicfiles = getAllAudio(this);

           //Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
    public void onRequestPermissionsResult(int requestcode,String[]permission,int[] grantResults){
        super.onRequestPermissionsResult(requestcode,permission,grantResults);
        if(requestcode==REQUEST_CODE){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults.length>0){
                Musicfiles = getAllAudio(this);
                Toast.makeText(MainActivity.this,"Permission granted",Toast.LENGTH_SHORT).show();
            }
            else{
                finishAndRemoveTask();
            }
        }
    }
    public static ArrayList<musicfile> getAllAudio(Context context){
        ArrayList<musicfile> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String [] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATE_ADDED

        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor!= null){
            while (cursor.moveToNext()){
                String album  = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);
                String date = cursor.getString(5);

                musicfile Musicfiles = new musicfile(path,  album,  title,  artist,  duration,date);
                tempAudioList.add(Musicfiles);


            }
            cursor.close();
        }
        return tempAudioList;
    }




}
