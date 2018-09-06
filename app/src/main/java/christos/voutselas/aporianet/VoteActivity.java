package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class VoteActivity extends AppCompatActivity {

    private DetailedMessageAdapter mDMessageAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mMessagesDatabaseReferenceSecondary;
    private DatabaseReference mMessagesDatabaseReferenceV;
    private Task<Void> mMessagesDatabaseReference1;
    private ChildEventListener mDChildEventListener;
    private ChildEventListener mCreditDChildEventListener;
    private String answerName = "";
    private Integer votedName = 0;
    private Integer voteNumber = 0;
    private String crKey = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
    }

    public void vote (String lessonName, String lessonDirection, String yearOfClass, String keyNumber, ListView mMessageListView, DetailedMessageAdapter mDMessageAdapter, String postedName)
    {
        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClass)
                .child(lessonDirection).child(lessonName).child(keyNumber).child("questions");

        mMessagesDatabaseReferenceSecondary = mFirebaseDatabase.getReference().child(yearOfClass)
                .child(lessonDirection).child(lessonName).child(keyNumber);
        mMessagesDatabaseReferenceSecondary.child("votes").setValue("Yes");

        setVote(postedName);
    }

    private void setVote(final String postedName) {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(postedName);


        if (mCreditDChildEventListener == null)
        {
            mCreditDChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dADataSnapshot, String keyOne)
                {
                    VoteMessage vote = dADataSnapshot.getValue(VoteMessage.class);
                    crKey = dADataSnapshot.getKey();
                    voteNumber = Integer.parseInt(String.valueOf(vote.getVotesNumbres()));

                    mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(postedName).child(crKey);
                    String finalVote =  String.valueOf(voteNumber  + 1);
                    mMessagesDatabaseReferenceV.child("votesNumbres").setValue( finalVote);

                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReferenceV.addChildEventListener(mCreditDChildEventListener);
        }
    }
}
