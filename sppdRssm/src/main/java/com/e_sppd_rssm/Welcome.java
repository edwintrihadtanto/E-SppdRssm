package com.e_sppd_rssm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ViewFlipper;

import com.e_sppd.rssm.R;

import androidx.fragment.app.Fragment;

public class Welcome extends Fragment {
    RelativeLayout view;

    private ViewFlipper v_flipper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = (RelativeLayout) inflater.inflate(R.layout.welcome, container, false);
        int[] images = {
                R.drawable.simrsxx,
                R.drawable.logorssm,
                R.drawable.simrsxx,
                R.drawable.logorssm
        };
        v_flipper = view.findViewById(R.id.v_flipper);

        for (int value : images) {
            fliverImages(value);
        }
        for (int image: images)
            fliverImages(image);
        return view;
    }

    private void  fliverImages(int images){
        ImageView imageView = new ImageView(getActivity());
        imageView.setBackgroundResource(images);

        v_flipper.addView(imageView);
        v_flipper.setFlipInterval(4000);
        v_flipper.setAutoStart(true);

        v_flipper.setInAnimation(getContext(),android.R.anim.slide_in_left);
        v_flipper.setOutAnimation(getContext(),android.R.anim.slide_out_right);
    }

}
