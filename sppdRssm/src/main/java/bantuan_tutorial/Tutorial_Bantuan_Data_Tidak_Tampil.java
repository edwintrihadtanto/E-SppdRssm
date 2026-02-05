package bantuan_tutorial;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.e_sppd.rssm.R;
import com.e_sppd_rssm.Login_Activity;

import config_swipe.PrefManager;


public class Tutorial_Bantuan_Data_Tidak_Tampil extends AppCompatActivity {


	private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;
    private ImageView btn_panah, btn_ok;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
      
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
 
        setContentView(R.layout.activity_welcome);
 
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (Button) findViewById(R.id.btn_skip);
        btnNext = (Button) findViewById(R.id.btn_next);
 
        btn_panah 	= (ImageView) findViewById(R.id.btn_panah);
        btn_ok 		= (ImageView) findViewById(R.id.btn_ok);
 
        // layouts of all welcome sliders
        // add few more layouts if you want
        Bundle b = getIntent().getExtras();
		String pesan = b.getString("pesan");
		String text1 = "Data Tidak Bisa Tampil";
		String text2 = "Tidak Bisa Posting";
		String text3 = "Cara-Cara Posting";
		String text4 = "Menampilkan Menu Pilihan";
		String text5 = "Cara Regristrasi";
		String text6 = "Cara Ganti Password";
		String text7 = "Lupa Password";
		String text8 = "Edit Laporan";
		if (pesan.contains(text1)){
			 layouts = new int[]{
		                R.layout.slidenomor1,
		                R.layout.slidenomor1_1,
		                R.layout.slidenomor1_1_1};
		}else if (pesan.contains(text2)){
			 layouts = new int[]{
		                R.layout.slidenomor2};
		}else if (pesan.contains(text3)){
			 layouts = new int[]{
	                R.layout.slidenomor3};
		}else if (pesan.contains(text4)){
			 layouts = new int[]{
	                R.layout.slidenomor4};
		}else if (pesan.contains(text5)){
			 layouts = new int[]{
		                R.layout.slidenomor5};
		}else if (pesan.contains(text6)){
			 layouts = new int[]{
		                R.layout.slidenomor6};
		}else if (pesan.contains(text7)){
			 layouts = new int[]{
		                R.layout.slidenomor7};
		}				
       
 
        // adding bottom dots
        addBottomDots(0);
 
        // making notification bar transparent
       // changeStatusBarColor(); //INI MEMPENGARUHI VERSI TERNYATA
 
        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
 
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  launchHomeScreen();
            	finish();
            
            }
        });
 
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                  // launchHomeScreen();
                	finish();
                	
                }
            }
        });
        
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  launchHomeScreen();
            	finish();
            
            }
        });
        btn_panah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                if (current < layouts.length) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                } else {
                  // launchHomeScreen();
                	finish();
                	
                }
            }
        });
    }
 
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
 
        int[] colorsActive = getResources().getIntArray(R.array.array_dot_active);
        int[] colorsInactive = getResources().getIntArray(R.array.array_dot_inactive);
 
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorsInactive[currentPage]);
            dotsLayout.addView(dots[i]);
        }
 
        if (dots.length > 0)
            dots[currentPage].setTextColor(colorsActive[currentPage]);
    }
 
    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }
 
    private void launchHomeScreen() { // MENAMPILKAN AKTIVITY SETELAH PROSES TUTORIAL SELESAI
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(Tutorial_Bantuan_Data_Tidak_Tampil.this, Login_Activity.class));
        finish();
    }
 
    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
 
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
 
            // changing the next button text 'NEXT' / 'GOT IT'
            if (position == layouts.length - 1) {
                // last page. make button text to GOT IT
              //  btnNext.setText(getString(R.string.start));
               // btnSkip.setVisibility(View.GONE);
            	btn_panah.setVisibility(View.GONE);
            	btn_ok.setVisibility(View.VISIBLE);
            } else {
                // still pages are left
               // btnNext.setText(getString(R.string.next));
               // btnSkip.setVisibility(View.VISIBLE);
            	btn_panah.setVisibility(View.VISIBLE);
            	btn_ok.setVisibility(View.GONE);
            }
        }
 
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
 
        }
 
        @Override
        public void onPageScrollStateChanged(int arg0) {
 
        }
    };
 
    /**
     * Making notification bar transparent
     */
    @SuppressLint("NewApi") 
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
 
    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
 
        public MyViewPagerAdapter() {
        }
 
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
 
            return view;
        }
 
        @Override
        public int getCount() {
            return layouts.length;
        }
 
        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
 
 
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
    
}