package com.fungsiutama;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;

import com.daimajia.androidanimations.library.Techniques;
import com.e_sppd.rssm.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import bantuan_tutorial.TutorialNew;

public class Splash_Screen extends AwesomeSplash {
	boolean doubleBackToExitPressedOnce = false;
	@Override
	public void initSplash(ConfigSplash configSplash) {

		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//menambahkan background
		configSplash.setBackgroundColor(R.color.list_item_title);
		configSplash.setAnimCircularRevealDuration(4000);
		configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);
		configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

		//menambahkan logo
		configSplash.setLogoSplash(R.drawable.simrsxx);
		configSplash.setAnimLogoSplashDuration(2000);
		configSplash.setAnimLogoSplashTechnique(Techniques.FadeInUp);

		//menambahkan title
		configSplash.setTitleSplash("[ e-SPPD ]");

		configSplash.setTitleTextColor(R.color.white);
		configSplash.setTitleFont("fonts/Poppins_ExtraBold.ttf");
		configSplash.setTitleTextSize(55f);
		configSplash.setAnimTitleDuration(5000);
		configSplash.setAnimTitleTechnique(Techniques.Landing);

		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {

				if (doubleBackToExitPressedOnce) {
					setEnabled(false);   // penting agar tidak loop
					getOnBackPressedDispatcher().onBackPressed();
					return;
				}

				doubleBackToExitPressedOnce = true;
				Toast.makeText(Splash_Screen.this,
						"Tekan tombol kembali [2x] untuk keluar aplikasi.",
						Toast.LENGTH_SHORT).show();

				new Handler(Looper.getMainLooper()).postDelayed(
						() -> doubleBackToExitPressedOnce = false,
						2000
				);
			}
		});
	}

	public void animationsFinished(){
		finish();
		startActivity(new Intent(Splash_Screen.this, TutorialNew.class));
	}
}
