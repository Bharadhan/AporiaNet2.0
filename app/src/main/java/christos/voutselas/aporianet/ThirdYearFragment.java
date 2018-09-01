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

public class ThirdYearFragment extends Fragment
{
    public String lessonNameSelected = "";
    public String courseDirection = "";
    public String yearClass = "";

    public ThirdYearFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.word_list, container, false);

        // Create a list of words
        final ArrayList<Word> words = new ArrayList<Word>();
        words.add(new Word(R.string.thirdYearThetikonAndYgeias, R.string.ekuesi, R.drawable.ekthesi, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearThetikonAndYgeias, R.string.fysiki, R.drawable.fusiki, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearThetikonAndYgeias, R.string.xhmeia, R.drawable.xhmeia, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearThetikon, R.string.mathimatika, R.drawable.math_prosavatolismou, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearYgeias, R.string.biologia, R.drawable.biologia, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.ekuesi, R.drawable.ekthesi_oikonomias, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.mathimatika, R.drawable.mathimatika_oikonomias, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.aoth, R.drawable.aoth, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearOikonomias, R.string.ae, R.drawable.ae, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.ekuesi, R.drawable.ekthesi_anthropistikon, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.arxaia_gnosto, R.drawable.arxaia_gnosto, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.arxaia_agnosto, R.drawable.arxaia_agnosto, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.istoria, R.drawable.istoria, R.string.category_third_year));
        words.add(new Word(R.string.thirdYearAnthropistikon, R.string.latinika, R.drawable.latinika, R.string.category_third_year));

        // Create an {@link WordAdapter}, whose data source is a list of {@link Word}s. The
        // adapter knows how to create list items for each item in the list.
        WordAdapter adapter = new WordAdapter(getActivity(), words);

        // Find the {@link ListView} object in the view hierarchy of the {@link Activity}.
        // There should be a {@link ListView} with the view ID called list, which is declared in the
        // word_list.xml layout file.
        ListView listView = (ListView) rootView.findViewById(R.id.list);

        // Make the {@link ListView} use the {@link WordAdapter} we created above, so that the
        // {@link ListView} will display list items for each {@link Word} in the list.
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                // Get the {@link Word} object at the given position the user clicked on
                Word word = words.get(position);
                lessonNameSelected =  String.valueOf(word.getLessonNameId());
                courseDirection = String.valueOf(word.getLessonDirectionId());
                yearClass = String.valueOf(word.getYearClassId());

                Intent intent = new Intent(getActivity(), FirstYearForumView.class);
                intent.putExtra("lessonName", lessonNameSelected);
                intent.putExtra("courseDirection", courseDirection);
                intent.putExtra("yearClass", yearClass);
                startActivity(intent);
            }
        });

        return rootView;
    }
}
