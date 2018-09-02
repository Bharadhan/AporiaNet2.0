package christos.voutselas.aporianet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class RoboticFragment extends Fragment
{
    public String lessonNameSelected = "";
    public String courseDirection = "";
    public String yearClass = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.string.firstYearDirection, R.string.roboBaby, R.drawable.robotbaby, R.string.category_robotic));
        words.add(new Word(R.string.firstYearDirection, R.string.roboKid, R.drawable.robokid, R.string.category_robotic));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        final ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l)
            {

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);
                lessonNameSelected =  String.valueOf(word.getLessonNameId());
                courseDirection = String.valueOf(word.getLessonDirectionId());
                yearClass = String.valueOf(word.getYearClassId());

                Intent intent = new Intent(getActivity(), RoboticForumView.class);
                intent.putExtra("lessonName", lessonNameSelected);
                intent.putExtra("courseDirection", courseDirection);
                intent.putExtra("yearClass", yearClass);
                intent.putExtra("back", "No");
                startActivity(intent);
            }
        });

        return rootView;
    }
}
