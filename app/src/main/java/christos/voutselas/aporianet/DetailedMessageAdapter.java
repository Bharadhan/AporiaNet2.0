package christos.voutselas.aporianet;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class DetailedMessageAdapter extends ArrayAdapter<DetailedFriendlyMessage>
{

    private boolean i = true;

    public DetailedMessageAdapter( Context context, int resource, List<DetailedFriendlyMessage> objects) {
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
        TextView messageAnswerTextView = (TextView) convertView.findViewById(R.id.answer_massage);
        TextView authorAnswerTextView = (TextView) convertView.findViewById(R.id.answerNameQuestion);

        DetailedFriendlyMessage message = getItem(position);
        String checkAnswer = String.valueOf(message.getUserAnswer());

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
        else if ((message.getUserAnswer()).equals(""))
        {
            messageTextView.setVisibility(View.VISIBLE);
        //    photoImageView.setVisibility(View.GONE);
            messageTextView.setText(message.getText());
            authorTextView.setText(message.getName());
            subjectTextView.setText(message.getSubject());







        }
        else
        {
            messageAnswerTextView.setVisibility(View.VISIBLE);
            authorAnswerTextView.setVisibility(View.VISIBLE);
            messageTextView.setVisibility(View.GONE);
            authorTextView.setVisibility(View.GONE);
            messageAnswerTextView.setText(message.getUserAnswer());
            authorAnswerTextView.setText(message.getAnswername());
        }


        return convertView;
    }
}
