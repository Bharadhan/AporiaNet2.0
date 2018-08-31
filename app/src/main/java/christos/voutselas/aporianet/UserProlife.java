package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
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

public class UserProlife extends AppCompatActivity
{
    private String mUsername;
    private TextView setUsername;
    private TextView creditsNumber;
    private ChildEventListener mVotesChildEventListener;
    private Integer votes = 0;
    private Integer availiableCredits;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReferenceV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.user_profile);

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

        mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(mUsername);

        if (mVotesChildEventListener == null)
        {
            mVotesChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dDataSnapshot, String keyOne)
                {
                    VoteMessage voteMessage = dDataSnapshot.getValue(VoteMessage.class);
                    voteMessage.getVotesNumbres();
                    votes = votes + 1;
                    availiableCredits = votes * 8;

                    creditsNumber.setText("Total Credits: " +   availiableCredits);

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
