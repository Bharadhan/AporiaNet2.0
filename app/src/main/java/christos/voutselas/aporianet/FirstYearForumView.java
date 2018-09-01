package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
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

public class FirstYearForumView extends AppCompatActivity
{
    private Integer lessoonNamePotition = 0;
    private Integer courseDirectionPotition = 0;
    private Integer yearClassPotition = 0;
    private Button newQuestion;
    private String strUserName = "";
    private String lessonName;
    private String lessonDirection = "";
    private String yearOfClass = "";
    private String strSubject = "";
    private String mUsername;
    private ChildEventListener mChildEventListener;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference sizeMessagesDatabaseReference;
    private MessageAdapter mMessageAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private ListView mMessageListView;
    private ProgressBar mProgressBar;
    private List<FriendlyMessage> friendlyMessages;
    private String selectetUserNAme = "";
    private String selectedSubject = "";
    private String selectedMainText = "";
    private String key = "";
    private String selectedKey = "";
    private String vote = "";
    private String back = "No";
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.list_forum);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        back = getIntent().getStringExtra("back");

        updateView();

        readData();

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

        mMessageListView.setOnItemClickListener(new OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mMessagesDatabaseReference.removeEventListener(mChildEventListener);
                FriendlyMessage message = friendlyMessages.get(position);
                selectetUserNAme =  message.getName();
                selectedSubject = message.getSubject();
                selectedMainText = message.getText();
                selectedKey = message.getKey();
                vote = message.getVotes();
                Intent intent = new Intent(getApplicationContext(), DetailedView.class);
                intent.putExtra("lessonName", lessonName);
                intent.putExtra("lessonDirection", lessonDirection);
                intent.putExtra("yearOfClass", yearOfClass);
                intent.putExtra("selectedUserName", selectetUserNAme);
                intent.putExtra("selectedSubject", selectedSubject);
                intent.putExtra("selectedMainText", selectedMainText);
                intent.putExtra("selectedKey", selectedKey);
                intent.putExtra("vote", vote);
                startActivity(intent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
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