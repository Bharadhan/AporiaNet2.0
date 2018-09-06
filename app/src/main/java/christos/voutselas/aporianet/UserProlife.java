package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class UserProlife extends AppCompatActivity
{
    private String mUsername;
    private TextView setUsername;
    private TextView creditsNumber;
    private ChildEventListener mVotesChildEventListener;
    private String votes = "";
    private Integer availiableCredits;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReferenceV;
    private ProgressBar mProgressBar;
    private Integer voteNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.user_profile);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mUsername = MainActivity.useName;
        setUsername = (TextView) findViewById(R.id.userName);
        creditsNumber = (TextView) findViewById(R.id.credits);
        setUsername.setText(mUsername);

        readData();
    }

    private void readData()
    {
        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(mUsername).child(MainActivity.userKey);

        if (mVotesChildEventListener == null)
        {
            mProgressBar.setVisibility(ProgressBar.VISIBLE);

            mVotesChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dDataSnapshot, String keyOne)
                {
                    VoteMessage voteMessage = dDataSnapshot.getValue(VoteMessage.class);
                    votes = voteMessage.getVotesNumbres();
                    voteNumber = Integer.parseInt(votes);

                    availiableCredits = (voteNumber - 1) * 8;

                    creditsNumber.setText("Total Credits: " +  availiableCredits);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);

                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReferenceV.addChildEventListener(mVotesChildEventListener);
        }
    }
}
