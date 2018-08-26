package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailedView extends AppCompatActivity
{
    private String selectetUserName = "";
    private String selectedSubject = "";
    private String selectedMainText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.detailed_message);
        update();


    }

    private void update()
    {
        setContentView(R.layout.detailed_message_view);
        selectetUserName = getIntent().getStringExtra("selectedUserName");
        selectedSubject = getIntent().getStringExtra("selectedSubject");
        selectedMainText = getIntent().getStringExtra("selectedMainText");

        final TextView userSelected = (TextView) findViewById(R.id.nameTextView_detailed_message_view);
        final TextView subjectSelected = (TextView) findViewById(R.id.subjectMessageTextView_detailed_message_view);
        final TextView mainTextSelected = (TextView) findViewById(R.id.main_question_detailed_message_view);

        userSelected.setText(selectetUserName);
        subjectSelected.setText(selectedSubject);
        mainTextSelected.setText(selectedMainText);
    }
}
