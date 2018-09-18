package christos.voutselas.aporianet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class CreditActivity extends AppCompatActivity
{
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void removeCredit()
    {
        new CreditActivity.DownloadFileFromURL(new CreditActivity.DownloadFileFromURL.AsynResponse()
        {
            @Override
            public void processFinish(Boolean output)
            {
                // you can go here
            }
        }).execute("remove credit");
    }

    public void checkUserCredits()
    {
        new CreditActivity.CheckUserCreditNumber(new CreditActivity.CheckUserCreditNumber.AsynResponse()
        {
            @Override
            public void processFinish(Boolean output)
            {
                // you can go here
            }
        }).execute("check credit");

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

        CreditActivity.DownloadFileFromURL.AsynResponse asynResponse = null;

        public DownloadFileFromURL(CreditActivity.DownloadFileFromURL.AsynResponse asynResponse)
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
                        creditNumber -= 20;
                        String creditN = String.valueOf(creditNumber);
                        long longCredit = Long.parseLong(creditN);
                        mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName).child(CheckUserCreditNumber.userKey);
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

    public static class CheckUserCreditNumber extends AsyncTask<String, String, String>
    {
        private DatabaseReference mCreditsMessagesDatabaseReferenceVC;
        private FirebaseDatabase mFirebaseDatabase;
        private DatabaseReference mMessagesDatabaseRe;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseStorage mFirebaseStorage;
        private DatabaseReference mACreditsMessagesDatabaseReferenceV;
        private ChildEventListener mDChildEventListener;
        public static Integer userCredit;
        public static String userKey;

        public interface AsynResponse
        {
            void processFinish(Boolean output);
        }

        CreditActivity.CheckUserCreditNumber.AsynResponse asynResponse = null;

        public CheckUserCreditNumber(CreditActivity.CheckUserCreditNumber.AsynResponse asynResponse)
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

        @Override
        protected String doInBackground(String... f_url)
        {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseStorage = FirebaseStorage.getInstance();


            mCreditsMessagesDatabaseReferenceVC = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName);

            mCreditsMessagesDatabaseReferenceVC.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (!dataSnapshot.exists()) {
                        mCreditsMessagesDatabaseReferenceVC.push().child("votesNumbres").setValue(801);
                    } else {
                        mCreditsMessagesDatabaseReferenceVC = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName);

                        if (mDChildEventListener == null) {
                            mDChildEventListener = new ChildEventListener() {
                                @Override
                                public void onChildAdded(DataSnapshot dDataSnapshot, String keyOne) {
                                    VoteMessage voteNbr = dDataSnapshot.getValue(VoteMessage.class);
                                    Long creditN = voteNbr.getVotesNumbres();
                                    String creditNB = String.valueOf(creditN);
                                    userCredit = Integer.parseInt(creditNB);
                                    userKey = dDataSnapshot.getKey();
                                    System.out.print("aaaa");
                                }

                                public void onChildChanged(DataSnapshot dDataSnapshot, String s) {
                                    System.out.print("aaa");
                                }

                                public void onChildRemoved(DataSnapshot dDataSnapshot) {
                                    System.out.print("aaa");
                                }

                                public void onChildMoved(DataSnapshot dDataSnapshot, String s) {
                                    System.out.print("aaa");
                                }

                                public void onCancelled(DatabaseError dDataSnapshot) {
                                }
                            };
                            mCreditsMessagesDatabaseReferenceVC.addChildEventListener(mDChildEventListener);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
            return null;
        }
    }

}
