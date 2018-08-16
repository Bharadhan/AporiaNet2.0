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

public class ThirdYearForumView extends AppCompatActivity
{
    Integer lessoonNamePotition = 0;
    Integer courseDirectionPotition = 0;
    Integer yearClassPotition = 0;
    Button newQuestion;
    String strUserName = "";
    String lessonName = "";
    String lessonDirection = "";
    String yearOfClass = "";

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

    private void updateView()
    {
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.string.thirdYearThetikonAndYgeias, R.string.ekuesi, R.drawable.ekthesi, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearThetikonAndYgeias, R.string.fysiki, R.drawable.fusiki, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearThetikonAndYgeias, R.string.xhmeia, R.drawable.xhmeia, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearThetikon, R.string.mathimatika, R.drawable.math_prosavatolismou, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearYgeias, R.string.biologia, R.drawable.biologia, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.ekuesi, R.drawable.ekthesi_oikonomias, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.mathimatika, R.drawable.mathimatika_oikonomias, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.aoth, R.drawable.aoth, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.ae, R.drawable.ae, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.ekuesi, R.drawable.ekthesi_anthropistikon, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.arxaia_gnosto, R.drawable.arxaia_gnosto, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.arxaia_agnosto, R.drawable.arxaia_agnosto, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.istoria, R.drawable.istoria, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.latinika, R.drawable.latinika, R.string.category_third_year));

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
}
