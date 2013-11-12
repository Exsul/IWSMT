package net.exsul.IWSMT;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

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
        ShowNext();
    }

    List<ParsedObject> elements = new ArrayList<ParsedObject>();

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
        ImageView view = (ImageView)findViewById(R.id.content);
        view.setImageBitmap(obj.Image(getApplicationContext()));

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(obj.obj.title);

        TextView votes = (TextView)findViewById(R.id.votes);
        String vote =
                "AWS: " + obj.obj.votes_count[0] +
                " WTW: " + obj.obj.votes_count[1] +
                " BOR: " + obj.obj.votes_count[2] +
                " COM: " + obj.obj.comments_count;
        votes.setText(vote);
    }

    private void ShowNext() {
        ShowContent(GetNext());
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        ShowNext();
        return super.dispatchTouchEvent(ev);
    }
}
