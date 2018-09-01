package christos.voutselas.aporianet;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class AboutUs extends AppCompatActivity
{

    private ImageView facebook;
    private ImageView web;
    private ImageView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.about_us);

        facebook = (ImageView) findViewById(R.id.facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFacebook();
            }
        });

        web = (ImageView) findViewById(R.id.web);
        web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWeb();
            }
        });

        mail = (ImageView) findViewById(R.id.mail);
        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMail();
            }
        });
    }

    private void startFacebook()
    {
        Intent facebookAppIntent;
        try
        {
            facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/317270851712586"));
            startActivity(facebookAppIntent);
        }
        catch (ActivityNotFoundException e)
        {
            facebookAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/Ενιαίο-Φροντιστηριακό-Κέντρον-317270851712586"));
            startActivity(facebookAppIntent);
        }
    }

    private void startWeb()
    {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        intent.setData(Uri.parse("http://www.kentron.edu.gr"));
        startActivity(intent);
    }

    private void startMail()
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@kentron.edu.gr"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        startActivity(Intent.createChooser(intent, ""));
    }
}

