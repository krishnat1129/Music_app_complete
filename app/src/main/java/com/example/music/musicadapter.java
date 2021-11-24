package com.example.music;


import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class musicadapter extends RecyclerView.Adapter<musicadapter.ViewHolder> {



    private String[] localDataSet;
    private  TextView textview2;
    private  TextView textview;
    private ArrayList<musicfile> mfiles;
    private Context mcontext;

    static Uri uri;




    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private TextView textview2;
        private TextView textView3;
        private TextView songname;
         ImageView albumart;
         private ImageView verticalmore ;







        public ViewHolder(View view) {
            super(view);

            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
            textview2 = view.findViewById(R.id.textView2);
            albumart = view.findViewById(R.id.imageView);
            verticalmore = view.findViewById(R.id.verticalmore);



            //textView3 = view.findViewById(R.id.remaining);
            //songname =view.findViewById(R.id.songname);





        }

        public TextView getTextView() {
            return textView;
        }
    }
    private byte[] getalbumart(String uri){
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[]art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * containing the data to populate views to be used
     * by RecyclerView.
     */
    public musicadapter(Context mcontext,ArrayList<musicfile> mfiles) {

        this.mfiles = mfiles;
        this.mcontext = mcontext;
        //localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(mcontext)
                .inflate(R.layout.text_row_item, viewGroup, false);
        textview = view.findViewById(R.id.textView);
        //metadata(uri);
        //ImageView albumart = view.findViewById(R.id.imageView);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.textView.setText(mfiles.get(position).getTitle());
        viewHolder.textview2.setText(mfiles.get(position).getArtist().replace("<unknown>","Unknown Artist"));
        viewHolder.verticalmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        byte[] image = getalbumart(mfiles.get(position).getPath());
        if(image!=null){
            Glide.with(mcontext).asBitmap().load(image).into(viewHolder.albumart);

        }
        else{
            //Glide.with(mcontext).asBitmap().load(R.drawable.tone).into(viewHolder.albumart);
        }


        //Glide.with(this).asBitmap().load(position).into(viewHolder.albumart)
        //viewHolder.textView3.setText(mfiles.get(position).getDuration());
        //viewHolder.songname.setText(mfiles.get(position).getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,playeractivity.class);
                intent.putExtra("position",position);
                mcontext.startActivity(intent);

            }
        });

        //viewHolder.textView3.setText(mfiles.get(position).getAlbum());


       // viewHolder.getTextView().setText(localDataSet[position]);



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return mfiles.size();
        //return localDataSet.length;
    }

//    private void metadata(Uri uri){
//
//        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
//        retriever.setDataSource(uri.toString());
//        //int durationtotal = Integer.parseInt(songlist.get(position).getDuration());
//        //remaining.setText(formattedTime(durationtotal));
//        byte[]art = retriever.getEmbeddedPicture();
//        if(art!=null){
//            Glide.with(mcontext).asBitmap().load(art).into(albumart);
//
//        }
//        else{
//            Glide.with(mcontext).asBitmap().load(R.drawable.tone).into(albumart);
//        }
//    }

}

