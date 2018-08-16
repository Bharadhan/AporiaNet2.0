package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class NewQuestionActivity extends AppCompatActivity
{

    String useName = "";
    String lessonNameNewQuestion = "";
    String lessonDirectionNewQuestion = "";
    String yearOfClassNewQuestion = "";
    Button cancelBtn;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);
        updateFields();

        cancelBtn = (Button) findViewById(R.id.rejectBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
