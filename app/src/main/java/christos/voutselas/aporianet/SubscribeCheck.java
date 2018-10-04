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

public class SubscribeCheck extends AppCompatActivity
{

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    public void checkUserSubs()
    {
        new SubscribeCheck.SubCheck(new SubscribeCheck.SubCheck.AsynResponse()
        {
            @Override
            public void processFinish(Boolean output)
            {
                // you can go here
            }
        }).execute("check subs");
    }

    public static class SubCheck extends AsyncTask<String, String, String>
    {
        private ChildEventListener mSubChildEventListener;
        private DatabaseReference mSubMessagesDatabaseReferenceV;
        private FirebaseDatabase mFirebaseDatabase;
        private FirebaseAuth mFirebaseAuth;
        private FirebaseStorage mFirebaseStorage;

        public interface AsynResponse
        {
            void processFinish(Boolean output);
        }

        SubscribeCheck.SubCheck.AsynResponse asynResponse = null;

        public SubCheck(SubscribeCheck.SubCheck.AsynResponse asynResponse)
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

            mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(MainActivity.useName);


            mSubMessagesDatabaseReferenceV.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (!dataSnapshot.exists()) {

                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
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

