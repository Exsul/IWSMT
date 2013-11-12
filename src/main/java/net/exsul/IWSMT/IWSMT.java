package net.exsul.IWSMT;

import android.app.Application;
import com.testflightapp.lib.TestFlight;

public class IWSMT extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //TestFlight.takeOff(this, "01cff6b9-f72c-4dc4-a25e-000d5a15fcea");
    }
}
