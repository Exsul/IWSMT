package net.exsul.IWSMT;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PreparedObject {
    final ParsedObject obj;
    Bitmap bitmap;
    PreparedObject( final ParsedObject _obj ) {
        obj = _obj;

    }

    private Bitmap DownloadImage() throws Exception {
        URL url = new URL(obj.img);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        return BitmapFactory.decodeStream(is);
    }

    public Bitmap Image(final Context a) {
        if (bitmap == null)
            try {
                bitmap = DownloadImage();
            } catch (Exception e) {
                Drawable dr = a.getResources().getDrawable(R.drawable.failed);
                bitmap = ((BitmapDrawable)dr).getBitmap();
            }
        return bitmap;
    }
}
