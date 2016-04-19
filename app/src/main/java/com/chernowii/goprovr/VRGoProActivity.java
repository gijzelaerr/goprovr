package com.chernowii.goprovr;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.VideoView;


public class VRGoProActivity extends Activity {
    private String urlStream;
    private VideoView streamLeft;
    private VideoView streamRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vrgo_pro);
        refreshStream();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       refreshStream();
        return super.onTouchEvent(event);
    }


    public void refreshStream(){

        streamLeft = (VideoView)this.findViewById(R.id.streamLeft);
        streamRight = (VideoView)this.findViewById(R.id.streamRight);

        urlStream = "http://10.5.5.9:8080/live/amba.m3u8";
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                streamLeft.setVideoURI(Uri.parse(urlStream));
                streamRight.setVideoURI(Uri.parse(urlStream));
                streamRight.start();
                streamLeft.start();
            }

        });
    }

}
