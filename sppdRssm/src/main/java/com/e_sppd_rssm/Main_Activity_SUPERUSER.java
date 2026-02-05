package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
@SuppressLint("NewApi")
public class Main_Activity_SUPERUSER extends AppCompatActivity implements
		OnClickListener {
	private Handler handler = new Handler();

	public Button btn_refresh_utama, btn_dataspt, btn_datasppd, btn_datauser,
			btn_datapegawai, btn_log_out;
	public LinearLayout layerbawah_slmtdatang;
	public TextView ambil_nip, tgl, jam, cek_versi_apk, nmsuperuser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_utama_superuser);
		handler.postDelayed(runnable, 1000);

		btn_refresh_utama = (Button) findViewById(R.id.btn_refresh_utama);
		btn_dataspt = (Button) findViewById(R.id.btn_dataspt);
		btn_datasppd = (Button) findViewById(R.id.btn_datasppd);
		btn_datapegawai = (Button) findViewById(R.id.btn_datapegawai);

		btn_datauser = (Button) findViewById(R.id.btn_datauser);

		btn_log_out = (Button) findViewById(R.id.btn_log_out);

		cek_versi_apk = (TextView) findViewById(R.id.cek_versi_apk);
		nmsuperuser = (TextView) findViewById(R.id.nmsuperuser);
		ambil_nip = (TextView) findViewById(R.id.ambil_nip);
		tgl = (TextView) findViewById(R.id.tgl);
		jam = (TextView) findViewById(R.id.jam);
		layerbawah_slmtdatang = (LinearLayout) findViewById(R.id.layerbawah_slmtdatang);

		// Bundle b = getIntent().getExtras();
		// String transfer_nip = b.getString("transfer_nip");

		Intent intent = getIntent();
		String nip_pegawai = intent.getStringExtra(Login_Activity.nippegawai);
		//String versi = intent.getStringExtra(Login_Activity.versi);
		//cek_versi_apk.setText(versi);
		nmsuperuser.setText("SUPER USER");
		String Text_layerbawah_slmtdatang = "Selamat Datang di, Aplikasi E-SPPD RSSM V.";
				//+ versi;
		setticker(layerbawah_slmtdatang, Text_layerbawah_slmtdatang, this);

		// Session Login
		if (nip_pegawai == null) {
			Bundle b = getIntent().getExtras();
			String transfer_nip = b.getString("transfer_nip");
			ambil_nip.setText(transfer_nip);
		} else if (nip_pegawai != null) {
			ambil_nip.setText(nip_pegawai);
		}

		btn_dataspt.setOnClickListener(this);
		btn_datauser.setOnClickListener(this);
		btn_datapegawai.setOnClickListener(this);
		btn_log_out.setOnClickListener(this);
		btn_datasppd.setOnClickListener(this);
	}

	@Override
	public void onClick(View view_data) {
		// TODO Auto-generated method stub
		switch (view_data.getId()) {
		case R.id.btn_dataspt:

			String nip2 = ambil_nip.getText().toString();
			Intent i = null;

			i = new Intent(Main_Activity_SUPERUSER.this,
					Daftar_Laporan_Per_Petugas.class);

			Bundle Bundle = new Bundle();
			Bundle.putString("transfer_nip", nip2);

			i.putExtras(Bundle);
			startActivity(i);
			break;
		case R.id.btn_datasppd:
			String nip3 = ambil_nip.getText().toString();
			String versi = cek_versi_apk.getText().toString();
			Intent in = null;

			in = new Intent(Main_Activity_SUPERUSER.this,
					Tentang_Aplikasi.class);

			Bundle bun = new Bundle();
			bun.putString("transfer_nip", nip3);
			bun.putString("versi", versi);
			in.putExtras(bun);
			startActivity(in);
			break;
		case R.id.btn_datapegawai:
			String nip4 = ambil_nip.getText().toString();
			Intent Intent1 = null;

			Intent1 = new Intent(Main_Activity_SUPERUSER.this, Profil.class);

			Bundle bund = new Bundle();
			bund.putString("transfer_nip", nip4);

			Intent1.putExtras(bund);
			startActivity(Intent1);
			break;
		case R.id.btn_datauser:
			String nip5 = ambil_nip.getText().toString();
			Intent Intent2 = null;

			Intent2 = new Intent(Main_Activity_SUPERUSER.this, History.class);

			Bundle bundl = new Bundle();
			bundl.putString("transfer_nip", nip5);

			Intent2.putExtras(bundl);
			startActivity(Intent2);
			break;
		case R.id.btn_log_out:
			infodialogback();
			break;
		default:
			break;
		}
	}

	public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}

	@Override
	public void onBackPressed() {
		infodialogback();
	}

	private void infodialogback() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("LogOut e-SPPD ?");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Main_Activity_SUPERUSER.this.finish();
				finish();
			}
		});
		ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ad.show();
	}

	// ANIMASI TEKS BERJALAN
	// ///--------------------------------------------------------
	public void setticker(LinearLayout parent_layout, String text,
			Context menu_Utama) {
		if (text != "") {

			TextView view = new TextView(menu_Utama);
			view.setText(text);

			view.setTextColor(Color.GREEN);
			view.setTextSize(25.5F);
			Context context = view.getContext();

			view.measure(View.MeasureSpec.UNSPECIFIED,
					View.MeasureSpec.UNSPECIFIED);

			float width = view.getMeasuredWidth();
			float height = view.getMeasuredHeight();

			@SuppressWarnings("deprecation")
			float screenWidth = ((AppCompatActivity) context).getWindowManager()
					.getDefaultDisplay().getWidth();

			view.setLayoutParams(new LinearLayout.LayoutParams((int) width,
					(int) height, 1f));

			System.out.println("width and screenwidth are" + width + "/"
					+ screenWidth + "///" + view.getMeasuredWidth());

			float toXDelta = width - (screenWidth - 0);

			if (toXDelta < 0) {
				toXDelta = 0 - screenWidth;// -300;
			} else {
				toXDelta = 0 - screenWidth - toXDelta;// -300 - toXDelta;
			}

			Animation mAnimation = new TranslateAnimation(screenWidth,
					toXDelta, 0, 0);
			mAnimation.setDuration(15000);
			mAnimation.setRepeatMode(Animation.RESTART);
			mAnimation.setRepeatCount(Animation.INFINITE);
			view.setAnimation(mAnimation);
			parent_layout.addView(view);
		}
	}

	private Runnable runnable = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();

			SimpleDateFormat tgl_skrng = new SimpleDateFormat("d/MM/yyyy");
			SimpleDateFormat jam_skrng = new SimpleDateFormat("hh:mm:ss aa");
			// SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
			String strdate_tgl = tgl_skrng.format(c1.getTime());
			String strdate_jam = jam_skrng.format(c1.getTime());

			tgl.setText(strdate_tgl);
			jam.setText(strdate_jam);

			handler.postDelayed(this, 1000);
		}

	};

}
