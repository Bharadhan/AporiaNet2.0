package christos.voutselas.aporianet;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage>
{
    public static Integer pos;

    //private Integer mPotition = FirstYearForumView.mPotition;


    public MessageAdapter(Context context, int resource, List<FriendlyMessage> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.item_message, parent, false);
        }

        ImageView photoImageView = (ImageView) convertView.findViewById(R.id.photoImageView);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.subjectMessageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message = getItem(position);
        FriendlyMessage message1 = getItem(position);

        assert message1 != null;
        boolean isPhoto = message1.getPhotoUrl() != null;
        if (isPhoto)
        {
            messageTextView.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(message1.getPhotoUrl())
                    .into(photoImageView);
        }
        else
        {
            messageTextView.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message1.getSubject());

        }
        authorTextView.setText(message1.getName());

        return convertView;
    }

    @Override
    public FriendlyMessage getItem(int position) {
        pos = (super.getCount());
        return super.getItem(super.getCount() - position - 1);
    }
}