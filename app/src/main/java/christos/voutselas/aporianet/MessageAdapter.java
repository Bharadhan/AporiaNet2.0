package christos.voutselas.aporianet;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class MessageAdapter extends ArrayAdapter<FriendlyMessage>
{
    public static Integer pos;

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

        TextView messageTextView = (TextView) convertView.findViewById(R.id.subjectMessageTextView);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView);

        FriendlyMessage message1 = getItem(position);

        assert message1 != null;
        boolean isPhoto = message1.getPhotoUrl() != null;
        if (isPhoto)
        {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(message1.getSubject());
        }
        else
        {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(message1.getSubject());

        }
        authorTextView.setText(message1.getName() + " " + message1.getDate());

        return convertView;
    }

    @Override
    public FriendlyMessage getItem(int position) {
        pos = (super.getCount());
        return super.getItem(super.getCount() - position - 1);
    }
}