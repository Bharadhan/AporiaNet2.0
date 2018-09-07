package christos.voutselas.aporianet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class CreditRemoval extends AppCompatActivity
{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void removeCredit()
    {
        new CreditRemoval.DownloadFileFromURL(new CreditRemoval.DownloadFileFromURL.AsynResponse()
        {
            @Override
            public void processFinish(Boolean output)
            {
                // you can go here
            }
        }).execute("remove credit");
    }


    public static class DownloadFileFromURL extends AsyncTask<String, String, String>
    {
        private ChildEventListener mCreditDChildEventListener;
        private Integer creditNumber = 0;
        private DatabaseReference mMessagesDatabaseReferenceV;
        private FirebaseDatabase mFirebaseDatabase;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseStorage mFirebaseStorage;

        public interface AsynResponse
        {
            void processFinish(Boolean output);
        }

        CreditRemoval.DownloadFileFromURL.AsynResponse asynResponse = null;

        public DownloadFileFromURL(CreditRemoval.DownloadFileFromURL.AsynResponse asynResponse)
        {
            this.asynResponse = asynResponse;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(String f_url)
        {
            // super.onPostExecute(f_url);
            asynResponse.processFinish(true);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url)
        {
            // Initialize Firebase components
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseStorage = FirebaseStorage.getInstance();

            mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName);

            if (mCreditDChildEventListener == null)
            {
                mCreditDChildEventListener = new ChildEventListener()
                {
                    @Override
                    public void onChildAdded(DataSnapshot dADataSnapshot, String keyOne)
                    {
                        VoteMessage vote = dADataSnapshot.getValue(VoteMessage.class);
                        creditNumber = Integer.parseInt(String.valueOf(vote.getVotesNumbres()));
                        creditNumber = creditNumber - 20;
                        String creditN = String.valueOf(creditNumber);
                        long longCredit = Long.parseLong(creditN);
                        mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName).child(MainActivity.userKey);
                        mMessagesDatabaseReferenceV.child("votesNumbres").setValue( longCredit);

                    }
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                    public void onChildRemoved(DataSnapshot dataSnapshot) {}
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                    public void onCancelled(DatabaseError databaseError) {}
                };
                mMessagesDatabaseReferenceV.addChildEventListener(mCreditDChildEventListener);
            }

            return null;
        }
    }
}
