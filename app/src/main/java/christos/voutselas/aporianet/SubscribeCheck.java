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

            mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Α´ ΛΥΚΕΙΟΥ").child("-").child("ΑΛΓΕΒΡΑ").child(MainActivity.useName);


            mSubMessagesDatabaseReferenceV.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (!dataSnapshot.exists()) {
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Α´ ΛΥΚΕΙΟΥ").child("-").child("ΑΡΧΑΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Α´ ΛΥΚΕΙΟΥ").child("-").child("ΕΚΘΕΣΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Α´ ΛΥΚΕΙΟΥ").child("-").child("ΓΕΩΜΕΤΡΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Α´ ΛΥΚΕΙΟΥ").child("-").child("ΦΥΣΙΚΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Α´ ΛΥΚΕΙΟΥ").child("-").child("ΧΗΜΕΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("-").child("ΕΚΘΕΣΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("-").child("ΑΛΓΕΒΡΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("-").child("ΓΕΩΜΕΤΡΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("ΠΡΟΣΑΝΑΤΟΛΙΣΜΟΥ").child("ΜΑΘΗΜΑΤΙΚΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("ΠΡΟΣΑΝΑΤΟΛΙΣΜΟΥ").child("ΦΥΣΙΚΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("-").child("ΦΥΣΙΚΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Β´ ΛΥΚΕΙΟΥ").child("-").child("ΧΗΜΕΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΘΕΤΙΚΩΝ ΚΑΙ ΥΓΕΙΑΣ").child("ΕΚΘΕΣΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΘΕΤΙΚΩΝ ΚΑΙ ΥΓΕΙΑΣ").child("ΦΥΣΙΚΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΘΕΤΙΚΩΝ ΚΑΙ ΥΓΕΙΑΣ").child("ΧΗΜΕΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΘΕΤΙΚΩΝ").child("ΜΑΘΗΜΑΤΙΚΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΥΓΕΙΑΣ").child("ΒΙΟΛΟΓΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΟΙΚΟΝΟΜΙΑΣ").child("ΕΚΘΕΣΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΟΙΚΟΝΟΜΙΑΣ").child("ΜΑΘΗΜΑΤΙΚΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΟΙΚΟΝΟΜΙΑΣ").child("ΑΟΘ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΟΙΚΟΝΟΜΙΑΣ").child("ΑΕ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΑΝΘΡΩΠΙΣΤΙΚΩΝ ΣΠΟΥΔΩΝ").child("ΕΚΘΕΣΗ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΑΝΘΡΩΠΙΣΤΙΚΩΝ ΣΠΟΥΔΩΝ").child("ΑΡΧΑΙΑ ΓΝΩΣΤΟ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΑΝΘΡΩΠΙΣΤΙΚΩΝ ΣΠΟΥΔΩΝ").child("ΑΡΧΑΙΑ ΑΓΝΩΣΤΟ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΑΝΘΡΩΠΙΣΤΙΚΩΝ ΣΠΟΥΔΩΝ").child("ΙΣΤΟΡΙΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("Γ´ ΛΥΚΕΙΟΥ").child("ΑΝΘΡΩΠΙΣΤΙΚΩΝ ΣΠΟΥΔΩΝ").child("ΛΑΤΙΝΙΚΑ").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("ΡΟΜΠΟΤΙΚΗ").child("-").child("RoboKid").child(MainActivity.useName);
                        mSubMessagesDatabaseReferenceV.push().child("sub").setValue("No");
                        mSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child("ΡΟΜΠΟΤΙΚΗ").child("-").child("RoboBaby").child(MainActivity.useName);
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

