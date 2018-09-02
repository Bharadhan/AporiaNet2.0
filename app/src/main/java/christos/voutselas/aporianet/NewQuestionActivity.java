package christos.voutselas.aporianet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.text.DateFormat.getDateTimeInstance;

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
    private DatabaseReference mMessagesDatabaseReferencePhoto;
    private EditText mMessageEditText;
    private EditText mSubbject;
    private boolean bHasContent;
    private boolean subJectHasContent;
    public String strSubject = "";
    private DatabaseReference mFirebaseDatabaseReference;
    private ChildEventListener mChildEventListener;
    private MessageAdapter mMessageAdapter;
    private ListView mMessageListView;
    private String stringdate;
    private StorageReference mChatPhotosStorageReference;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);
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

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();


        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion);

        updateFields();

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton1);
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    storeDatetoFirebase();


                    FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, strSubject, null, "No", stringdate);
                    mMessagesDatabaseReference.push().setValue(friendlyMessage);


                    // Clear input box
                    mMessageEditText.setText("");

                    finish();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();

            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseStorage = FirebaseStorage.getInstance();

            mChatPhotosStorageReference = mFirebaseStorage.getReference().child("photos");


            // Get a reference to store file at chat_photos/<FILENAME>
            StorageReference photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            // Upload file to Firebase Storage
            photoRef.putFile(selectedImageUri)
                    .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // When the image has successfully uploaded, we get its download URL
                            String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();

                            // Set the download URL to the message box, so that the user can send it to the database
                            FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, strSubject, downloadUrl, "No", stringdate);
                            mMessagesDatabaseReference.push().setValue(friendlyMessage);
                        }
                    });
        }
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

        if(TextUtils.isEmpty(strSubject))
        {

            subJectHasContent = false;
        }
        else
        {
            subJectHasContent = true;
        }
    }

    public void storeDatetoFirebase() {

        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        stringdate = dt.format(date);

        System.out.println("Submission Date: " + stringdate);
    }
}
