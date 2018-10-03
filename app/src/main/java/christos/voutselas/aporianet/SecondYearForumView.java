package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SecondYearForumView extends AppCompatActivity
{
    private Integer lessoonNamePotition = 0;
    private Integer courseDirectionPotition = 0;
    private Integer yearClassPotition = 0;
    private Button newQuestion;
    private String strUserName = "";
    private String lessonName = "";
    private String lessonDirection = "";
    private String yearOfClass = "";
    private String back = "No";
    private String mUsername;
    private ListView mMessageListView;
    private ProgressBar mProgressBar;
    private List<FriendlyMessage> friendlyMessages;
    private MessageAdapter mMessageAdapter;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;
    private ChildEventListener mChildEventListener;
    private String selectetUserNAme = "";
    private String selectedSubject = "";
    private String selectedMainText = "";
    private String key = "";
    private String selectedKey = "";
    private String wrongAnwser = "";
    private ImageView backBtn;
    private Integer finalPos = 0;
    private String time = "";
    private String selectedUrl = "";
    private Button subscribe;
    private Button unSubscribe;
    private String sub = "";
    private ChildEventListener mSubChildEventListener;
    private DatabaseReference mAddSubMessagesDatabaseReferenceV;
    private DatabaseReference mAddUserSubMessagesDatabaseReferenceV;
    private ChildEventListener mSubRemovalChildEventListener;
    private DatabaseReference mCheckSubMessagesDatabaseReferenceV;
    private ChildEventListener mSubCheckChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.list_forum);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        subscribe = (Button) findViewById(R.id.subBTtn);
        unSubscribe = (Button) findViewById(R.id.unSubBtn);
        back = getIntent().getStringExtra("back");

        updateView();

        readData();

        checkSubBtn();

        newQuestion = (Button) findViewById(R.id.newQuestionButton);
        newQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), NewQuestionActivity.class);
                intent.putExtra("finalResults", strUserName);
                intent.putExtra("lessonName", lessonName);
                intent.putExtra("lessonDirection", lessonDirection);
                intent.putExtra("yearOfClass", yearOfClass);
                startActivity(intent);
            }
        });

        subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSubChildEventListener = null;

                mAddSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(yearOfClass).child(lessonDirection).child(lessonName).child(MainActivity.useName);

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

                mAddUserSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("subscriptions").child(yearOfClass).child(lessonDirection).child(lessonName);

                Query queryToGetData = mAddUserSubMessagesDatabaseReferenceV.orderByChild("user").equalTo(MainActivity.useName);


                queryToGetData.addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot)
                    {
                        if(!dataSnapshot.exists())
                        {
                            mAddUserSubMessagesDatabaseReferenceV.push().child("user").setValue(MainActivity.useName);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


        unSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mSubRemovalChildEventListener = null;

                mAddSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(yearOfClass).child(lessonDirection).child(lessonName).child(MainActivity.useName);

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

                mAddUserSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("subscriptions").child(yearOfClass).child(lessonDirection).child(lessonName);

                Query queryToGetData = mAddUserSubMessagesDatabaseReferenceV.orderByChild("user").equalTo(MainActivity.useName);

                mAddUserSubMessagesDatabaseReferenceV.removeValue();
            }
        });

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mMessagesDatabaseReference.removeEventListener(mChildEventListener);
                finalPos = MessageAdapter.pos;
                FriendlyMessage message = friendlyMessages.get(finalPos - position -1);
                selectetUserNAme =  message.getName();
                selectedSubject = message.getSubject();
                selectedMainText = message.getText();
                selectedKey = message.getKey();
                wrongAnwser = message.getVotes();
                selectedUrl = message.getPhotoUrl();
                time = message.getDate();
                Intent intent = new Intent(getApplicationContext(), DetailedView.class);
                intent.putExtra("lessonName", lessonName);
                intent.putExtra("lessonDirection", lessonDirection);
                intent.putExtra("yearOfClass", yearOfClass);
                intent.putExtra("selectedUserName", selectetUserNAme);
                intent.putExtra("selectedSubject", selectedSubject);
                intent.putExtra("selectedMainText", selectedMainText);
                intent.putExtra("selectedKey", selectedKey);
                intent.putExtra("photoUrl", selectedUrl);
                intent.putExtra("wrongAnwser", wrongAnwser);
                intent.putExtra("time", time);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    private void updateView()
    {
        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.string.secondYearGeneralDirection, R.string.ekuesi, R.drawable.ekthesi, R.string.category_second_year));
        words.add(new Word(R.string.secondYearGeneralDirection, R.string.algevra, R.drawable.algevra, R.string.category_second_year));
        words.add(new Word(R.string.secondYearGeneralDirection, R.string.geometria, R.drawable.geometry, R.string.category_second_year));
        words.add(new Word(R.string.secondYearSpecialDirection, R.string.mathimatika, R.drawable.math_prosavatolismou, R.string.category_second_year));
        words.add(new Word(R.string.secondYearGeneralDirection, R.string.fysiki, R.drawable.fusiki, R.string.category_second_year));
        words.add(new Word(R.string.secondYearSpecialDirection, R.string.fysiki, R.drawable.fysiki_prosanatolismou, R.string.category_second_year));
        words.add(new Word(R.string.secondYearGeneralDirection, R.string.xhmeia, R.drawable.xhmeia, R.string.category_second_year));

        final TextView lessonNameTextView = (TextView) findViewById(R.id.lesson);
        final TextView lessonDirectionTextView = (TextView) findViewById(R.id.derection);
        final TextView yearClassTextView = (TextView) findViewById(R.id.yearClass);

        if (back.equals("No"))
        {
            lessoonNamePotition = Integer.parseInt(getIntent().getStringExtra("lessonName"));
            courseDirectionPotition = Integer.parseInt(getIntent().getStringExtra("courseDirection"));
            yearClassPotition = Integer.parseInt(getIntent().getStringExtra("yearClass"));

            lessonNameTextView.setText(lessoonNamePotition);
            lessonDirectionTextView.setText(courseDirectionPotition);
            yearClassTextView.setText(yearClassPotition);

            lessonName = lessonNameTextView.getText().toString();
            lessonDirection = lessonDirectionTextView.getText().toString();
            yearOfClass = yearClassTextView.getText().toString();
        }
        else
        {
            lessonName = getIntent().getStringExtra("lessonName");
            lessonDirection = getIntent().getStringExtra("courseDirection");
            yearOfClass = getIntent().getStringExtra("yearClass");

            lessonNameTextView.setText(lessonName);
            lessonDirectionTextView.setText(lessonDirection);
            yearClassTextView.setText(yearOfClass);
        }
    }

    private void readData()
    {
        mUsername = MainActivity.useName;

        mMessageListView = findViewById(R.id.listViewAs);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Initialize message ListView and its adapter
        friendlyMessages = new ArrayList<>();

        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize progress bar
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClass).child(lessonDirection).child(lessonName);

        if (mChildEventListener == null)
        {
            mChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String key)
                {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    String wrongMesage = friendlyMessage.getVotes();

                    if (wrongMesage.equals("No"))
                    {
                        key = dataSnapshot.getKey();
                        mMessageAdapter.add(friendlyMessage);
                        friendlyMessage.setKey(key);
                        mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                    }

                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }

    }

    private void checkSubBtn()
    {
        mCheckSubMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("SubCheck").child(yearOfClass).child(lessonDirection).child(lessonName).child(MainActivity.useName);

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
