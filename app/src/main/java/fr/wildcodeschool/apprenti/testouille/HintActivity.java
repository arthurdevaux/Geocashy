package fr.wildcodeschool.apprenti.testouille;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;

public class HintActivity extends AppCompatActivity {

    private ImageView imageView;
    private String url;
    int plop = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hint);

        this.imageView = (ImageView) findViewById(R.id.hint_picture);
        this.url = getIntent().getStringExtra(Constants.IMG_URL);
        AsyncCallWS task = new AsyncCallWS();
        task.execute();

        // musique dents de la mer
        Intent svc=new Intent(this, Music2.class);
        startService(svc);

    }

    private class AsyncCallWS extends AsyncTask<Void, Void, Void> {
        private String TAG = "WCS - AsyncTask";
        @Override
        protected Void doInBackground(Void... params) {
            Log.i(TAG, "doInBackground");
            getImageFromUrl();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.i(TAG, "onPostExecute");
        }

        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            Log.i(TAG, "onProgressUpdate");
        }

    }

    private void getImageFromUrl() {
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                URL newurl = new URL(this.url);
                final Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(mIcon_val);
                    }
                });
            } else {
                Toast.makeText(this, "no connection", Toast.LENGTH_SHORT).show();
            }

        }
        catch (MalformedURLException e){
            Log.d("MalformedURLException", e.toString());

            Intent ficheIntent= new Intent();
            ficheIntent = getIntent();
            HashMap mouloud = (HashMap)ficheIntent.getSerializableExtra("hashmap");
            Intent georges = new Intent(this,MapsActivity.class);
            georges.putExtra("hashmap",mouloud);
            startActivity(georges);
            this.finish();
        }
        catch (IOException e){
            Log.d("IOException", e.toString());
            Intent ficheIntent = new Intent(this,MapsActivity.class);
            startActivity(ficheIntent);
            this.finish();
        }
    }
    protected void onResume(){
        super.onResume();
        startService(new Intent(this,Music2.class));

    }
    protected void onDestroy(){
        super.onDestroy();
        stopService(new Intent(this,Music2.class));
    }
}
