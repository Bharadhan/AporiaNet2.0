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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class DetailedView extends AppCompatActivity
{
    private String selectetUserName = "";
    private String selectedSubject = "";
    private String selectedMainText = "";
    private String yearOfClassNewQuestion = "";
    private String lessonDirectionNewQuestion = "";
    private String lessonNameNewQuestion = "";
    private DetailedMessageAdapter mDMessageAdapter;
    private ListView mMessageListView;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private DatabaseReference mMessagesDatabaseReference;
    private DatabaseReference mVoteMessagesDatabaseReference;
    private String key = "";
    private ProgressBar mProgressBar;
    private ChildEventListener mDChildEventListener;
    private EditText mMessageEditText;
    private Button mSendButton;
    private EditText userInput;
    private String mUsername;
    private String userText = "";
    private String postedName = "";
    private ImageView voteBtn;
    private TextView votedMessage;
    private String vote = "";
    private ImageView backBtn;
    private String photoUrl = "";
    private String selectedPhotoUri = "";
    private List<DetailedFriendlyMessage> dFriendlyMessages;
    private ImageButton photoPickerButtonAnwnser;
    private static final int RC_PHOTO_PICKER =  2;
    private Uri selectedImageUri = null;
    private StorageReference mChatPhotosStorageReference;
    private StorageReference photoRef;
    private String selectImage = "No";
    private String stringdate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.detailed_message);
        userInput = (EditText) findViewById(R.id.messageEditText);
        mMessageListView = findViewById(R.id.listViewAs_detailed);
        yearOfClassNewQuestion = getIntent().getStringExtra("yearOfClass");
        key = getIntent().getStringExtra("selectedKey");
        lessonDirectionNewQuestion = getIntent().getStringExtra("lessonDirection");
        lessonNameNewQuestion = getIntent().getStringExtra("lessonName");
        selectetUserName = getIntent().getStringExtra("selectedUserName");
        selectedSubject = getIntent().getStringExtra("selectedSubject");
        selectedMainText = getIntent().getStringExtra("selectedMainText");
        vote = getIntent().getStringExtra("vote");
        photoUrl = getIntent().getStringExtra("photoUrl");
        mMessageEditText = (EditText) findViewById(R.id.messageEditText);
        mSendButton = (Button) findViewById(R.id.sendButton);
        mUsername = MainActivity.useName;
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        votedMessage = (TextView) findViewById(R.id.voted);
        votedMessage.setVisibility(View.INVISIBLE);
        voteBtn = (ImageView) findViewById(R.id.fab);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        voteBtn.setVisibility(View.INVISIBLE);
        photoPickerButtonAnwnser = (ImageButton) findViewById(R.id.photoPickerButtonAnwnser);

        // Initialize message ListView and its adapter
        dFriendlyMessages = new ArrayList<>();
        mDMessageAdapter = new DetailedMessageAdapter(this, R.layout.question_message_view, dFriendlyMessages);
        mMessageListView.setAdapter(mDMessageAdapter);

        // Initialize Firebase components
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

        checkChildDetails();

        readData();

        // Send button sends a message and clears the EditText
        mSendButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mMessagesDatabaseReference.removeEventListener(mDChildEventListener);
                userText = userInput.getText().toString();
                storeDatetoFirebase();

                switch (selectImage) {
                    case "Yes":
                        uploadImage();
                        changeView();
                        return;
                    case "No":
                        changeView();
                        return;
                }


            }
        });

        voteBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mMessagesDatabaseReference.removeEventListener(mDChildEventListener);

                vote();

            }
        });

        photoPickerButtonAnwnser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);
            }
        });

        mMessageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                DetailedFriendlyMessage message = dFriendlyMessages.get(position);
                selectedPhotoUri = message.getPhotoUrl();
                Intent intent = new Intent(getApplicationContext(), ImageViewExtention.class);

                if(selectedPhotoUri.equals(""))
                {
                    return;
                }
                else
                {

                    intent.putExtra("imageUri", selectedPhotoUri);
                    intent.putExtra("subject", selectedSubject);
                    intent.putExtra("name", selectetUserName);
                    startActivity(intent);
                }


            }
        });

        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();

            mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                    .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

            mChatPhotosStorageReference = mFirebaseStorage.getReference().child("photos").child(yearOfClassNewQuestion)
                    .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(selectedSubject).child(selectedImageUri.getLastPathSegment());

            // Get a reference to store file at chat_photos/<FILENAME>
            photoRef = mChatPhotosStorageReference.child(selectedImageUri.getLastPathSegment());

            Toast.makeText(DetailedView.this, "Attachment has been selected", Toast.LENGTH_SHORT).show();

            selectImage = "Yes";

        }
    }
    private void checkChildDetails()
    {
        mMessagesDatabaseReference.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {

                if (!dataSnapshot.exists())
                {
                    findQuestion();

                }
                else {

                    if (dataSnapshot.getChildrenCount() < 2 && !(selectetUserName.equals(mUsername)))
                    {
                        mSendButton.setEnabled(true);
                        photoPickerButtonAnwnser.setEnabled(true);

                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(mMessageEditText.getWindowToken(), 0);

                        voteBtn.setVisibility(View.INVISIBLE);
                        votedMessage.setVisibility(View.INVISIBLE);

                    }
                    else if (dataSnapshot.getChildrenCount() > 1 && (selectetUserName.equals(mUsername)))
                    {
                        mSendButton.setEnabled(false);
                        mMessageEditText.setFocusable(false);
                        photoPickerButtonAnwnser.setEnabled(false);

                        switch (vote)
                        {
                            case "Yes":
                                voteBtn.setVisibility(View.INVISIBLE);
                                votedMessage.setVisibility(View.VISIBLE);
                                break;

                            case "No":
                                votedMessage.setVisibility(View.INVISIBLE);
                                voteBtn.setVisibility(View.VISIBLE);
                        }

                        //hide keyboard
                        EditText editText = (EditText) findViewById(R.id.messageEditText);
                        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

                    }
                    else
                    {
                        mSendButton.setEnabled(false);
                        mMessageEditText.setFocusable(false);
                        photoPickerButtonAnwnser.setEnabled(false);
                        voteBtn.setVisibility(View.INVISIBLE);
                        votedMessage.setVisibility(View.INVISIBLE);

                        //hide keyboard
                        EditText editText = (EditText) findViewById(R.id.messageEditText);
                        editText.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void findQuestion()
    {
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child(yearOfClassNewQuestion)
                .child(lessonDirectionNewQuestion).child(lessonNameNewQuestion).child(key).child("questions");

        DetailedFriendlyMessage dFriendlyMessage = new DetailedFriendlyMessage(selectedMainText, selectetUserName, selectedSubject, key, photoUrl, "grey","No");
        mMessagesDatabaseReference.push().setValue(dFriendlyMessage);
    }

    private void readData()
    {
        mProgressBar.setVisibility(ProgressBar.VISIBLE);

        if (mDChildEventListener == null)
        {
            mDChildEventListener = new ChildEventListener()
            {
                @Override
                public void onChildAdded(DataSnapshot dDataSnapshot, String keyOne)
                {
                    DetailedFriendlyMessage detailedFriendlyMessage = dDataSnapshot.getValue(DetailedFriendlyMessage.class);
                    postedName = detailedFriendlyMessage.getName();
                    mDMessageAdapter.add(detailedFriendlyMessage);
                    mProgressBar.setVisibility(ProgressBar.INVISIBLE);
                }
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
                public void onChildRemoved(DataSnapshot dataSnapshot) {}
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
                public void onCancelled(DatabaseError databaseError) {}
            };
            mMessagesDatabaseReference.addChildEventListener(mDChildEventListener);
        }
    }

    private void changeView()
    {
        Intent intent = new Intent(getApplicationContext(), UserDetailedView.class);
        intent.putExtra("lessonName", lessonNameNewQuestion);
        intent.putExtra("lessonDirection", lessonDirectionNewQuestion);
        intent.putExtra("yearOfClass", yearOfClassNewQuestion);
        intent.putExtra("key", key);
        intent.putExtra("selectedSubject", selectedSubject);
        intent.putExtra("selectedMainText", selectedMainText);
        intent.putExtra("userText", userText);


        intent.putExtra("selectImage", selectImage);






        startActivity(intent);
    }

    private void vote()
    {
        mMessagesDatabaseReference.removeEventListener(mDChildEventListener);
        VoteActivity voteB = new VoteActivity();
        voteB.vote(lessonNameNewQuestion, lessonDirectionNewQuestion, yearOfClassNewQuestion, key, mMessageListView, mDMessageAdapter);
        voteBtn.setVisibility(View.INVISIBLE);
        votedMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        switch (yearOfClassNewQuestion)
        {
            case "Α´ ΛΥΚΕΙΟΥ" :
                Intent  intent = new Intent(DetailedView.this, FirstYearForumView.class);
                intent.putExtra("lessonName", lessonNameNewQuestion);
                intent.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent.putExtra("yearClass", yearOfClassNewQuestion);
                intent.putExtra("back", "Yes");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return;
            case "Β´ ΛΥΚΕΙΟΥ" :
                Intent  intent1 = new Intent(DetailedView.this, SecondYearForumView.class);
                intent1.putExtra("lessonName", lessonNameNewQuestion);
                intent1.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent1.putExtra("yearClass", yearOfClassNewQuestion);
                intent1.putExtra("back", "Yes");
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                finish();
                return;

            case "Γ´ ΛΥΚΕΙΟΥ" :
                Intent  intent2 = new Intent(DetailedView.this, ThirdYearForumView.class);
                intent2.putExtra("lessonName", lessonNameNewQuestion);
                intent2.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent2.putExtra("yearClass", yearOfClassNewQuestion);
                intent2.putExtra("back", "Yes");
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                finish();
                return;
            case "ΡΟΜΠΟΤΙΚΗ" :
                Intent  intent3 = new Intent(DetailedView.this, RoboticForumView.class);
                intent3.putExtra("lessonName", lessonNameNewQuestion);
                intent3.putExtra("courseDirection", lessonDirectionNewQuestion);
                intent3.putExtra("yearClass", yearOfClassNewQuestion);
                intent3.putExtra("back", "Yes");
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent3);
                finish();
                return;
        }
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
                        FriendlyMessage friendlyMessage = new FriendlyMessage(mMessageEditText.getText().toString(), mUsername, selectedSubject, downloadUrlFinal.toString(), "No", stringdate);
                        mMessagesDatabaseReference.push().setValue(friendlyMessage);
                        progressDialog.dismiss();
                        Toast.makeText(DetailedView.this, "Uploaded", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(DetailedView.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    public void storeDatetoFirebase() {

        Date date = new Date();
        SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        stringdate = dt.format(date);

        System.out.println("Submission Date: " + stringdate);
    }

}