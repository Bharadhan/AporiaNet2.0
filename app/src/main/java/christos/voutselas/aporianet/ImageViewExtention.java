package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageViewExtention extends AppCompatActivity
{
    private ImageView photoImageView;
    private  String photoUri = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.image_view);
        photoImageView = (ImageView) findViewById(R.id.imageViewSelected);
        photoUri = getIntent().getStringExtra("imageUri");

        Glide.with(photoImageView.getContext())
                .load(photoUri)
                .into(photoImageView);






    }
}
