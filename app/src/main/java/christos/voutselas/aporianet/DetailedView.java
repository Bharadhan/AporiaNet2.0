package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.detailed_message);


        findQuestion();


        readData();







    }

    private void findQuestion()
    {
        mMessageListView = findViewById(R.id.listViewAs_detailed);
        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");
        key = getIntent().getStringExtra("selectedKey");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        selectetUserName = getIntent().getStringExtra("selectedUserName");
        selectedSubject = getIntent().getStringExtra("selectedSubject");
        selectedMainText = getIntent().getStringExtra("selectedMainText");

        // Initialize message ListView and its adapter
        List<DetailedFriendlyMessage> dFriendlyMessages = new ArrayList<>();
        mDMessageAdapter = new DetailedMessageAdapter(this, R.layout.detailed_message_view, dFriendlyMessages);
        mMessageListView.setAdapter(mDMessageAdapter);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

        DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(selectedMainText, selectetUserName, selectedSubject, key, null);
        mMessagesDatabaseReference.push().setValue(dFriendlyMessage);

    }

    private void readData()
    {

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion).child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

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
}
