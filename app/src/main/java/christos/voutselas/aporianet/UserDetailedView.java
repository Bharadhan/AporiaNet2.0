package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

public class UserDetailedView extends AppCompatActivity {

    private EditText mMessageEditText;
    private Button mSendButton;
    private EditText userInput;
    private String mUsername;
    private DetailedMessageAdapter mDMessageAdapter;
    private ListView mMessageListView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReference;
    private ProgressBar mProgressBar;
    private String yearOfClassNewQuestion = "";
    private String lessonDirectionNewQuestion = "";
    private String lessonNameNewQuestion = "";
    private String key;
    private ChildEventListener mDChildEventListener;
    private String userText = "";
    private String userTextFinal = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.detailed_message);
        userInput = (EditText) findViewById(R.id.messageEditText);
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mUsername = MainActivity.useName;
        mMessageListView = findViewById(R.id.listViewAs_detailed);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");
        key = getIntent().getStringExtra("key");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        userText = getIntent().getStringExtra("userText");

        // Initialize message ListView and its adapter
        List<DetailedFriendlyMessage> dFriendlyMessages = new ArrayList<>();
        mDMessageAdapter = new DetailedMessageAdapter(this, R.layout.detailed_message_view, dFriendlyMessages);
        mMessageListView.setAdapter(mDMessageAdapter);
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");
        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(userText, mUsername, "", "", null);
        mMessagesDatabaseReference.push().setValue(dFriendlyMessage);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        readData();

        // Enable Send button when there's text to send
        mMessageEditText.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if (charSequence.toString().trim().length() > 0)
                {
                    mSendButton.setEnabled(true);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
                else
                {
                    mSendButton.setEnabled(false);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //userText = "";
                //setContentView(R.layout.detailed_message);
                userTextFinal = userInput.getText().toString();
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(userTextFinal, mUsername, "", "", null);
                mMessagesDatabaseReference.push().setValue(dFriendlyMessage);
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                // Clear input box
                mMessageEditText.setText("");

                readData();
                mProgressBar.setVisibility(ProgressBar.INVISIBLE);

            }
        });
    }

    private void readData()
    {
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        mProgressBar.setVisibility(ProgressBar.INVISIBLE);

        if (mDChildEventListener == null)
        {
            mDChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dDataSnapshot, String key)
                {
                    DetailedFriendlyMessage detailedFriendlyMessage = dDataSnapshot.getValue(DetailedFriendlyMessage.class);
                    System.out.println("The updated post title is: " + detailedFriendlyMessage.getName());
                    mDMessageAdapter.add(detailedFriendlyMessage);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mDChildEventListener);
            mProgressBar.setVisibility(ProgressBar.INVISIBLE);
        }

    }


}
