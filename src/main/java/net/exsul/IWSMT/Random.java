package net.exsul.IWSMT;

import android.app.Activity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Random extends Activity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    List<ParsedObject> elements;

    Random() {
        super();
        elements = new ArrayList<ParsedObject>();
    }

    private void LoadNext() {
        ContentLoader cl = new ContentLoader();
        elements.addAll(cl.LoadRandom());
    }

    private PreparedObject GetNext() {
        if (elements.size() == 0)
            LoadNext();
        assert(elements.size() != 0);
        ParsedObject obj = elements.get(0);
        elements.remove(0);
        return new PreparedObject(obj);
    }

    private void ShowContent( PreparedObject obj ) {

    }

    private void ShowNext() {
        ShowContent(GetNext());
    }
}
