package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ThirdYearForumView extends AppCompatActivity
{
    private Integer lessoonNamePotition = 0;
    private Integer courseDirectionPotition = 0;
    private Integer yearClassPotition = 0;
    private Button newQuestion;
    private String strUserName = "";
    private String lessonName = "";
    private String lessonDirection = "";
    private String yearOfClass = "";
    private String back = "No";
    private String mUsername;
    private ListView mMessageListView;
    private ProgressBar mProgressBar;
    private List<FriendlyMessage> friendlyMessages;
    private MessageAdapter mMessageAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.list_forum);

        back = getIntent().getStringExtra("back");

        updateView();

        newQuestion = (Button) findViewById(R.id.newQuestionButton);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

        final TextView lessonNameTextView = (TextView) findViewById(R.id.lesson);
        final TextView lessonDirectionTextView = (TextView) findViewById(R.id.derection);
        final TextView yearClassTextView = (TextView) findViewById(R.id.yearClass);

        if (back.equals("No"))
        {
            lessoonNamePotition = Integer.parseInt(getIntent().getStringExtra("lessonName"));
            courseDirectionPotition = Integer.parseInt(getIntent().getStringExtra("courseDirection"));
            yearClassPotition = Integer.parseInt(getIntent().getStringExtra("yearClass"));

            lessonNameTextView.setText(lessoonNamePotition);
            lessonDirectionTextView.setText(courseDirectionPotition);
            yearClassTextView.setText(yearClassPotition);

            lessonName = lessonNameTextView.getText().toString();
            lessonDirection = lessonDirectionTextView.getText().toString();
            yearOfClass = yearClassTextView.getText().toString();
        }
        else
        {
            lessonName = getIntent().getStringExtra("lessonName");
            lessonDirection = getIntent().getStringExtra("courseDirection");
            yearOfClass = getIntent().getStringExtra("yearClass");

            lessonNameTextView.setText(lessonName);
            lessonDirectionTextView.setText(lessonDirection);
            yearClassTextView.setText(yearOfClass);

            readData();
        }
    }

    private void readData()
    {
        mUsername = MainActivity.useName;

        mMessageListView = findViewById(R.id.listViewAs);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Initialize message ListView and its adapter
        friendlyMessages = new ArrayList<>();

        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClass).child(lessonDirection).child(lessonName);

        if (mChildEventListener == null)
        {
            mChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String key)
                {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    System.out.println("The updated post title is: " + friendlyMessage.getName());
                    key = dataSnapshot.getKey();
                    mMessageAdapter.add(friendlyMessage);
                    friendlyMessage.setKey(key);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }
}
