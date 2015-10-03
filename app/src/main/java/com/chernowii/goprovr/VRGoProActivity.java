package com.chernowii.goprovr;


import android.annotation.TargetApi;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;


public class VRGoProActivity extends Activity {
    private String urlStream;
    private VideoView streamLeft;
    private VideoView streamRight;
    private URL url;
    public static String getFileContents(final File file) throws IOException {
        final InputStream inputStream = new FileInputStream(file);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        final StringBuilder stringBuilder = new StringBuilder();

        boolean done = false;

        while (!done) {
            final String line = reader.readLine();
            done = (line == null);

            if (line != null) {
                stringBuilder.append(line);
            }
        }

        reader.close();
        inputStream.close();

        return stringBuilder.toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vrgo_pro);

        //START STREAMING SERVICE ON CAMERA H2/H3/H3+
 String yourFilePath = this.getFilesDir() + "/" + "camconfig.txt";

        File yourFile = new File( yourFilePath );
        try {
            String password = getFileContents(yourFile);

            new HttpAsyncTask().execute("http://10.5.5.9/camera/PV?t=" + password + "&p=%02");
        } catch (IOException e) {
            e.printStackTrace();
        }
//START TO STREAM TO TWO VIDEO SPACES

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


    @Override
    public boolean onTouchEvent(MotionEvent event) {
       refreshStream();
        return super.onTouchEvent(event);
    }


    public void refreshStream(){
        String yourFilePath = this.getFilesDir() + "/" + "camconfig.txt";

        File yourFile = new File( yourFilePath );
        try {
            String password = getFileContents(yourFile);

            new HttpAsyncTask().execute("http://10.5.5.9/camera/PV?t=" + password + "&p=%02");
        } catch (IOException e) {
            e.printStackTrace();
        }
//START TO STREAM TO TWO VIDEO SPACES

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
    public static String GET(String url){
        InputStream inputStream = null;
        String result = "";
        try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getBaseContext(), "Done!", Toast.LENGTH_SHORT).show();
            ;
        }
    }
}
