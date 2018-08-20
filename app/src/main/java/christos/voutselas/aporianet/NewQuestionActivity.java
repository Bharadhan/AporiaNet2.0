package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class NewQuestionActivity extends AppCompatActivity
{
    private ImageButton mPhotoPickerButton;
    private static final int RC_PHOTO_PICKER =  2;
    private String useName = "";
    private String lessonNameNewQuestion = "";
    private String lessonDirectionNewQuestion = "";
    private String yearOfClassNewQuestion = "";
    private Button cancelBtn;
    private Button sudmitBtn;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);
        updateFields();
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        cancelBtn = (Button) findViewById(R.id.rejectBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sudmitBtn = (Button) findViewById(R.id.submitBtn);
        sudmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ///////////////////////////////////////////////////////////////













            }
        });
    }

    private void updateFields() {
        useName = getIntent().getStringExtra("strUserName");
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");

        final TextView lessonNameTextViewNewQuestion = (TextView) findViewById(R.id.lessonNewQuestion);
        final TextView lessonDirectionTextViewNewQuestion = (TextView) findViewById(R.id.derectionNewQuestion);
        final TextView yearClassTextViewNewQuestion = (TextView) findViewById(R.id.yearClassNewQuestion);

        lessonNameTextViewNewQuestion.setText(lessonNameNewQuestion);
        lessonDirectionTextViewNewQuestion.setText(lessonDirectionNewQuestion);
        yearClassTextViewNewQuestion.setText(yearOfClassNewQuestion);
    }
}
