package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import java.util.ArrayList;
import java.util.List;

public class NewQuestionActivity extends AppCompatActivity
{
    private ImageButton mPhotoPickerButton;
    private static final int RC_PHOTO_PICKER =  2;
    private String lessonNameNewQuestion = "";
    private String lessonDirectionNewQuestion = "";
    private String yearOfClassNewQuestion = "";
    private String subject = "";
    private Button cancelBtn;
    private Button sudmitBtn;
    private String mUsername;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReference;
    private EditText mMessageEditText;
    private EditText mSubbject;
    private boolean bHasContent;
    private boolean subJectHasContent;
    private String strSubject = "";
    private DatabaseReference mFirebaseDatabaseReference;
    private ChildEventListener mChildEventListener;
    private MessageAdapter mMessageAdapter;
    private ListView mMessageListView;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton);
        mMessageEditText = (EditText) findViewById(R.id.questions);
        mSubbject = (EditText) findViewById(R.id.subjectArea);
        setContentView(R.layout.list_forum);
        mMessageListView = findViewById(R.id.listViewAs);
        setContentView(R.layout.new_question);

        mUsername = MainActivity.useName;

        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        updateFields();

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/jpeg");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        cancelBtn = (Button) findViewById(R.id.rejectBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sudmitBtn = (Button) findViewById(R.id.submitBtn);
        sudmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkEmptyText();

                checkSubject();

                if (bHasContent && subJectHasContent)
                {

                    // Initialize Firebase components
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    mFirebaseAuth = FirebaseAuth.getInstance();
                    mFirebaseStorage = FirebaseStorage.getInstance();

                    mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                            .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(mUsername).child(strSubject);

                    FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, null);
                    mMessagesDatabaseReference.push().setValue(friendlyMessage);

                    // Clear input box
                    mMessageEditText.setText("");

                    finish();

                  //  attachDatabaseReadListener();

                }
                else if (!subJectHasContent)
                {
                    mSubbject.setError("Αυτό το πεδίο δεν μπορεί να είναι κενό.");
                    mSubbject.requestFocus();
                    return;
                }
                else
                {
                    mMessageEditText.setError("Αυτό το πεδίο δεν μπορεί να είναι κενό.");
                    mMessageEditText.requestFocus();
                }
            }
        });
    }

    private void updateFields()
    {
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");

        final TextView lessonNameTextViewNewQuestion = (TextView) findViewById(R.id.lessonNewQuestion);
        final TextView lessonDirectionTextViewNewQuestion = (TextView) findViewById(R.id.derectionNewQuestion);
        final TextView yearClassTextViewNewQuestion = (TextView) findViewById(R.id.yearClassNewQuestion);

        lessonNameTextViewNewQuestion.setText(lessonNameNewQuestion);
        lessonDirectionTextViewNewQuestion.setText(lessonDirectionNewQuestion);
        yearClassTextViewNewQuestion.setText(yearOfClassNewQuestion);
    }

    private void checkEmptyText ()
    {
        mMessageEditText = (EditText) findViewById(R.id.questions);
        String strUserName = mMessageEditText.getText().toString();

        if(TextUtils.isEmpty(strUserName)) {

            bHasContent = false;
        }
        else
        {
            bHasContent = true;
        }
    }

    private void checkSubject ()
    {
        mSubbject = (EditText) findViewById(R.id.subjectArea);
        strSubject = mSubbject.getText().toString();

        if(TextUtils.isEmpty(strSubject)) {

            subJectHasContent = false;
    }
        else
        {
            subJectHasContent = true;
        }
    }

 /*   private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    FriendlyMessage friendlyMessage = dataSnapshot.getValue(FriendlyMessage.class);
                    System.out.println("The updated post title is: " + friendlyMessage.getName());
                    mMessageAdapter.add(friendlyMessage);
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mChildEventListener);
        }
    } */
}
