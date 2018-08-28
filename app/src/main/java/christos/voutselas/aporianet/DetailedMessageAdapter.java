package christos.voutselas.aporianet;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;


public class DetailedMessageAdapter extends ArrayAdapter<DetailedFriendlyMessage>
{

    public DetailedMessageAdapter( Context context, int resource, List<DetailedFriendlyMessage> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.detailed_message_view, parent, false);
        }
        TextView subjectTextView = (TextView) convertView.findViewById(R.id.subjectMessageTextView_detailed_message_view);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.main_question_detailed_message_view);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView_detailed_message_view);

        DetailedFriendlyMessage message = getItem(position);

        assert message != null;
        boolean isPhoto = message.getPhotoUrl() != null;
        if (isPhoto)
        {
        //    messageTextView.setVisibility(View.GONE);
         //   photoImageView.setVisibility(View.VISIBLE);
        //    Glide.with(photoImageView.getContext())
        //            .load(message.getPhotoUrl())
         //           .into(photoImageView);
        }
        else
        {
            messageTextView.setVisibility(View.VISIBLE);
        //    photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
        }
        authorTextView.setText(message.getName());
        subjectTextView.setText(message.getSubject());

        return convertView;
    }
}
