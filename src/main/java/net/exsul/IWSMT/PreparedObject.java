package net.exsul.IWSMT;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PreparedObject {
    final ParsedObject obj;
    Bitmap bitmap;
    PreparedObject( final ParsedObject _obj ) {
        obj = _obj;

    }

    private Bitmap DownloadImage() {
        URL url = new URL(obj.img);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        return BitmapFactory.decodeStream(is);
    }

    public Bitmap Image() {
        if (bitmap == null)
            bitmap = DownloadImage();
        return bitmap;
    }
}
