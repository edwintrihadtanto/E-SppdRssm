package com.e_sppd_rssm;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.e_sppd.rssm.R;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class Tampilan_Utama extends Fragment {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN= {
            R.drawable.satu_gambar,
            R.drawable.dua_gambar,
            R.drawable.tiga_gambar,
            R.drawable.empat_gambar,
            R.drawable.lima_gambar};

    private final ArrayList<Integer> XMENArray = new ArrayList<>();

    public Tampilan_Utama(){}
    LinearLayout view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {

        view = (LinearLayout) inflater.inflate(R.layout.galery, container, false);
        init();
        return view;
    }
    private void init() {
        XMENArray.addAll(Arrays.asList(XMEN));

        mPager = view.findViewById(R.id.pagergalery);
        mPager.setAdapter(new MyAdapter_Slide(getActivity(),XMENArray));
        CircleIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == XMEN.length) {
                currentPage = 0;
            }
            mPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 99999, 99999);
    }

}