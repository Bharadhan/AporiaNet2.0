package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
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

public class RoboticForumView extends AppCompatActivity
{
    private String lessonName = "";
    private String lessonDirection = "";
    private String yearOfClass = "";
    private Integer lessoonNamePotition = 0;
    private Integer courseDirectionPotition = 0;
    private Integer yearClassPotition = 0;
    private Button newQuestion;
    private String strUserName = "";
    private String back = "No";
    private String mUsername;
    private ListView mMessageListView;
    private ProgressBar mProgressBar;
    private List<FriendlyMessage> friendlyMessages;
    private MessageAdapter mMessageAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String selectetUserNAme = "";
    private String selectedSubject = "";
    private String selectedMainText = "";
    private String key = "";
    private String selectedKey = "";
    private String wrongAnwser = "";
    private ImageView backBtn;
    private Integer finalPos = 0;
    private String time = "";
    private String selectedUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mMessagesDatabaseReference.removeEventListener(mChildEventListener);
                finalPos = MessageAdapter.pos;
                FriendlyMessage message = friendlyMessages.get(finalPos - position -1);
                selectetUserNAme =  message.getName();
                selectedSubject = message.getSubject();
                selectedMainText = message.getText();
                selectedKey = message.getKey();
                wrongAnwser = message.getVotes();
                time = message.getDate();
                selectedUrl = message.getPhotoUrl();
                Intent intent = new Intent(getApplicationContext(), DetailedView.class);
                intent.putExtra("lessonName", lessonName);
                intent.putExtra("lessonDirection", lessonDirection);
                intent.putExtra("yearOfClass", yearOfClass);
                intent.putExtra("selectedUserName", selectetUserNAme);
                intent.putExtra("selectedSubject", selectedSubject);
                intent.putExtra("selectedMainText", selectedMainText);
                intent.putExtra("selectedKey", selectedKey);
                intent.putExtra("photoUrl", selectedUrl);
                intent.putExtra("wrongAnwser", wrongAnwser);
                intent.putExtra("time", time);
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
        words.add(new Word(R.string.firstYearDirection, R.string.roboBaby, R.drawable.robotbaby, R.string.category_robotic));
        words.add(new Word(R.string.firstYearDirection, R.string.roboKid, R.drawable.robokid, R.string.category_robotic));

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
                    String wrongMesage = friendlyMessage.getVotes();

                    if (wrongMesage.equals("No"))
                    {
                        key = dataSnapshot.getKey();
                        mMessageAdapter.add(friendlyMessage);
                        friendlyMessage.setKey(key);
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    }
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
