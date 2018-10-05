package christos.voutselas.aporianet;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestActivity {
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mMessagesDatabaseReference;

    public void testActivity()
    {
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("notifications").child(MainActivity.uid);
        test friendlyMessage = new test(MainActivity.useName, MainActivity.uid, MainActivity.deviceToken);
        mMessagesDatabaseReference.push().setValue(friendlyMessage);
    }
}
