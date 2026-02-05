package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import koneksi.Daftar_String;
import koneksi.Koneksi;
@SuppressLint("NewApi")
public class Main_Activity_Petugas extends AppCompatActivity implements OnClickListener {
	private Handler handler = new Handler();
	private static final String TAG = "Main_Activity_Petugas";
	public Button btn_refresh_utama, btn_recentactivity,
			btn_tentang_apk_petugas, btn_profil, btn_log_out, btn_history;
	public LinearLayout layerbawah_slmtdatang;
	public TextView ambil_nip, tgl, jam, cek_versi_apk;
	private ProgressDialog ProgressDialog;
	private ListView listView;
	private Koneksi Koneksi_Server;
	private List<Daftar_String> list;
	private List_Nama_Pegawai adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu_utama_petugas);
		handler.postDelayed(runnable, 1000);

		Koneksi_Server = new Koneksi();
		listView = (ListView) findViewById(R.id.listtampil_namapeg);

		btn_refresh_utama 		= (Button) findViewById(R.id.btn_refresh_utama);
		btn_recentactivity 		= (Button) findViewById(R.id.btn_recentactivity);
		btn_tentang_apk_petugas = (Button) findViewById(R.id.btn_tentang_apk_petugas);
		btn_profil 				= (Button) findViewById(R.id.btn_profil);
		btn_log_out 			= (Button) findViewById(R.id.btn_log_out);
		btn_history 			= (Button) findViewById(R.id.btn_history);
		cek_versi_apk 			= (TextView) findViewById(R.id.cek_versi_apk);
		ambil_nip 				= (TextView) findViewById(R.id.ambil_nip);
		tgl 					= (TextView) findViewById(R.id.tgl);
		jam 					= (TextView) findViewById(R.id.jam);
		layerbawah_slmtdatang 	= (LinearLayout) findViewById(R.id.layerbawah_slmtdatang);

		// Bundle b = getIntent().getExtras();
		// String transfer_nip = b.getString("transfer_nip");

		Intent intent = getIntent();
		String nip_pegawai = intent.getStringExtra(Login_Activity.nippegawai);
		//String versi = intent.getStringExtra(Login_Activity.versi);
		//cek_versi_apk.setText(versi);

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

		list = new ArrayList<Daftar_String>();
		new MainActivityAsync().execute();

		btn_recentactivity.setOnClickListener(this);
		btn_tentang_apk_petugas.setOnClickListener(this);
		btn_profil.setOnClickListener(this);
		btn_log_out.setOnClickListener(this);
		btn_history.setOnClickListener(this);
	}
	
	private void showAlert(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				
				.setPositiveButton("Coba Lagi",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								finish();
								startActivity(getIntent());
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	@Override
	public void onClick(View view_data) {
		// TODO Auto-generated method stub
		switch (view_data.getId()) {
		case R.id.btn_recentactivity:

			String nip2 = ambil_nip.getText().toString();
			Intent i = null;

			i = new Intent(Main_Activity_Petugas.this,
					Daftar_Laporan_Per_Petugas.class);

			Bundle Bundle = new Bundle();
			Bundle.putString("transfer_nip", nip2);

			i.putExtras(Bundle);
			startActivity(i);
			break;
		case R.id.btn_tentang_apk_petugas:
			String nip3 = ambil_nip.getText().toString();
			String versi = cek_versi_apk.getText().toString();
			Intent in = null;

			in = new Intent(Main_Activity_Petugas.this, Tentang_Aplikasi.class);

			Bundle bun = new Bundle();
			bun.putString("transfer_nip", nip3);
			bun.putString("versi", versi);
			in.putExtras(bun);
			startActivity(in);
			break;
		case R.id.btn_profil:
			String nip4 = ambil_nip.getText().toString();
			Intent Intent1 = null;

			Intent1 = new Intent(Main_Activity_Petugas.this, Profil.class);

			Bundle bund = new Bundle();
			bund.putString("transfer_nip", nip4);

			Intent1.putExtras(bund);
			startActivity(Intent1);
			break;
		case R.id.btn_history:
			String nip5 = ambil_nip.getText().toString();
			Intent Intent2 = null;

			Intent2 = new Intent(Main_Activity_Petugas.this, History.class);

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

	private class MainActivityAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			ProgressDialog = new ProgressDialog(Main_Activity_Petugas.this);
			ProgressDialog.setMessage("Loading...");
			ProgressDialog.setIndeterminate(false);
			ProgressDialog.setCancelable(false);
			ProgressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			/** Mengirimkan request ke server dan memproses JSON response */
			String nip_pegawai = ambil_nip.getText().toString().trim();

			String Cek = String.valueOf(nip_pegawai);
			String url;
			String responseString = null; 	
			// Super PENTINGGGGGGGGGGGGGGGGGGGGGG PUOOLLLLLLLLLL
			// Barokallah Alhamdulilah KETEMU CARANE MENGATASI
			// DATA NIP YANG BERSIFAT VARCHAR DENGAN SPASI BANYAK

			try {

				url = Koneksi_Server.sendGetRequest(Koneksi.profil_pegawai
						+ "?nip_pegawai=" + URLEncoder.encode(Cek, "UTF-8"));

				list = proses_pengambilan_data(url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				responseString = e.toString();
			} 	catch (Exception e) {
				responseString = e.toString();
			}

			return responseString;	
		}


		@Override
		protected void onPostExecute(final String result) {
			ProgressDialog.dismiss();
			Log.e(TAG, "Respon Dari Server Petugas :::> " + result);
			final String pesan = "java.lang.NullPointerException";
			final String pesan2 = "null";
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (result != null) {						
						if (result.contains(pesan)) {
							showAlert("Pastikan Koneksi Internet Anda Stabil");
						}else if(result.contains(pesan2))  {
							showAlert("Gagal Sinkronisasi Data !!!");
						}else{
							menampilkan_nama_pegawai();
						}
					}else{
						menampilkan_nama_pegawai();
						
					}					
				}
			});
			super.onPostExecute(result);	
		}

	}

	@SuppressWarnings("unused")
	private List<Daftar_String> proses_pengambilan_data(String response) {
		List<Daftar_String> list_Daftar_String = new ArrayList<Daftar_String>();
		String responseString = null; 	
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampil_data");
			Log.d(TAG, "data lengt: " + jsonArray.length());
			Daftar_String mhs = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				mhs = new Daftar_String();
				mhs.setnip(obj.getString("nip"));
				mhs.setnama_pegawai(obj.getString("nama_pegawai"));
				mhs.setjabatan(obj.getString("jabatan"));
				mhs.setgolongan(obj.getString("golongan"));

				list_Daftar_String.add(mhs);
			}
		} catch (JSONException e) {
			responseString = e.toString();			
		}catch (Exception e) {
			responseString = e.toString();
		}
		return list_Daftar_String;
	}

	private void menampilkan_nama_pegawai() {
		if (!terkoneksi(Main_Activity_Petugas.this)) {
			String a = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			showAlert(a);
			return;		
			
		}else {
			adapter = new List_Nama_Pegawai(getApplicationContext(), list);
			listView.setAdapter(adapter);
		}
	}

	private boolean terkoneksi(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			Toast.makeText(
					getApplication(),
					"Koneksi Internet Anda : " + netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
			return true;
		
		} else if (netInfo != null && netInfo.isRoaming()) {
			Toast.makeText(
					getApplication(),
					"Jaringan Internet Roaming" + netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
			return true;

		} else if (netInfo != null && netInfo.isAvailable()) {
			Toast.makeText(
					getApplication(),
					"Masih Mercari Jaringan" + netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
		}
		return false;

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
				Main_Activity_Petugas.this.finish();
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
			Context context = view.getContext(); // gets the context of the view

			// measures the unconstrained size of the view
			// before it is drawn in the layout
			view.measure(View.MeasureSpec.UNSPECIFIED,
					View.MeasureSpec.UNSPECIFIED);

			// takes the unconstrained width of the view
			float width = view.getMeasuredWidth();
			float height = view.getMeasuredHeight();

			// gets the screen width
			@SuppressWarnings("deprecation")
			float screenWidth = ((AppCompatActivity) context).getWindowManager()
					.getDefaultDisplay().getWidth();

			view.setLayoutParams(new LinearLayout.LayoutParams((int) width,
					(int) height, 1f));

			System.out.println("width and screenwidth are" + width + "/"
					+ screenWidth + "///" + view.getMeasuredWidth());

			// performs the calculation
			float toXDelta = width - (screenWidth - 0);

			// sets toXDelta to -300 if the text width is smaller that the
			// screen size
			if (toXDelta < 0) {
				toXDelta = 0 - screenWidth;// -300;
			} else {
				toXDelta = 0 - screenWidth - toXDelta;// -300 - toXDelta;
			}
			// Animation parameters
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
			SimpleDateFormat jam_skrng = new SimpleDateFormat("H:m:s a");
			// SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
			String strdate_tgl = tgl_skrng.format(c1.getTime());
			String strdate_jam = jam_skrng.format(c1.getTime());

			tgl.setText(strdate_tgl);
			jam.setText(strdate_jam);

			handler.postDelayed(this, 1000);
		}

	};

}
