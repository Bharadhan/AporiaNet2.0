package christos.voutselas.aporianet;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public class VoteActivity extends AppCompatActivity
{
    public static String mLessonName;
    public static String mLessonDirection;
    public static String mYearOfClass;
    public static String mKeyNumber;
    public static String mPostedName;
    public static String mSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
    }

    public void vote (String lessonName, String lessonDirection, String yearOfClass, String keyNumber, ListView mMessageListView, DetailedMessageAdapter mDMessageAdapter, String postedName, String subject)
    {
        mLessonName  = lessonName;
        mLessonDirection = lessonDirection;
        mYearOfClass = yearOfClass;
        mKeyNumber = keyNumber;
        mPostedName = postedName;
        mSubject = subject;


        new VoteActivity.VoteFromURL(new VoteActivity.VoteFromURL.AsynResponse()
        {
            @Override
            public void processFinish(Boolean output)
            {
                // you can go here
            }
        }).execute();


        new VoteActivity.VoteMySelfFromURL(new VoteActivity.VoteMySelfFromURL.AsynResponse()
        {
            @Override
            public void processFinish(Boolean output)
            {
                // you can go here
            }
        }).execute();
    }

    private static class VoteFromURL extends AsyncTask<String, String, String>
    {
        private String lessonName = "";
        private String lessonDirection = "";
        private String yearOfClass = "";
        private String keyNumber = "";
        private String postedName = "";
        private String subject = "";
        private String votedUserName = "";
        private Integer voteNumber = 0;
        private String crKey = "";
        private FirebaseDatabase mFirebaseDatabase;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseStorage mFirebaseStorage;
        private DatabaseReference mVotedMessagesDatabaseReference;
        private DatabaseReference mMessagesDatabaseReferenceV;
        private DatabaseReference mUMessagesDatabaseReferenceV;
        private ChildEventListener mCreditDChildEventListener;

        public interface AsynResponse
        {
            void processFinish(Boolean output);
        }

        AsynResponse asynResponse = null;

        public VoteFromURL(AsynResponse asynResponse)
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
            lessonName = VoteActivity.mLessonName;
            lessonDirection = VoteActivity.mLessonDirection;
            yearOfClass = VoteActivity.mYearOfClass;
            keyNumber = VoteActivity.mKeyNumber;
            postedName = VoteActivity.mPostedName;
            subject = VoteActivity.mSubject;

            try
            {
                votedUserName = MainActivity.useName;
                // Initialize Firebase components
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseStorage = FirebaseStorage.getInstance();

                mVotedMessagesDatabaseReference = mFirebaseDatabase.getReference().child("voted").child(yearOfClass)
                        .child(lessonDirection).child(lessonName).child(subject);

                VotedMessage votedMessage = new VotedMessage(votedUserName);
                mVotedMessagesDatabaseReference.push().setValue(votedMessage);

                mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(postedName);
                mUMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName);


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
                            String finalVote =  String.valueOf(voteNumber  + 2);
                            long longCreditNumber = Long.parseLong(finalVote);
                            mMessagesDatabaseReferenceV.child("votesNumbres").setValue( longCreditNumber);


                      //      mUMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(postedName).child(CreditActivity.CheckUserCreditNumber.userKey);
                     //       String finalVoteU =  String.valueOf(voteNumber  + 2);
                     //       long longCreditNumberU = Long.parseLong(finalVoteU);/   mUMessagesDatabaseReferenceV.child("votesNumbres").setValue( longCreditNumberU);



                        }
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    mMessagesDatabaseReferenceV.addChildEventListener(mCreditDChildEventListener);
                }

            }
            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
    }

    private static class VoteMySelfFromURL extends AsyncTask<String, String, String>
    {
        private String lessonName = "";
        private String lessonDirection = "";
        private String yearOfClass = "";
        private String keyNumber = "";
        private String postedName = "";
        private String subject = "";
        private String votedUserName = "";
        private Integer voteNumber = 0;
        private String crKey = "";
        private FirebaseDatabase mFirebaseDatabase;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseStorage mFirebaseStorage;
        private DatabaseReference mVotedMessagesDatabaseReference;
        private DatabaseReference mMessagesDatabaseReferenceV;
        private DatabaseReference mUMessagesDatabaseReferenceV;
        private ChildEventListener mCreditDChildEventListener;

        public interface AsynResponse
        {
            void processFinish(Boolean output);
        }

        AsynResponse asynResponse = null;

        public VoteMySelfFromURL(AsynResponse asynResponse)
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
            lessonName = VoteActivity.mLessonName;
            lessonDirection = VoteActivity.mLessonDirection;
            yearOfClass = VoteActivity.mYearOfClass;
            keyNumber = VoteActivity.mKeyNumber;
            postedName = VoteActivity.mPostedName;
            subject = VoteActivity.mSubject;

            try
            {
                votedUserName = MainActivity.useName;
                // Initialize Firebase components
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                mFirebaseAuth = FirebaseAuth.getInstance();
                mFirebaseStorage = FirebaseStorage.getInstance();

                mUMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName);


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

                            mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(MainActivity.useName).child(CreditActivity.CheckUserCreditNumber.userKey);
                            String finalVote =  String.valueOf(voteNumber  + 2);
                            long longCreditNumber = Long.parseLong(finalVote);
                            mMessagesDatabaseReferenceV.child("votesNumbres").setValue( longCreditNumber);


                            //      mUMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(postedName).child(CreditActivity.CheckUserCreditNumber.userKey);
                            //       String finalVoteU =  String.valueOf(voteNumber  + 2);
                            //       long longCreditNumberU = Long.parseLong(finalVoteU);/   mUMessagesDatabaseReferenceV.child("votesNumbres").setValue( longCreditNumberU);



                        }
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    mUMessagesDatabaseReferenceV.addChildEventListener(mCreditDChildEventListener);
                }

            }
            catch (Exception e)
            {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
    }
}
