package christos.voutselas.aporianet;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private String stringdate = "";
    private StorageReference mChatPhotosStorageReference;
    private Uri selectedImageUri = null;
    private StorageReference photoRef;
    private String selectImage = "No";
    private ImageView imageView;
    private DatabaseReference mMessagesDatabaseReferenceV;
    private ChildEventListener mCreditDChildEventListener;
    private Integer creditNumber = 0;
    private String userHaveCredit = "Yes";
    private String creditRemoved = "No";
    private EditText questionFied;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_question);
        mSubbject = (EditText) findViewById(R.id.subjectArea);
        setContentView(R.layout.list_forum);
        mMessageListView = findViewById(R.id.listViewAs);
        setContentView(R.layout.new_question);
        mMessageEditText = (EditText) findViewById(R.id.questions);
        imageView = (ImageView) findViewById(R.id.imgView);
        mUsername = MainActivity.useName;

        //hide keyboard
        questionFied = (EditText) findViewById(R.id.questions);
        questionFied.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(questionFied.getWindowToken(), 0);


        // Initialize message ListView and its adapter
        List<FriendlyMessage> friendlyMessages = new ArrayList<>();
        mMessageAdapter = new MessageAdapter(this, R.layout.item_message, friendlyMessages);
        mMessageListView.setAdapter(mMessageAdapter);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReferenceV = mFirebaseDatabase.getReference().child("xVotesNumbers").child(mUsername);

        checkUserCredit();

        updateFields();

        // ImagePickerButton shows an image picker to upload a image for a message
        mPhotoPickerButton = (ImageButton) findViewById(R.id.photoPickerButton1);
        mPhotoPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSubject();

                if (subJectHasContent)
                {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                    startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
                }
                else
                {
                    mSubbject.setError("Αυτό το πεδίο δεν μπορεί να είναι κενό.");
                    mSubbject.requestFocus();
                    return;
                }
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
        sudmitBtn.setVisibility(View.VISIBLE);
        sudmitBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                storeDatetoFirebase();

                removeUserCredit();

                switch (selectImage)
                {
                    case "Yes":
                        uploadImage();
                        return;
                    case "No":

                        checkEmptyText();

                        checkSubject();

                        if (bHasContent && subJectHasContent)
                        {

                            mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("forum").child(yearOfClassNewQuestion)
                                    .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion);


                            FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, strSubject, "", "No", stringdate);
                            mMessagesDatabaseReference.push().setValue(friendlyMessage);

                            mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("notifications").child(MainActivity.uid);
                            mMessagesDatabaseReference.removeValue();
                            UserMessage userMessage = new UserMessage(MainActivity.useName, MainActivity.uid, MainActivity.deviceToken);
                            mMessagesDatabaseReference.push().setValue(userMessage);






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
                        return;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK)
        {
            selectedImageUri = data.getData();

            mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                    .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion);

            mChatPhotosStorageReference = mFirebaseStorage.getReference().child("photos").child(yearOfClassNewQuestion)
                    .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(strSubject).child(selectedImageUri.getLastPathSegment());

            // Get a reference to store file at chat_photos/<FILENAME>
            photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                imageView.setImageBitmap(bitmap);
                imageView.setVisibility(View.VISIBLE);
            }

            catch (IOException e)
            {
                e.printStackTrace();
            }

            selectImage = "Yes";
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
        SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        stringdate = dt.format(date);

        System.out.println("Submission Date: " + stringdate);
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();

        // Upload file to Firebase Storage
        mChatPhotosStorageReference.putFile(selectedImageUri)
                .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // When the image has successfully uploaded, we get its download URL
                       // String downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().toString();
                      //  Uri downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                        Task<Uri> downloadUrl = taskSnapshot.getStorage().getDownloadUrl();

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Uri downloadUrlFinal = downloadUrl.getResult();

                        // Set the download URL to the message box, so that the user can send it to the database
                        FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, strSubject, downloadUrlFinal.toString(), "No", stringdate);
                        mMessagesDatabaseReference.push().setValue(friendlyMessage);
                        progressDialog.dismiss();
                        Toast.makeText(NewQuestionActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(NewQuestionActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                .getTotalByteCount());
                        progressDialog.setMessage("Uploaded "+(int)progress+"%");
                    }
                });
    }

    private void checkUserCredit()
    {

        if (mCreditDChildEventListener == null)
        {
            mCreditDChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dADataSnapshot, String keyOne)
                {
                    VoteMessage vote = dADataSnapshot.getValue(VoteMessage.class);
                    creditNumber = Integer.parseInt(String.valueOf(vote.getVotesNumbres()));

                    if (creditNumber < 20)
                    {
                        userHaveCredit = "No";
                        Toast toast = Toast.makeText(NewQuestionActivity.this,"You don't have enough credit for new question!!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        sudmitBtn.setVisibility(View.INVISIBLE);
                        return;
                    }

                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReferenceV.addChildEventListener(mCreditDChildEventListener);
        }
    }

    private void removeUserCredit()
    {
        mMessagesDatabaseReferenceV.removeEventListener(mCreditDChildEventListener);

        CreditActivity creditRemoval = new CreditActivity();
        creditRemoval.removeCredit();
    }
}
