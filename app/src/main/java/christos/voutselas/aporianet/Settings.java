package christos.voutselas.aporianet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class Settings extends AppCompatActivity
{
    private Button subscribe;
    private Button unSubscribe;
    private String sub = "";
    private ChildEventListener mSubChildEventListener;
    private DatabaseReference mAddSubMessagesDatabaseReferenceV;
    private DatabaseReference mAddUserSubMessagesDatabaseReferenceV;
    private ChildEventListener mSubRemovalChildEventListener;
    private DatabaseReference mCheckSubMessagesDatabaseReferenceV;
    private ChildEventListener mSubCheckChildEventListener;
    private FirebaseDatabase mFirebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.settings_view);
        subscribe = (Button) findViewById(R.id.subBTtn);
        unSubscribe = (Button) findViewById(R.id.unSubBtn);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        checkSubBtn();

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSubChildEventListener = null;

                mAddSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(MainActivity.useName);

                if (mSubChildEventListener == null)
                {
                    mSubChildEventListener = new ChildEventListener()
                    {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String key)
                        {

                            key = dataSnapshot.getKey();
                            mAddSubMessagesDatabaseReferenceV.child(key).child("sub").setValue("Yes");
                        }

                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    mAddSubMessagesDatabaseReferenceV.addChildEventListener(mSubChildEventListener);
                }

                unSubscribe.setVisibility(View.VISIBLE);
                subscribe.setVisibility(View.INVISIBLE);

                //mAddUserSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("notifications").child(MainActivity.uid);

                //mAddUserSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("deviceToken").child(MainActivity.uid);
               // mAddUserSubMessagesDatabaseReferenceV.push().setValue(MainActivity.deviceToken);

                mAddUserSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("Device Tokens").child(MainActivity.uid);
                mAddUserSubMessagesDatabaseReferenceV.setValue(MainActivity.deviceToken);

              /*  Query queryToGetData = mAddUserSubMessagesDatabaseReferenceV.orderByChild("user").equalTo(MainActivity.useName);


                queryToGetData.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(!dataSnapshot.exists())
                        {
                            test userMessage = new test(MainActivity.deviceToken);
                            mAddUserSubMessagesDatabaseReferenceV.push().setValue(userMessage);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

            }
        });


        unSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSubRemovalChildEventListener = null;

                mAddSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(MainActivity.useName);

                if (mSubRemovalChildEventListener == null)
                {
                    mSubRemovalChildEventListener = new ChildEventListener()
                    {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String key)
                        {

                            key = dataSnapshot.getKey();
                            mAddSubMessagesDatabaseReferenceV.child(key).child("sub").setValue("No");
                        }

                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                        public void onChildRemoved(DataSnapshot dataSnapshot) {}
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    mAddSubMessagesDatabaseReferenceV.addChildEventListener(mSubRemovalChildEventListener);
                }

                unSubscribe.setVisibility(View.INVISIBLE);
                subscribe.setVisibility(View.VISIBLE);

                mAddUserSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("subscriptions");

               // Query queryToGetData = mAddUserSubMessagesDatabaseReferenceV.orderByChild("user").equalTo(MainActivity.useName);

              //  mAddUserSubMessagesDatabaseReferenceV.removeValue();
            }
        });

    }

    private void checkSubBtn()
    {
        mCheckSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(MainActivity.useName);

        if (mSubCheckChildEventListener == null)
        {
            mSubCheckChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String key)
                {
                    SubMessage subMessage = dataSnapshot.getValue(SubMessage.class);
                    sub = subMessage.getSub();

                    if (sub.equals("No"))
                    {
                        unSubscribe.setVisibility(View.INVISIBLE);
                        subscribe.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        unSubscribe.setVisibility(View.VISIBLE);
                        subscribe.setVisibility(View.INVISIBLE);
                    }

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mCheckSubMessagesDatabaseReferenceV.addChildEventListener(mSubCheckChildEventListener);
        }
    }

}
