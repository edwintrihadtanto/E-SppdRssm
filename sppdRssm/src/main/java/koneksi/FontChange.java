package koneksi;

import android.annotation.SuppressLint;
import android.app.Application;

import com.e_sppd.rssm.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

@SuppressLint("Registered")
public class FontChange extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/FTY_STRATEGYCIDE_NCV.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
