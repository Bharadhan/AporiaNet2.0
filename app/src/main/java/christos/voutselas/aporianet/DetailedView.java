package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

public class DetailedView extends AppCompatActivity
{
    private String selectetUserName = "";
    private String selectedSubject = "";
    private String selectedMainText = "";
    private String yearOfClassNewQuestion = "";
    private String lessonDirectionNewQuestion = "";
    private String lessonNameNewQuestion = "";
    private String mUsername;
    private String userText = "";
    private String postedName = "";
    private DetailedMessageAdapter mDMessageAdapter;
    private ListView mMessageListView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReference;
    private String key = "";
    private ProgressBar mProgressBar;
    private ChildEventListener mDChildEventListener;
    private EditText mMessageEditText;
    private Button mSendButton;
    private EditText userInput;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.detailed_message);
        userInput = (EditText) findViewById(R.id.messageEditText);

        mMessageListView = findViewById(R.id.listViewAs_detailed);
        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");
        key = getIntent().getStringExtra("selectedKey");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        selectetUserName = getIntent().getStringExtra("selectedUserName");
        selectedSubject = getIntent().getStringExtra("selectedSubject");
        selectedMainText = getIntent().getStringExtra("selectedMainText");
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mUsername = MainActivity.useName;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialize message ListView and its adapter
        List<DetailedFriendlyMessage> dFriendlyMessages = new ArrayList<>();
        mDMessageAdapter = new DetailedMessageAdapter(this, R.layout.question_message_view, dFriendlyMessages);
        mMessageListView.setAdapter(mDMessageAdapter);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

        checkChildDetails();

        readData();

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mMessagesDatabaseReference.removeEventListener(mDChildEventListener);
                userText = userInput.getText().toString();

                changeView();
            }
        });
    }

    private void checkChildDetails()
    {
        mMessagesDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {

                    findQuestion();
                } else {
                   // DetailedFriendlyMessage detailedFriendlyMessage = dataSnapshot.getValue(DetailedFriendlyMessage.class);
                   // postedName = String.valueOf(detailedFriendlyMessage.getName());

                    if (dataSnapshot.getChildrenCount() < 2 && !(selectetUserName.equals(mUsername))) {
                        mSendButton.setEnabled(true);

                        mMessageEditText.setFocusable(true);
                    } else {
                        mSendButton.setEnabled(false);

                        mMessageEditText.setFocusable(false);
                    }
                }
            }



            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void findQuestion()
    {
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

        DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(selectedMainText, selectetUserName, selectedSubject, key, "", "", null);
        mMessagesDatabaseReference.push().setValue(dFriendlyMessage);
    }

    private void readData()
    {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        if (mDChildEventListener == null)
        {
            mDChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dDataSnapshot, String keyOne)
                {
                    DetailedFriendlyMessage detailedFriendlyMessage = dDataSnapshot.getValue(DetailedFriendlyMessage.class);
                    postedName = detailedFriendlyMessage.getName();
                    mDMessageAdapter.add(detailedFriendlyMessage);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mDChildEventListener);
        }
    }

    private void changeView()
    {
        Intent intent = new Intent(getApplicationContext(), UserDetailedView.class);
        intent.putExtra("lessonName", lessonNameNewQuestion);
        intent.putExtra("lessonDirection", lessonDirectionNewQuestion);
        intent.putExtra("yearOfClass", yearOfClassNewQuestion);
        intent.putExtra("key", key);
        intent.putExtra("selectedSubject", selectedSubject);
        intent.putExtra("selectedMainText", selectedMainText);
        intent.putExtra("userText", userText);
        startActivity(intent);
    }
}
