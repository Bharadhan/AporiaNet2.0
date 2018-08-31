package christos.voutselas.aporianet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private String mUsername;
    private String userText = "";
    private String postedName = "";
    private ImageView voteBtn;

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
        voteBtn = (ImageView) findViewById(R.id.fab);
        voteBtn.setVisibility(View.INVISIBLE);

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

        voteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mMessagesDatabaseReference.removeEventListener(mDChildEventListener);

                vote();

            }
        });
    }

    private void checkChildDetails()
    {
        mMessagesDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if (!dataSnapshot.exists())
                {
                    findQuestion();

                }
                else {

                    if (dataSnapshot.getChildrenCount() < 2 && !(selectetUserName.equals(mUsername)))
                    {
                        mSendButton.setEnabled(true);

                        mMessageEditText.setFocusable(true);

                        voteBtn.setVisibility(View.INVISIBLE);

                    }
                    else if (dataSnapshot.getChildrenCount() > 1 && (selectetUserName.equals(mUsername)))
                    {
                        voteBtn.setVisibility(View.VISIBLE);

                        mSendButton.setEnabled(false);

                        mMessageEditText.setFocusable(false);

                        //hide keyboard
                        EditText editText = (EditText) findViewById(R.id.messageEditText);
                        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    }
                    else
                    {
                        mSendButton.setEnabled(false);

                        mMessageEditText.setFocusable(false);

                        voteBtn.setVisibility(View.INVISIBLE);

                        //hide keyboard
                        EditText editText = (EditText) findViewById(R.id.messageEditText);
                        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
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

        DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(selectedMainText, selectetUserName, selectedSubject, key, null, "grey","No");
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

    private void vote()
    {
        mMessagesDatabaseReference.removeEventListener(mDChildEventListener);
        VoteActivity voteB = new VoteActivity();
        voteB.vote(lessonNameNewQuestion, lessonDirectionNewQuestion, yearOfClassNewQuestion, key, mMessageListView, mDMessageAdapter);

    }
}