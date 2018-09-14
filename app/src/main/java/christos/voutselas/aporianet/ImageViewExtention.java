package christos.voutselas.aporianet;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageViewExtention extends AppCompatActivity
{
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private ImageView photoImageView;
    private  String photoUri = "";
    private Button closeBtn;
    private Button downloadBtn;
    private String finisgh = "No";
    private String name = "";
    private String subject = "";
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.image_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBarImageView);
        mProgressBar.setVisibility(ProgressBar.VISIBLE);
        photoImageView = (ImageView) findViewById(R.id.imageViewSelected);
        photoUri = getIntent().getStringExtra("imageUri");
        name = getIntent().getStringExtra("name");
        subject = getIntent().getStringExtra("subject");
        closeBtn = (Button) findViewById(R.id.btnClose);
        downloadBtn = (Button) findViewById(R.id.btnDownload);


        Glide.with(photoImageView.getContext())
                .load(photoUri)
                .into(photoImageView);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        closeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        downloadBtn.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                verifyStoragePermissions(ImageViewExtention.this);

                new DownloadFileFromURL(new DownloadFileFromURL.AsynResponse()
                {
                    @Override
                    public void processFinish(Boolean output)
                    {
                        Toast.makeText(ImageViewExtention.this, "Download finished!!", Toast.LENGTH_SHORT).show();
                    }
                }).execute(photoUri,"/Download/" + subject + "-" + name + ".JPEG");

                Toast.makeText(ImageViewExtention.this, "Download start...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static void verifyStoragePermissions(Activity activity)
    {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    private static class DownloadFileFromURL extends AsyncTask<String, String, String>
    {

        public interface AsynResponse
        {
            void processFinish(Boolean output);
        }

        AsynResponse asynResponse = null;

        public DownloadFileFromURL(AsynResponse asynResponse)
        {
            this.asynResponse = asynResponse;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String f_url)
        {
           // super.onPostExecute(f_url);
            asynResponse.processFinish(true);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url)
        {
            int count;

            try
            {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getExternalStorageDirectory().toString()
                        + f_url[1]);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
//
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            }
            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
    }
}
