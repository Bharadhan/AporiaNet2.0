package christos.voutselas.aporianet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DetailedMessageAdapter extends ArrayAdapter<DetailedFriendlyMessage>
{
    private String color = "";

    public DetailedMessageAdapter( Context context, int resource, List<DetailedFriendlyMessage> objects)
    {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.question_message_view, parent, false);
        }
        TextView subjectTextView = (TextView) convertView.findViewById(R.id.subjectMessageTextView_detailed_message_view);
        TextView messageTextView = (TextView) convertView.findViewById(R.id.main_question_detailed_message_view);
        TextView authorTextView = (TextView) convertView.findViewById(R.id.nameTextView_detailed_message_view);

        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) messageTextView.getLayoutParams();

        DetailedFriendlyMessage message = getItem(position);
        color = message.getColor();

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
            switch(color)
            {

                case "grey" :

                    messageTextView.setVisibility(View.VISIBLE);
                    messageTextView.setText(message.getText());
                    authorTextView.setText(message.getName());
                    subjectTextView.setText(message.getSubject());
                    messageTextView.setBackgroundResource(R.drawable.question_border);
                    messageTextView.setTextColor(Color.parseColor("#000000"));
                    lp.setMargins(0,0,300,0);
                    // Apply the updated layout parameters to TextView
                    messageTextView.setLayoutParams(lp);
                    authorTextView.setGravity(Gravity.LEFT);
                    break;

                case "blue" :

                    messageTextView.setVisibility(View.VISIBLE);
                    messageTextView.setText(message.getText());
                    authorTextView.setText(message.getName());
                    subjectTextView.setText(message.getSubject());
                    messageTextView.setBackgroundResource(R.drawable.answer_boarder);
                    messageTextView.setTextColor(Color.parseColor("#ffffff"));
                    lp.setMargins(300,0,0,0);
                    // Apply the updated layout parameters to TextView
                    messageTextView.setLayoutParams(lp);
                    authorTextView.setGravity(Gravity.RIGHT);
                    break;
            }
        }
        return convertView;
    }
}
