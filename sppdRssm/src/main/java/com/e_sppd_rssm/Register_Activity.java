package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;

import com.e_sppd.rssm.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import koneksi.Daftar_String;
import koneksi.JSONParser;
import koneksi.Koneksi;

public class Register_Activity extends AppCompatActivity implements OnQueryTextListener, SearchView.OnQueryTextListener {
	private static final String TAG = "Register_Activity";
	private ListView List_View_Pegawai;
	public CheckBox checkbox_pass_reg;
	private Koneksi Koneksi_Server;
	private List<Daftar_String> list;
	private List_Data_Pegawai adapter;
	private Daftar_String selectedList;
	private SearchView pencarian_nip;
	private ProgressDialog tampilan_proses;
	private RelativeLayout rel_list_view_pegawai;
	private SearchManager SearchManager;
	
	private static final String TAG_BERHASIL 	= "sukses";
	private static final String TAG_PESAN 		= "pesan";
	public EditText edit_reg_pass, edit_nip, edit_namapeg, edit_jabatan,
			edit_golongan, edit_pencarian_nip;
	public Button simpan, Button_tampil_listview, btn_refresh;
	static Bundle bundle;
	String bundle_nip, bundle_nama, bundle_jabatan, bundle_golongan;
	public Button ImageView_Help;
	public MotionEvent ev;
	boolean doubleBackToExitPressedOnce = false;
	ConnectivityManager conMgr;
	@SuppressLint({"WrongViewCast", "ResourceAsColor"})
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity);
		conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		{
			if (conMgr.getActiveNetworkInfo() != null
					&& conMgr.getActiveNetworkInfo().isAvailable()
					&& conMgr.getActiveNetworkInfo().isConnected()) {
			} else {
				//String pesan = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
				//show_warning(pesan);

			}
		}
		edit_nip 				= (EditText) findViewById(R.id.edit_nip);
		edit_namapeg 			= (EditText) findViewById(R.id.edit_namapeg);
		edit_jabatan 			= (EditText) findViewById(R.id.edit_jabatan);
		edit_golongan 			= (EditText) findViewById(R.id.edit_golongan);
		edit_reg_pass 			= (EditText) findViewById(R.id.edit_reg_pass);
		rel_list_view_pegawai 	= (RelativeLayout) findViewById(R.id.rel_list_view_pegawai);
		simpan				 	= (Button) findViewById(R.id.btn_simpan_register);
		btn_refresh 			= (Button) findViewById(R.id.btn_refresh);
		ImageView_Help  		= (Button) findViewById(R.id.btn_help_register);
		ImageView_Help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//String pesan = "Sipp Mantap";
			//showAlert(pesan);
				Intent i = null;
				i = new Intent(Register_Activity.this,
						Tampil_Bantuan.class);
				startActivity(i);				
			}
		});
		// ------------------ INI FUNGSI UNTUK SWIPE ---------------------//
		/*
		 * RelativeLayout ln = (RelativeLayout) findViewById(R.id.swipe);
		 * ln.setOnTouchListener(new OnSwipeTouchListener() { public void
		 * onSwipeBottom() { // swipe ke atas
		 * Toast.makeText(Register_Activity.this, "Refresh",
		 * Toast.LENGTH_SHORT).show(); finish(); startActivity(getIntent()); }
		 * public void onSwipeRight() { // swipe layar ke kanan
		 * //onBackPressed(); }
		 * 
		 * });
		 */
		// ------------------ BATAS ---------------------//
		checkbox_pass_reg = (CheckBox) findViewById(R.id.checkbox_pass_reg);

		Koneksi_Server = new Koneksi();
		List_View_Pegawai = (ListView) findViewById(R.id.list_view_pegawai);

		SearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		pencarian_nip = (SearchView) findViewById(R.id.edit_text_cariregister)
				.getParent();
		pencarian_nip.setQueryHint("Pencarian NIP/NPK Pegawai");
		pencarian_nip.setDrawingCacheBackgroundColor(R.color.hitam);
		pencarian_nip.setSearchableInfo(SearchManager
				.getSearchableInfo(getComponentName()));
		pencarian_nip.setOnQueryTextListener(this);


		if (!isOnline(Register_Activity.this)
				&& pencarian_nip.getQuery().toString().length() > 1) {
			Toast.makeText(Register_Activity.this, "Tidak Ada Koneksi",
					Toast.LENGTH_LONG).show();
			return;
		} else {
			pencarian_nip.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					rel_list_view_pegawai.setVisibility(View.VISIBLE);
					return true;
				}

			});

		}
		if (pencarian_nip.getQuery().toString().length() > 8) {
			rel_list_view_pegawai.setVisibility(View.VISIBLE);

		}

		

		list = new ArrayList<Daftar_String>();
		new MainActivityAsync().execute();

		// -----------------------------SCRIPT SHOW / HIDE
		// PASSWORD---------------------------- //

		checkbox_pass_reg
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// checkbox status is changed from uncheck to checked.
						if (!isChecked) {
							// show password
							edit_reg_pass
									.setTransformationMethod(PasswordTransformationMethod
											.getInstance());
							checkbox_pass_reg.setHint("Tampilkan Password");
						} else {
							// hide password
							edit_reg_pass.setTransformationMethod(HideReturnsTransformationMethod
											.getInstance());
							checkbox_pass_reg.setHint("Sembunyikan Password");
						}
					}
				});

	}

	private class MainActivityAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			tampilan_proses = new ProgressDialog(Register_Activity.this);
			tampilan_proses.setMessage("Loading...");
			tampilan_proses.setIndeterminate(false);
			tampilan_proses.setCancelable(false);
			tampilan_proses.show();
		}

		@Override
		protected String doInBackground(String... params) {

			/** Mengirimkan request ke server dan memproses JSON response */
			String responseString = null;
			try {
				String response = Koneksi_Server
						.sendGetRequest(Koneksi.tampil_data_nip);
				list = proses_pengambilan_data(response);
			}catch (Exception e){
				responseString = e.toString();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			tampilan_proses.dismiss();
			Log.e(TAG, "Respon Dari Server ::: " +result+ "Error");
			//menampilkan_data_pegawai();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					menampilkan_data_pegawai();
				}
			});
		}

	}

	private List<Daftar_String> proses_pengambilan_data(String response) {
		//String responseString = null;
		List<Daftar_String> list_Daftar_String = new ArrayList<Daftar_String>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampil_data_nip");
			Log.d(TAG, "data lengt: " + jsonArray.length());
			Daftar_String mhs = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				JSONParser ambil_classJSONParser = new JSONParser();

				mhs = new Daftar_String();
				mhs.setnip(obj.getString("nip"));
				mhs.setnama_pegawai(obj.getString("nama_pegawai"));
				mhs.setjabatan(obj.getString("jabatan"));
				mhs.setgolongan(obj.getString("golongan"));
				mhs.setpass(obj.getString("pass"));

				list_Daftar_String.add(mhs);
			}
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		} catch (Exception e){
			Log.d(TAG, e.getMessage());
		}
		return list_Daftar_String;
	}

	private boolean isOnline(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
	private void show_warning(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Peringatan")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Coba Lagi",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								finish();
								startActivity(getIntent());
							}
						})
				.setNeutralButton("Keluar",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
								Register_Activity.this.finish();
								finish();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void menampilkan_data_pegawai() {
		if (!isOnline(Register_Activity.this)) {			
			String a = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			show_warning(a);
			return;
		} else {
			adapter = new List_Data_Pegawai(getApplicationContext(), list);

			List_View_Pegawai.setAdapter(adapter);

			List_View_Pegawai.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View v,
						int pos, long id) {
					selectedList = (Daftar_String) adapter.getItem(pos);
					/*
					 * Intent in = null;
					 * 
					 * in = new
					 * Intent(getApplicationContext(),Register_Activity.class);
					 * in.putExtra("nip", selectedList.getnip().toString());
					 * in.putExtra("nama_pegawai",
					 * selectedList.getnama_pegawai()); in.putExtra("jabatan",
					 * selectedList.getjabatan()); in.putExtra("golongan",
					 * selectedList.getgolongan()); startActivity(in); finish();
					 */

					if (pencarian_nip.getQuery().toString().isEmpty()) {
						Toast.makeText(Register_Activity.this,
								"Anda Belum Melakukan Pencarian NIP/NPK Pegawai",
								Toast.LENGTH_LONG).show();
					} else if (pencarian_nip.getQuery().toString().length() <= 8) { // kurangdari
						Toast.makeText(Register_Activity.this,
								"Masukkan NIP/NPK Pegawai Lengkap Anda", Toast.LENGTH_LONG)
								.show();
					} else if (pencarian_nip.getQuery().toString().length() > 8) { // lebihdari
						tampil_data();

						rel_list_view_pegawai.setVisibility(View.GONE);
						pencarian_nip.setQuery("", true);

						String get_password = "null";

						if (edit_reg_pass.getText().toString().trim()
								.contains(get_password)
								|| edit_reg_pass.getText().toString().isEmpty()) {

							Toast.makeText(Register_Activity.this,
									"Silahkan Registrasi Password Baru Anda",
									Toast.LENGTH_LONG).show();

							edit_reg_pass.setText("");
							edit_reg_pass.setVisibility(View.VISIBLE);
							simpan.setVisibility(View.VISIBLE);
							edit_reg_pass.setEnabled(true);
							checkbox_pass_reg.setVisibility(View.VISIBLE);

						} else {
							edit_reg_pass.setEnabled(false);
							checkbox_pass_reg.setVisibility(View.GONE);
							simpan.setVisibility(View.GONE);
						}

					}

				}
			});

		}
	}

	// ------------------ INI FUNGSI UNTUK MENONAKTIFKAN SCROOLBAR PADA LISTVIEW
	// ---------------------//
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_MOVE)
			return true;
		return super.dispatchTouchEvent(ev);
	}

	@Override
	public void onPointerCaptureChanged(boolean hasCapture) {

	}

	// ------------------ BATAS ---------------------//

	public boolean validasi_pass(EditText editText) {

		edit_reg_pass.setError(null);
		// length 0 means there is no text
		if (edit_reg_pass.length() == 0) {
			editText.setError(Html
					.fromHtml("<font color='red'>Pasword Tidak Boleh Kosong</font>"));
			return false;
		}
		return true;
	}

	public void simpan_btn_simpan_register(View view) {
		String ambil_reg = edit_reg_pass.getText().toString().trim();
		//String nip = edit_nip.getText().toString();

		if (!isOnline(Register_Activity.this)) {

			Toast.makeText(Register_Activity.this,
					"Tidak Ada Koneksi Internet", Toast.LENGTH_LONG).show();
			return;

		} else if (ambil_reg.isEmpty()) {

			validasi_pass(edit_reg_pass);
			return;
		} else if (ambil_reg.length() < 5) {
			Toast.makeText(Register_Activity.this,
					"Password Baru Minimal 5 Karakter", Toast.LENGTH_LONG).show();
			return;
		} else {
			new simpan_new_pass().execute();
			return;
		}

	}

	public void tampil_list_view(View view) {
		rel_list_view_pegawai.setVisibility(View.VISIBLE);

	}

	public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}

	class simpan_new_pass extends AsyncTask<String, String, String> {

		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			tampilan_proses = new ProgressDialog(Register_Activity.this);
			tampilan_proses.setMessage("Loading ...");
			tampilan_proses.setIndeterminate(false);
			tampilan_proses.setCancelable(true);
			tampilan_proses.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int berhasil;
			String responseString = null;
			String nip = edit_nip.getText().toString().trim();
			String pass = edit_reg_pass.getText().toString().trim();

			try {

				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair("pass", pass));

				Log.d("Request ke server!", "dimulai");
				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser
						.makeHttpRequest(Koneksi.simpan_pass, "POST", parameterNya);
				Log.d("Try Again", jsonObjectNya.toString());
				berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", jsonObjectNya.toString());
					Intent i = new Intent(Register_Activity.this,
							Login_Activity.class);
					startActivity(i);
					finish();
					return jsonObjectNya.getString(TAG_PESAN);

				} else {
					Log.d("Failed !!!", jsonObjectNya.getString(TAG_PESAN));
					// edit_reg_pass.setEnabled(true);
					return jsonObjectNya.getString(TAG_PESAN);
				}

			} catch (JSONException e) {
				responseString = e.toString();
			} catch (Exception e){
				responseString = e.toString();
			}

			return responseString;

		}

		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			tampilan_proses.dismiss();

			if (url_registrasi_nya != null) {
				Toast.makeText(Register_Activity.this, url_registrasi_nya,
						Toast.LENGTH_LONG).show();
				edit_reg_pass.setEnabled(false);
				simpan.setVisibility(View.GONE);
				checkbox_pass_reg.setVisibility(View.GONE);
			}

		}

	}

	public void menu_refresh() {
		// finish();
		// startActivity(getIntent());
		Register_Activity.this.finish();

		Intent i = new Intent(Register_Activity.this, Login_Activity.class);
		startActivity(i);
		finish();
	}

	// MENAMPILKAN DATA DARI LISTVIEW SECARA LANGSNUNG KE ACTIVTY YANG SAMA //
	private void tampil_data() {
		/*
		 * bundle = this.getIntent().getExtras();
		 * 
		 * bundle_nip = bundle.getString("nip"); bundle_nama =
		 * bundle.getString("nama_pegawai"); bundle_jabatan =
		 * bundle.getString("jabatan"); bundle_golongan =
		 * bundle.getString("golongan");
		 * 
		 * edit_nip.setText(bundle_nip); edit_namapeg.setText(bundle_nama);
		 * edit_jabatan.setText(bundle_jabatan);
		 * edit_golongan.setText(bundle_golongan);
		 * 
		 * selectedList.setnip(bundle_nip);
		 * selectedList.setnama_pegawai(bundle_nama);
		 * selectedList.setjabatan(bundle_jabatan);
		 * selectedList.setgolongan(bundle_golongan);
		 */

		edit_nip.setText(selectedList.getnip());
		edit_namapeg.setText(selectedList.getnama_pegawai());
		edit_jabatan.setText(selectedList.getjabatan());
		edit_golongan.setText(selectedList.getgolongan());
		edit_reg_pass.setText(selectedList.getpass());
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter.getFilter().filter(newText);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public void onBackPressed() {
		if (doubleBackToExitPressedOnce) {
			super.onBackPressed();
			return;
		}
		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Klik Tombol Kembali Dua Kali (2x) Untuk Keluar Dari Aplikasi E-SPPD", Toast.LENGTH_SHORT).show();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 2000);
	}

	private void Kembali() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Anda Akan Kembali Ke Menu Login ?");
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Register_Activity.this.finish();

				Intent i = new Intent(Register_Activity.this,
						Login_Activity.class);
				startActivity(i);
				finish();
			}
		});
		ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ad.setNeutralButton("Keluar", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				Register_Activity.this.finish();
				finish();

			}
		});
		ad.show();
	}
	public void kembali_activity(View view){
		Kembali();

	}
}
