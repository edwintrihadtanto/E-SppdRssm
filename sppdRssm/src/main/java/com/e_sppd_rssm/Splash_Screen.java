package com.e_sppd_rssm;

import android.content.Intent;

import com.daimajia.androidanimations.library.Techniques;
import com.e_sppd.rssm.R;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import bantuan_tutorial.Tutorial;

public class Splash_Screen extends AwesomeSplash {
	@Override
	public void initSplash(ConfigSplash configSplash) {

		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

		//menambahkan background
		configSplash.setBackgroundColor(R.color.list_item_title);
		configSplash.setAnimCircularRevealDuration(4000);
		configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
		configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM);

		//menambahkan logo
		configSplash.setLogoSplash(R.drawable.simrs);
		configSplash.setAnimLogoSplashDuration(2000);
		configSplash.setAnimLogoSplashTechnique(Techniques.FadeInUp);

		//menambahkan title
		configSplash.setTitleSplash("- E-SPPD -");

		configSplash.setTitleTextColor(R.color.hitam);
		configSplash.setTitleFont("fonts/FTY_STRATEGYCIDE_NCV.ttf");
		configSplash.setTitleTextSize(55f);
		configSplash.setAnimTitleDuration(5000);
		configSplash.setAnimTitleTechnique(Techniques.Landing);

	}

	public void animationsFinished(){
		finish();
		startActivity(new Intent(Splash_Screen.this, Tutorial.class));
	}

	public void onBackPressed() {
		Splash_Screen.this.finish();
		finish();
	}
}
