package christos.voutselas.aporianet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;

public class FirstYearForumView extends AppCompatActivity
{
    private Integer lessoonNamePotition = 0;
    private Integer courseDirectionPotition = 0;
    private Integer yearClassPotition = 0;
    private Button newQuestion;
    private String strUserName = "";
    private String lessonName = "";
    private String lessonDirection = "";
    private String yearOfClass = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.list_forum);

        updateView();

        newQuestion = (Button) findViewById(R.id.newQuestionButton);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userName();

                Intent intent = new Intent(getApplicationContext(), NewQuestionActivity.class);
                intent.putExtra("finalResults", strUserName);
                intent.putExtra("lessonName", lessonName);
                intent.putExtra("lessonDirection", lessonDirection);
                intent.putExtra("yearOfClass", yearOfClass);
                startActivity(intent);
            }
        });
    }

    private final String userName()
    {
        final TextView lessonNameTextView = (TextView) findViewById(R.id.lesson);
        final TextView lessonDirectionTextView = (TextView) findViewById(R.id.derection);
        final TextView yearClassTextView = (TextView) findViewById(R.id.yearClass);

        lessonName = lessonNameTextView.getText().toString();
        lessonDirection = lessonDirectionTextView.getText().toString();
        yearOfClass = yearClassTextView.getText().toString();

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        strUserName = SP.getString("username", "NA");
        System.out.print(strUserName);
        return strUserName;
    }

    private void updateView()
    {
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.string.firstYearDirection, R.string.algevra, R.drawable.algevra, R.string.category_first_year));
        words.add(new Word(R.string.firstYearDirection, R.string.arxaia, R.drawable.arxaia, R.string.category_first_year));
        words.add(new Word(R.string.firstYearDirection, R.string.ekuesi, R.drawable.ekthesi, R.string.category_first_year));
        words.add(new Word(R.string.firstYearDirection, R.string.geometria, R.drawable.geometry, R.string.category_first_year));
        words.add(new Word(R.string.firstYearDirection, R.string.fysiki, R.drawable.fusiki, R.string.category_first_year));
        words.add(new Word(R.string.firstYearDirection, R.string.xhmeia, R.drawable.xhmeia, R.string.category_first_year));

        lessoonNamePotition = Integer.parseInt(getIntent().getStringExtra("lessonName"));
        courseDirectionPotition = Integer.parseInt(getIntent().getStringExtra("courseDirection"));
        yearClassPotition = Integer.parseInt(getIntent().getStringExtra("yearClass"));

        final TextView lessonNameTextView = (TextView) findViewById(R.id.lesson);
        final TextView lessonDirectionTextView = (TextView) findViewById(R.id.derection);
        final TextView yearClassTextView = (TextView) findViewById(R.id.yearClass);

        lessonNameTextView.setText(lessoonNamePotition);
        lessonDirectionTextView.setText(courseDirectionPotition);
        yearClassTextView.setText(yearClassPotition);
    }
}