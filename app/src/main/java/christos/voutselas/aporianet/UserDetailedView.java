package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.Collections;
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
    private ImageView voteBtn;
    private TextView votedMessage;
    private ImageView backBtn;

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
        voteBtn = (ImageView) findViewById(R.id.fab);
        voteBtn.setVisibility(View.INVISIBLE);
        votedMessage = (TextView) findViewById(R.id.voted);
        votedMessage.setVisibility(View.INVISIBLE);
        backBtn = (ImageView) findViewById(R.id.backBtn);

        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");
        key = getIntent().getStringExtra("key");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        userText = getIntent().getStringExtra("userText");

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


        DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(userText, mUsername, "", "", "", "blue", "No");
        mMessagesDatabaseReference.push().setValue(dFriendlyMessage);



        mSendButton.setEnabled(false);
        mMessageEditText.setFocusable(false);

        readData();

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }

    private void readData()
    {
      //  mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

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
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        switch (yearOfClassNewQuestion)
        {
            case "Α´ ΛΥΚΕΙΟΥ" :
                Intent intent = new Intent(UserDetailedView.this, FirstYearForumView.class);
                intent.putExtra("lessonName", lessonNameNewQuestion);
                intent.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent.putExtra("yearClass", yearOfClassNewQuestion);
                intent.putExtra("back", "Yes");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            case "Β´ ΛΥΚΕΙΟΥ" :
                Intent  intent1 = new Intent(UserDetailedView.this, SecondYearForumView.class);
                intent1.putExtra("lessonName", lessonNameNewQuestion);
                intent1.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent1.putExtra("yearClass", yearOfClassNewQuestion);
                intent1.putExtra("back", "Yes");
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();

            case "Γ´ ΛΥΚΕΙΟΥ" :
                Intent  intent2 = new Intent(UserDetailedView.this, ThirdYearForumView.class);
                intent2.putExtra("lessonName", lessonNameNewQuestion);
                intent2.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent2.putExtra("yearClass", yearOfClassNewQuestion);
                intent2.putExtra("back", "Yes");
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();

            case "ΡΟΜΠΟΤΙΚΗ" :
                Intent  intent3 = new Intent(UserDetailedView.this, RoboticForumView.class);
                intent3.putExtra("lessonName", lessonNameNewQuestion);
                intent3.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent3.putExtra("yearClass", yearOfClassNewQuestion);
                intent3.putExtra("back", "Yes");
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                finish();
        }
    }
}
