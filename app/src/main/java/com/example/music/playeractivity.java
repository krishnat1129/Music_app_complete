 package com.example.music;

 import android.content.Context;
 import android.graphics.Bitmap;
 import android.graphics.BitmapFactory;
 import android.media.MediaMetadataRetriever;
 import android.media.MediaPlayer;
 import android.net.Uri;
 import android.os.Bundle;
 import android.os.Handler;
 import android.view.View;
 import android.view.animation.Animation;
 import android.view.animation.AnimationUtils;
 import android.widget.ImageView;
 import android.widget.SeekBar;
 import android.widget.TextView;
 import android.widget.Toast;

 import androidx.appcompat.app.AppCompatActivity;

 import com.bumptech.glide.Glide;

 import java.util.ArrayList;
 import java.util.Random;

 import static com.example.music.MainActivity.Musicfiles;
 import static com.example.music.MainActivity.repeatones;
 import static com.example.music.MainActivity.shufflech;

 public class playeractivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener{
    TextView songname,remaining,current,artistname;
    ImageView Albumimage,next,previous,shuffle,repeat,repeatone,playbutton;
    SeekBar seekBar;

    int position  = -1;
    public static ArrayList<musicfile> songlist = new ArrayList<>();
    static Uri uri ;
    static MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private Thread playthread,nextthread,previousthread;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playeractivity);
        initview();
        getIntentMethod();
        mediaPlayer.setOnCompletionListener(this);
        repeatmethod();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        playeractivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int mcurrentposition = mediaPlayer.getCurrentPosition()/1000;
                    int remainingduration = mediaPlayer.getDuration()/1000;
                    seekBar.setProgress(mcurrentposition);
                    current.setText(formattedTime(mcurrentposition));
                    remaining.setText(formattedTime(remainingduration-mcurrentposition));
                    songname.setText(songlist.get(position).getTitle());
                    artistname.setText(songlist.get(position).getArtist().replace("<unknown>",""));




                }
                handler.postDelayed(this,0);
            }
        });

        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shufflech&&repeatones){
                    shufflech=false;
                    repeatones = true;
                    shuffle.setImageResource(R.drawable.shuffle);
                    Toast.makeText(playeractivity.this, "Playback is shuffled", Toast.LENGTH_SHORT).show();
                }else if(!shufflech&&repeatones){
                    shufflech = true;
                    repeatones = false;
                    shuffle.setImageResource(R.drawable.ic_baseline_repeat_one_24);
                    Toast.makeText(playeractivity.this,"Current song is looped",Toast.LENGTH_SHORT).show();
                }
                else if(!repeatones&&shufflech){
                    shufflech = true;
                    repeatones = true;
                    shuffle.setImageResource(R.drawable.repeat);
                    Toast.makeText(playeractivity.this,"Current playlist is looped",Toast.LENGTH_SHORT).show();

                }
            }
        });
//        repeat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (repeatch=true){
//                    repeatch=false;
//                    repeat.setImageResource(R.drawable.repeat);
//                }else{
//                    repeatch = true;
//                    shuffle.setImageResource(R.drawable.repeat);
//                }
//
//            }
//        });
    }
    public void repeatmethod(){
        shuffle.setImageResource(R.drawable.repeat);
        shufflech = true;
        repeatones=true;
    }

    @Override
    protected void onResume() {
        playthreadbtn();
        nextthreadbtn();
        previousthreadbtn();
        super.onResume();
    }




    private void playthreadbtn() {
        playthread = new Thread(){
            @Override
            public void run() {
                super.run();
                playbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playbuttonClicked();
                    }
                });
            }
        };
        playthread.start();
    }

    private void playbuttonClicked() {
        if(mediaPlayer.isPlaying()){
            playbutton.setImageResource(R.drawable.play);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            playeractivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mcurrentposition = mediaPlayer.getCurrentPosition()/1000;
                        int remainingduration = mediaPlayer.getDuration()/1000;
                        seekBar.setProgress(mcurrentposition);
                        //current.setText(formattedTime(mcurrentposition));
                       // remaining.setText(formattedTime(remainingduration-mcurrentposition));
                        //songname.setText(songlist.get(position).getTitle());
                        //artistname.setText(songlist.get(position).getArtist().replace("<unknown>",""));
                    }
                    handler.postDelayed(this,0);
                }
            });


        }
        else{
            playbutton.setImageResource(R.drawable.pause);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);

        }
    }

    private void nextthreadbtn() {
        nextthread = new Thread(){
            @Override
            public void run() {
                super.run();
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextbuttonClicked();
                        
                    }
                });
            }
        };
        nextthread.start();
    }


    private void nextbuttonClicked(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shufflech&&repeatones){
            position = (position+1)%songlist.size();}
            else if(!shufflech&&repeatones) {
                position = getRandom(songlist.size()-1);

            }else if(shufflech&&!repeatones){
                position = position;
            }

            uri = Uri.parse(songlist.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);;
            songname.setText(songlist.get(position).getTitle());
            artistname.setText(songlist.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            playeractivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mcurrentposition = mediaPlayer.getCurrentPosition()/1000;
                        int remainingduration = mediaPlayer.getDuration()/1000;
                        seekBar.setProgress(mcurrentposition);
                        //current.setText(formattedTime(mcurrentposition));
                        // remaining.setText(formattedTime(remainingduration-mcurrentposition));
                        //songname.setText(songlist.get(position).getTitle());
                        //artistname.setText(songlist.get(position).getArtist().replace("<unknown>",""));
                    }
                    handler.postDelayed(this,0);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            if(shufflech){
                position = (position+1)%songlist.size();}
            else {
                position = getRandom(songlist.size()-1);

            }
            //position = (position+1)%songlist.size();
            uri = Uri.parse(songlist.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);;
            songname.setText(songlist.get(position).getTitle());
            artistname.setText(songlist.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            playeractivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mcurrentposition = mediaPlayer.getCurrentPosition()/1000;
                        int remainingduration = mediaPlayer.getDuration()/1000;
                        seekBar.setProgress(mcurrentposition);
                        //current.setText(formattedTime(mcurrentposition));
                        // remaining.setText(formattedTime(remainingduration-mcurrentposition));
                        //songname.setText(songlist.get(position).getTitle());
                        //artistname.setText(songlist.get(position).getArtist().replace("<unknown>",""));
                    }
                    handler.postDelayed(this,0);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.pause);
            mediaPlayer.start();
                    }

    }

     private int getRandom(int i) {
         Random random = new Random();
         return random.nextInt(i+1);
     }

     private void previousthreadbtn() {
        previousthread = new Thread(){
            @Override
            public void run() {
                super.run();
                previous.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousbuttonClicked();
                    }
                });
            }
        };
        previousthread.start();
    }

    private void previousbuttonClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position-1)<0 ? (songlist.size()-1) : (position-1));
            uri = Uri.parse(songlist.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);;
            songname.setText(songlist.get(position).getTitle());
            artistname.setText(songlist.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            playeractivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mcurrentposition = mediaPlayer.getCurrentPosition()/1000;
                        int remainingduration = mediaPlayer.getDuration()/1000;
                        seekBar.setProgress(mcurrentposition);
                        //current.setText(formattedTime(mcurrentposition));
                        // remaining.setText(formattedTime(remainingduration-mcurrentposition));
                        //songname.setText(songlist.get(position).getTitle());
                        //artistname.setText(songlist.get(position).getArtist().replace("<unknown>",""));
                    }
                    handler.postDelayed(this,0);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
        else{
            mediaPlayer.stop();
            mediaPlayer.release();
            position = ((position-1)<0 ? (songlist.size()-1) : (position-1));
            uri = Uri.parse(songlist.get(position).getPath());
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            metadata(uri);;
            songname.setText(songlist.get(position).getTitle());
            artistname.setText(songlist.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            playeractivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(mediaPlayer!=null){
                        int mcurrentposition = mediaPlayer.getCurrentPosition()/1000;
                        int remainingduration = mediaPlayer.getDuration()/1000;
                        seekBar.setProgress(mcurrentposition);
                        //current.setText(formattedTime(mcurrentposition));
                        // remaining.setText(formattedTime(remainingduration-mcurrentposition));
                        //songname.setText(songlist.get(position).getTitle());
                        //artistname.setText(songlist.get(position).getArtist().replace("<unknown>",""));
                    }
                    handler.postDelayed(this,0);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.pause);
            mediaPlayer.start();
        }
    }

    private String formattedTime(int mcurrentposition) {
        String Totalout = "";
        String totalNew = "";
        String seconds = String.valueOf(mcurrentposition % 60);
        String minutes = String.valueOf(mcurrentposition / 60);
        Totalout = minutes+":"+seconds;
        totalNew = minutes+":"+"0"+seconds;
        if(seconds.length()==1){
            return totalNew;
        }else{
            return Totalout;
        }
    }

    private void getIntentMethod() {
        position = getIntent().getIntExtra("position",-1);
        songlist = Musicfiles;
        if(songlist!=null){
            playbutton.setImageResource(R.drawable.pause);
            uri = Uri.parse(songlist.get(position).getPath());
            //System.out.print(songlist);

        }
        if(mediaPlayer !=null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();

        }else{
            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metadata(uri);

    }






    private void initview() {
        artistname = findViewById(R.id.artistname);
        songname = findViewById(R.id.songname);
        remaining = findViewById(R.id.remaining);
        current = findViewById(R.id.current);
        Albumimage = findViewById(R.id.Albumimage);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        shuffle = findViewById(R.id.shuffle);
        repeat = findViewById(R.id.repeat);
        repeatone = findViewById(R.id.repeatone);
        playbutton = findViewById(R.id.playbutton);
        seekBar = findViewById(R.id.seekBar);

    }
    private void metadata(Uri uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        //int durationtotal = Integer.parseInt(songlist.get(position).getDuration());
        //remaining.setText(formattedTime(durationtotal));
        byte[]art = retriever.getEmbeddedPicture();
        //byte[]art2 = {(byte) R.drawable.tone};
        Bitmap bitmap;
        if(art!=null){
           //Glide.with(this).asBitmap().load(art).into(Albumimage);
            bitmap = BitmapFactory.decodeByteArray(art,0,art.length);
            imageanimation(this,bitmap,Albumimage);

        }
        else{
            //int imagepath = R.drawable.tone;
           // bitmap = BitmapFactory.decodeByteArray(art2,0,art2.length);
            //imageanimation(this,bitmap,Albumimage);
            Glide.with(this).load(R.drawable.tone).into(Albumimage);


        }
    }
    public void imageanimation(Context context, Bitmap bitmap,ImageView imageView){
        Animation animout = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation animin = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        animout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Glide.with(context).load(bitmap).into(imageView);
                animin.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageView.startAnimation(animin);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageView.startAnimation(animout);

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextbuttonClicked();
//        if(mediaPlayer!= null){
//            mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
//            mediaPlayer.start();
//            mediaPlayer.setOnCompletionListener(this);
       // }
    }
}