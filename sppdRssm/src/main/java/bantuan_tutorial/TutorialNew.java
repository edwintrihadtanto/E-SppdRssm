package bantuan_tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.e_sppd.rssm.R;
import com.fungsiutama.Login_Activity;

import config_swipe.PrefManager;

public class TutorialNew extends AppCompatActivity {
    private ViewPager2 viewPager;
    private LinearLayout dotsLayout;
    TextView[] dots;

    private final int[] layouts = {
            R.layout.slide1,
            R.layout.slide2,
            R.layout.slide3,
            R.layout.slide4
    };

    private PrefManager prefManager;

    private ImageView btn_panah, btn_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);

        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.view_pager);
        dotsLayout = findViewById(R.id.layoutDots);

        btn_panah = findViewById(R.id.btn_panah);
        btn_ok = findViewById(R.id.btn_ok);

        TutorialAdapter adapter = new TutorialAdapter(layouts);
        viewPager.setAdapter(adapter);

        addBottomDots(0);

        viewPager.registerOnPageChangeCallback(pageChangeCallback);

        btn_panah.setOnClickListener(v -> {
            int current = viewPager.getCurrentItem() + 1;

            if (current < layouts.length) {
                viewPager.setCurrentItem(current);
            } else {
                launchHomeScreen();
            }
        });

        btn_ok.setOnClickListener(v -> launchHomeScreen());
    }

    private void addBottomDots(int position) {

        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();

        for (int i = 0; i < layouts.length; i++) {

            dots[i] = new TextView(this);
            dots[i].setText("•");
            dots[i].setTextSize(35);

            if (i == position) {
                dots[i].setTextColor(ContextCompat.getColor(this, R.color.dot_light_screen1));
            } else {
                dots[i].setTextColor(ContextCompat.getColor(this, R.color.dot_light_screen2));
            }

            dotsLayout.addView(dots[i]);
        }
    }

    private void launchHomeScreen() {

        prefManager.setFirstTimeLaunch(false);

        startActivity(new Intent(TutorialNew.this, Login_Activity.class));
        finish();
    }

    private final ViewPager2.OnPageChangeCallback pageChangeCallback =
            new ViewPager2.OnPageChangeCallback() {

                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);

                    addBottomDots(position);

                    if (position == layouts.length - 1) {
                        btn_panah.setVisibility(View.GONE);
                        btn_ok.setVisibility(View.VISIBLE);
                    } else {
                        btn_panah.setVisibility(View.VISIBLE);
                        btn_ok.setVisibility(View.GONE);
                    }
                }
            };
}
