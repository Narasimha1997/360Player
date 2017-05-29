package com.example.narasimha.my360;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

public class Video_Player extends AppCompatActivity {
    VrVideoView myVideo;
    final int code=1;
    TextView divContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video__player);
        myVideo=(VrVideoView)findViewById(R.id.video_view);
        divContainer=(TextView)findViewById(R.id.DIV_PATH);
    }
    public
       void video_picker(View v){
        Intent iVideo=new Intent(Intent.ACTION_PICK,
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(iVideo, code);
    }

    private String getPath(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if(cursor!=null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        else return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if(resultCode==RESULT_OK){
                if(requestCode==code){
                    String URI_PATH=getPath(data.getData());
                    divContainer.setText(URI_PATH);
                    Uri video_file=Uri.parse("file://"+URI_PATH);
                    try {
                        VrVideoView.Options op=new VrVideoView.Options();
                        op.inputFormat=VrVideoView.Options.FORMAT_DEFAULT;
                        op.inputType=VrVideoView.Options.TYPE_MONO;
                        myVideo.loadVideo(video_file,op);
                        myVideo.setEventListener(listner);
                    } catch (IOException e) {
                        Toast.makeText(this, "Could not load video", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }catch (Exception e){
            Toast.makeText(this, "Unable to load Video", Toast.LENGTH_SHORT).show();
        }
    }
    boolean pause_flag=false;
    long pos=0;
    VrVideoEventListener listner=new VrVideoEventListener(){
        @Override
        public void onLoadSuccess() {
            super.onLoadSuccess();
            Toast.makeText(Video_Player.this, "Loaded sussessfully", Toast.LENGTH_SHORT).show();
            myVideo.playVideo();
        }

        @Override
        public void onClick() {
            super.onClick();
            if(!pause_flag) {
                pos=myVideo.getCurrentPosition();
                pause_flag=true;
                myVideo.pauseVideo();
            }
            else {
                myVideo.seekTo(pos);
                myVideo.playVideo();
                pause_flag=false;
            }
        }

    };

}
