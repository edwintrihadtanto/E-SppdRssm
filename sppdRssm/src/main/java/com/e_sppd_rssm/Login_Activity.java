package com.e_sppd_rssm;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

//import koneksi.JSONParser;
import koneksi.Java_Connection;
import koneksi.Koneksi;
import koneksi.PermissionHelper;

public class Login_Activity extends AppCompatActivity {
	private final static String TAG = "Login_Activity";
	public static final String nippegawai 		= "nip";
	private static final String versi 			= "versi";

	private static final String TAG_CODE 		= "code";
	private static final String TAG_PESAN_CEK	= "pesan";
	private static final String TAG_WARNING		= "warning";
	private static final String TAG_VERSIBARU	= "versiygbaru";

	private static final String TAG_SUKSES2 	= "success";
	private static final String TAG_PESAN2 		= "message";

	private static final String Security_Level 	= "level";

	public final static String TAG_NIP 			= "nip";
	public final static String TAG_NAMA_PEGAWAI = "nama_pegawai";
	public final static String TAG_JABATAN 		= "jabatan";
	public final static String TAG_GOLONGAN 	= "golongan";
	public final static String TAG_UNIT 		= "unit";
	public final static String TAG_PASSWORD 	= "password";
	public final static String TAG_EMAIL 		= "email";
	private static final int progress_DOWNLOAD 	= 0;
	public  static final int RequestPermissionCode_StorageCamera  = 11 ;

	private final String[] daftarversiaplikasi={
			"Versi 3.0",
			"Versi 2.1",
			"Versi 2.0",
			"Versi 1.3.3",
			"Versi 1.3.2",
			"Versi 1.3.1",
			"Versi 1.2",
			"Versi 1.1",
			"Versi 1.0"};
	private String nip, nama_pegawai, jabatan, golongan, unit, password, email, kirim_versi;
	public static final String my_shared_preferences = "my_shared_preferences";
	public static final String session_status_level1 = "session_status_level1";
	public static final String session_status_level2 = "session_status_level2";
	public String pesan, warning, versiygbaru, progressdownload;

	PermissionHelper permissionHelper;
	ImageView img_showpass_login1, img_showpass_login2, imgicon, gmbar_loading_login;
	EditText edit_pass, edit_nip;
	ProgressDialog loading, download;
//	JSONParser classJsonParser = new JSONParser();
	TextView cek_versi_apk, develpe;
	ListView listView;
	ArrayAdapter<String> arrayAdapter;
	SharedPreferences sharedpreferences;
	Boolean session_1 = false;
	Boolean session_2 = false;
	Animation animAlpha, animkekiri, animkekanan;
	RelativeLayout frame_loading_login;
	CardView cardView_btnlogin, cardView_register, cardview_lupapass;
	String info = "© ESPPD 2017-2024,\nDeveloped by I.T.I.S.I - RSSM";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_activity);
		Permission_AksesCameradanStorage();
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		permissionHelper = new PermissionHelper(this);

		animAlpha 	= AnimationUtils.loadAnimation(this, R.anim.anim_menghilang);
		animkekiri 	= AnimationUtils.loadAnimation(this, R.anim.anim_kekiri);
		animkekanan = AnimationUtils.loadAnimation(this, R.anim.anim_kekanan);
		gmbar_loading_login = findViewById(R.id.gmbar_loading_login);
		frame_loading_login = findViewById(R.id.frame_loading_login);
		edit_pass 			= findViewById(R.id.edit_pass);
		edit_nip 			= findViewById(R.id.edit_nip);
		cek_versi_apk		= findViewById(R.id.cek_versi_apk);
		cardView_btnlogin 	= findViewById(R.id.cardView_btnlogin);
		cardView_register 	= findViewById(R.id.cardView_register);
		cardview_lupapass 	= findViewById(R.id.cardview_lupapass);

		develpe				= findViewById(R.id.develp);
		img_showpass_login1 = findViewById(R.id.img_showpass_login1);
		img_showpass_login2 = findViewById(R.id.img_showpass_login2);
		develpe.setText(info);

		String versi = "3.0"; //JANGAN LUPA VERSI INI DIRUBAH SESUAI UPDATENYA
		cek_versi_apk.setText(versi);
		kirim_versi = cek_versi_apk.getText().toString();
		//edit_nip.setText("303-03081992-052017-8776");
		//edit_pass.setText("edwin");
		//edit_nip.setText("12345");
		//edit_pass.setText("admin12345");
		// -----------------------------SCRIPT SHOW / HIDE
		// PASSWORD---------------------------- //

		// Cek session login jika TRUE maka langsung buka MainActivityBaru_Admin
		sharedpreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
		session_1 		= sharedpreferences.getBoolean(session_status_level1, false);
		session_2 		= sharedpreferences.getBoolean(session_status_level2, false);
		nip 			= sharedpreferences.getString(TAG_NIP, null);
		nama_pegawai 	= sharedpreferences.getString(TAG_NAMA_PEGAWAI, null);
		jabatan			= sharedpreferences.getString(TAG_JABATAN, null);
		golongan 		= sharedpreferences.getString(TAG_GOLONGAN, null);
		unit			= sharedpreferences.getString(TAG_UNIT, null);
		password		= sharedpreferences.getString(TAG_PASSWORD, null);
		email			= sharedpreferences.getString(TAG_EMAIL, null);

		if (session_1) {
//			Intent intent = new Intent(Login_Activity.this, MainActivityBaru_Admin.class);
//			intent.putExtra(TAG_NIP, nip);
//			intent.putExtra(TAG_NAMA_PEGAWAI, nama_pegawai);
//			intent.putExtra(TAG_JABATAN, jabatan);
//			intent.putExtra(TAG_GOLONGAN, golongan);
//			intent.putExtra(TAG_UNIT, unit);
//			intent.putExtra(TAG_PASSWORD, password);
//			intent.putExtra(TAG_EMAIL, email);
//			intent.putExtra(versi, kirim_versi);
//			finish();
//			startActivity(intent);
			Toast.makeText(Login_Activity.this, "Aplikasi E-SPPD Khusus Admin Tidak Bisa Digunakan\nSilahkan Hubungi Administrator",
					Toast.LENGTH_LONG).show();
		}else if (session_2) {
			Intent intent = new Intent(Login_Activity.this, MainActivityBaru_Petugas.class);
			intent.putExtra(TAG_NIP, nip);
			intent.putExtra(TAG_NAMA_PEGAWAI, nama_pegawai);
			intent.putExtra(TAG_JABATAN, jabatan);
			intent.putExtra(TAG_GOLONGAN, golongan);
			intent.putExtra(TAG_UNIT, unit);
			intent.putExtra(TAG_PASSWORD, password);
			intent.putExtra(TAG_EMAIL, email);
			intent.putExtra(versi, kirim_versi);
			finish();
			startActivity(intent);
		}

		img_showpass_login1.setOnClickListener(v -> {
			edit_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
			v.startAnimation(animAlpha);
			img_showpass_login1.setVisibility(View.GONE);
			img_showpass_login2.setVisibility(View.VISIBLE);
		});

		img_showpass_login2.setOnClickListener(v -> {
			edit_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			v.startAnimation(animAlpha);
			img_showpass_login1.setVisibility(View.VISIBLE);
			img_showpass_login2.setVisibility(View.GONE);
		});

		
		// PENGECEKAN VALIDASI KERENNN
		edit_nip.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				validasi_nip(edit_nip);
			}
		});

		edit_pass.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				validasi_pass(edit_pass);
			}
		});


	}

	private void lupa_password() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_lupa_password);
		final TextView textcp1 = dialog.findViewById(R.id.textcp1);
		final TextView textcp2 = dialog.findViewById(R.id.textcp2);

		textcp1.setOnClickListener(v -> {
			//number = inputan dari editText
			String toDial="tel:"+textcp1.getText().toString();
			startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse(toDial)));
		});

		textcp2.setOnClickListener(v -> {
			//number = inputan dari editText
			String toDial2="tel:"+textcp2.getText().toString();
			startActivity(new Intent(Intent.ACTION_DIAL,Uri.parse(toDial2)));
		});
		dialog.show();
	}


	public void validasi_nip(EditText editText) {

		edit_nip.setError(null);
		// length 0 means there is no text
		if (edit_nip.length() == 0) {
			editText.setError(Html
					.fromHtml("<font color='red'>NIP/NPK Tidak Boleh Kosong</font>"));
		}
	}

	public void validasi_pass(EditText editText) {

		edit_pass.setError(null);
		if (edit_pass.length() == 0) {
			editText.setError(Html
					.fromHtml("<font color='red'>Password Tidak Boleh Kosong</font>"));
		}
	}
	
	private boolean terkoneksi_roaming(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();

	}
	public void gotolupapassword (View view){

		lupa_password();
		view.startAnimation(animkekanan);
	}

	public void gotoregister (View view){
		view.startAnimation(animkekiri);
		//Intent i = new Intent(Login_Activity.this,Register_Activity.class);
		Intent i = new Intent(Login_Activity.this,Register_Activity_Baru.class);
		startActivity(i);
		finish();
	}
	public void ceklogin(View view) {
		view.startAnimation(animAlpha);
		if (!terkoneksi_roaming(Login_Activity.this)) {
			String a = "Tidak ada sambungan Internet.\nPastikan Wi-fi atau Data Seluler aktif, lalu coba lagi";
			show_warning(a);

		}else if (edit_nip.getText().toString().isEmpty()) {
			Snackbar.make(view, "NIP/NPK Pegawai Belum Diisi", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();

		}else if (edit_pass.getText().toString().isEmpty()) {
			Snackbar.make(view, "Password Belum Diisi", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();

		}else if ((edit_nip.getText().toString().contains("ipde")) || (edit_nip.getText().toString().contains("su"))
				|| (edit_nip.getText().toString().contains("admin")) || (edit_nip.getText().toString().contains("123456789"))
				|| (edit_nip.getText().toString().contains("qwerty"))|| (edit_nip.getText().toString().contains("%"))
				|| (edit_pass.getText().toString().contains("ipde2017"))) {
			/*
			String nip = "ipde";
			String kirim_versi = cek_versi_apk.getText().toString();
			Intent intent = new Intent(Login_Activity.this,
					Main_Activity_SUPERUSER.class);
			intent.putExtra(nippegawai, nip);
			intent.putExtra(versi, kirim_versi);
			startActivity(intent);
			finish();
*/
			Toast.makeText(Login_Activity.this, "Aplikasi E-SPPD Tidak Bisa DiInjeksi\nCoba Lagi Dilain Kesempatan",
					Toast.LENGTH_LONG).show();

        } else {

			new Cek_Versi_Dulu().execute();
		}

	}

	@SuppressLint("StaticFieldLeak")
	/*public class Cek_Versi_Dulu extends AsyncTask<String, String, String> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {
			int jikaSukses;

			String Versi_Apk = cek_versi_apk.getText().toString();
			String responseString;
			try {

				List<NameValuePair> versi = new ArrayList<>();
				versi.add(new BasicNameValuePair("versi_apk", Versi_Apk));
				Log.d("Proses Cek Versi!", "dimulai");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(Koneksi.CEK_VERSI, "POST", versi);

				Log.d("Prosess Login...", jsonObjectNya.toString());
				jikaSukses = jsonObjectNya.getInt(TAG_CODE);

				if (jikaSukses == 1) { // VERSI APK Sudah Terbaru

					pesan = jsonObjectNya.getString(TAG_PESAN_CEK);
					Log.e("pesan:", jsonObjectNya.getString(TAG_PESAN_CEK));
					return jsonObjectNya.getString(TAG_CODE);

				}else if (jikaSukses == 405) { //Butuh Info Saja

					pesan 	= jsonObjectNya.getString(TAG_PESAN_CEK);
					warning = jsonObjectNya.getString(TAG_WARNING);

					Log.e("pesan:", jsonObjectNya.getString(TAG_PESAN_CEK));
					return jsonObjectNya.getString(TAG_CODE);

				}else if (jikaSukses == 404) { //Butuh Maintenance Saja

					pesan 	= jsonObjectNya.getString(TAG_PESAN_CEK);
					warning = jsonObjectNya.getString(TAG_WARNING);

					Log.e("pesan:", jsonObjectNya.getString(TAG_PESAN_CEK));
					return jsonObjectNya.getString(TAG_CODE);

				}else if (jikaSukses == 405404) { //Butuh Info dan Maintenance

					pesan 	= jsonObjectNya.getString(TAG_PESAN_CEK);
					warning = jsonObjectNya.getString(TAG_WARNING);

					Log.e("pesan:", jsonObjectNya.getString(TAG_PESAN_CEK));
					return jsonObjectNya.getString(TAG_CODE);

				}else if (jikaSukses == 101) { //DIBUTUHKAN PEMBARUAN KE VERSI YANG BARU

					pesan 		= jsonObjectNya.getString(TAG_PESAN_CEK);
					versiygbaru = jsonObjectNya.getString(TAG_VERSIBARU);
					warning 	= jsonObjectNya.getString(TAG_WARNING);

					Log.e("pesan:", jsonObjectNya.getString(TAG_PESAN_CEK));
					return jsonObjectNya.getString(TAG_CODE);

				} else {

					Log.d("pesan",jsonObjectNya.getString(TAG_PESAN_CEK));
					return jsonObjectNya.getString(TAG_CODE);
					
				}
			} catch (JSONException e) {
				responseString = e.toString();					
			} catch (Exception e){
				responseString = e.toString();
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String code) {
			loading_sembunyi();
			//1, 405, 404, 405404, 101
			switch (code) {
				case "1": //versiterbaru
					new Login_APK().execute();
					break;
				case "405": //info
					jikainfo(pesan);
					break;
				case "404": //maintenance
					jikamaintenance(pesan);
					break;
				case "405404": //info dan maintenance
					jikamaintenancedaninfo(pesan);
					break;
				case "101": //perpermbaruan versi atau download
					info_download(pesan);
					break;
			}

        }

	}*/

	public class Cek_Versi_Dulu extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading_tampil();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			String versiApk = cek_versi_apk.getText().toString().trim();
			Java_Connection jc = new Java_Connection();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("versi_apk", versiApk);

				Log.d("CEK_VERSI", "Request dimulai");

				String response = jc.sendPostRequest(
						Koneksi.CEK_VERSI,
						params
				);

				if (response == null) {
					return "0"; // gagal / tidak ada respon
				}

				Log.d("CEK_VERSI", "RESPON = " + response);

				JSONObject json = new JSONObject(response);
				int code = json.getInt(TAG_CODE);

				pesan = json.optString(TAG_PESAN_CEK, "");
				warning = json.optString(TAG_WARNING, "");
				versiygbaru = json.optString(TAG_VERSIBARU, "");

				return String.valueOf(code);

			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
		}

		@Override
		protected void onPostExecute(String code) {
			loading_sembunyi();

			switch (code) {
				case "1": // versi terbaru
					new Login_APK().execute();
					break;

				case "405": // info
					jikainfo(pesan);
					break;

				case "404": // maintenance
					jikamaintenance(pesan);
					break;

				case "405404": // info + maintenance
					jikamaintenancedaninfo(pesan);
					break;

				case "101": // wajib update
					info_download(pesan);
					break;

				default:
					Toast.makeText(
							getApplicationContext(),
							"Gagal cek versi",
							Toast.LENGTH_LONG
					).show();
					break;
			}
		}
	}

	private void info_versi_commingsoon(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Comming Soon")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Bye Bye",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void info_versi(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi Versi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Terima Kasih",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}

	//dialog untuk cek versi
	private void jikainfo(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(warning)
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Ok",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void jikamaintenance(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(warning)
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Terima Kasih",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void jikamaintenancedaninfo(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(warning)
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Terima Kasih",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}

	//------------------------------------------------------------------------------------

	/** @noinspection CatchMayIgnoreException*/
	private void info_download(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(warning)
				.setCancelable(false)
				.setIcon(R.drawable.ic_download_sppd)
				.setPositiveButton("Masuk Website",
						(dialog, id) -> {
							dialog.dismiss();
							Intent in = new Intent();
							in.setAction(Intent.ACTION_VIEW);
							in.addCategory(Intent.CATEGORY_BROWSABLE);
							in.setData(Uri.parse(Koneksi.URL_WEBSITE));
							startActivity(in);
						})
				.setNegativeButton("Download",
						(dialog, id) -> {
							try {
								new down_apk().execute(Koneksi.download_apk + "e-Sppd.v"
										+ URLEncoder.encode(versiygbaru, "UTF-8")+".apk");
							} catch (Exception ex) {
								// TODO Auto-generated catch block
								ex.getMessage();
//								ex.printStackTrace();
							}
						})

				.setNeutralButton("Keluar",
						(dialog, id) -> {
							dialog.dismiss();
							Login_Activity.this.finish();
							finish();
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	protected Dialog onCreateDialog(int id) {
		if (id == progress_DOWNLOAD) {
			download = new ProgressDialog(this);
			download.setMessage("Downloading file...\ne-Sppd.v" + versiygbaru + ".apk");
			download.setIndeterminate(false);
			download.setMax(100);
			download.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			download.setCancelable(false);
			download.show();
			return download;
		}
		return null;
	}
	
	//@SuppressLint("SdCardPath")
	@SuppressLint("StaticFieldLeak")
	private class down_apk extends AsyncTask<String, String, String>

	{
		@Override
		@SuppressWarnings("deprecation")
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(progress_DOWNLOAD);
		}

		@RequiresApi(api = Build.VERSION_CODES.O)
		@Override
		protected String doInBackground(String... f_url) {
			int count;
			String responseString = null; 											// 1 
			//String kirim_versi = cek_versi_apk.getText().toString();
			try {

				URL url = new URL(f_url[0]);

				URLConnection connection = url.openConnection();
				connection.connect();

				int lenghOfFile = connection.getContentLength();
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				@SuppressLint("SdCardPath")
				OutputStream output = Files.newOutputStream(Paths.get("/sdcard/Download/e-Sppd.v" + versiygbaru + ".apk"));
				byte[] data = new byte[1024];
				long total = 0;

				while ((count = input.read(data)) != -1) {
					total += count;
					publishProgress("" + (int) ((total * 100) / lenghOfFile));
					output.write(data, 0, count);
				}
				output.flush();
				output.close();
				input.close();
			} catch (ClientProtocolException e) { 	// 2
				responseString = e.toString();		//
			} catch (IOException e) {				//	
				responseString = e.toString();		//3
			}										//
			return responseString;					//4
		}

		@Override
		protected void onProgressUpdate(String... progress) {
			download.setProgress(Integer.parseInt(progress[0]));
			progressdownload = Arrays.toString(progress);
			Log.e(TAG, "Respon Download:" + Arrays.toString(progress));
		}

		@Override
		@SuppressWarnings("deprecation")
		protected void onPostExecute(String file_url) {
			dismissDialog(progress_DOWNLOAD);
			Log.e(TAG, "Respon Download:" + file_url);

			String pesan1 = "java.net.SocketException: recvfrom failed: ETIMEDOUT (Connection timed out)";
			String pesan2 = "java.net.UnknownHostException: Unable to resolve host";
			String pesan3 = "javax.net.ssl.SSLException: Read error: ssl=0x7037e39e88: I/O error during system call, Software caused connection abort";

			Toast.makeText(Login_Activity.this, progressdownload, Toast.LENGTH_LONG).show();

			if (file_url != null) {

				if (progressdownload.contains("[100]")){
					String pesan  = "Download E-SPPD V"+versiygbaru+" Sukses\nSilahkan di Instal Ulang di FOLDER DOWNLOAD\n\nJika masih bingung tata cara penginstalan Aplikasi E-SPPD, Silahkan Hubungi IPDE ext: 146";
					showprogress_download(pesan);
				}else{
					String pesan  = "Download Gagal";
					showprogress_download(pesan);
				}
				if ((file_url.contains(pesan2)) || (file_url.contains(pesan1)) || (file_url.contains(pesan3))) {
					String info_pesan1 = "Tidak Ada Koneksi Internet\n" +
							"Pastikan Wi-fi atau Data Seluler aktif dan lancar, lalu coba lagi";
					show_warning(info_pesan1);
				}else if (file_url.contains("Permission denied")) {
					Toast.makeText(Login_Activity.this, "Diperlukan Ijin Mengakses Penyimpanan\nPergi Ke Pengaturan->Manajemen Aplikasi->E-SPPD->Ijin Aplikasi->Aktifkan", Toast.LENGTH_LONG).show();
				}else {
					refresh();
				}
			}else{
				refresh();
			}
			super.onPostExecute(file_url);
		}
	}

	@SuppressLint("UnsafeIntentLaunch")
	private void showprogress_download(String a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(a)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Ok",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
							startActivity(getIntent());
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@SuppressLint("StaticFieldLeak")
	/*public class Login_APK extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			loading = new ProgressDialog(Login_Activity.this);
//			loading.setMessage("Sedang Memuat...");
//			loading.setIndeterminate(false);
//			loading.setIcon(R.drawable.ic_info_outline_24dp);
//			loading.setCancelable(false);
//			loading.show();
			loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {
		
			int jikaSukses;
			int cek_level;

			String responseString;
			String nippegawai 	= edit_nip.getText().toString();
			String passpwgawai 	= edit_pass.getText().toString();
			String Kirim_Cek_Versi_APK = cek_versi_apk.getText().toString();

			try {
			
				List<NameValuePair> namaDanPassword = new ArrayList<>();
				namaDanPassword.add(new BasicNameValuePair("nip", nippegawai));
				namaDanPassword.add(new BasicNameValuePair("pass", passpwgawai));
				namaDanPassword.add(new BasicNameValuePair("versi", Kirim_Cek_Versi_APK));
				
				Log.d("Login!", "dimulai");
			
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest
						(Koneksi.LINK_UNTUK_LOGIN_TES, "POST", namaDanPassword);
			
				Log.d("Coba login", jsonObjectNya.toString());

				jikaSukses 				  = jsonObjectNya.getInt(TAG_SUKSES2);

				if (jikaSukses == 1) {
					cek_level 	= jsonObjectNya.getInt(Security_Level);

					nip 			= jsonObjectNya.getString(TAG_NIP);
					nama_pegawai 	= jsonObjectNya.getString(TAG_NAMA_PEGAWAI);
					jabatan 		= jsonObjectNya.getString(TAG_JABATAN);
					golongan 		= jsonObjectNya.getString(TAG_GOLONGAN);
					unit 			= jsonObjectNya.getString(TAG_UNIT);
					password		= jsonObjectNya.getString(TAG_PASSWORD);
					email			= jsonObjectNya.getString(TAG_EMAIL);
					Log.d("pesan:", jsonObjectNya.toString());

					if (cek_level == 1) {

						String pesan = "Untuk Dapat Menggunakan Aplikasi E-SPPD Dengan Menggunakan\nUser :"+nip+
								"Password :"+password+"\nAnda Perlu Menghubungi Pihak Administrator Untuk Mendapatkan Hak Akses Aplikasi E-SPPD";
						info(pesan);
*//*
						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putBoolean(session_status_level1, true);
						editor.putString(TAG_NIP, nip);
						editor.putString(TAG_NAMA_PEGAWAI, nama_pegawai);
						editor.putString(versi, kirim_versi);
						editor.commit();

						Intent intent = new Intent(Login_Activity.this,
								MainActivityBaru_Admin.class);
						//intent.putExtra(nippegawai, nip);

						intent.putExtra(TAG_NIP, nip);
						intent.putExtra(TAG_NAMA_PEGAWAI, nama_pegawai);
						intent.putExtra(versi, kirim_versi);
						finish();
						startActivity(intent);
						*//*
					}else if (cek_level == 2) {

							SharedPreferences.Editor editor = sharedpreferences.edit();
							editor.putBoolean(session_status_level2, true);
							editor.putString(TAG_NIP, nip);
							editor.putString(TAG_NAMA_PEGAWAI, nama_pegawai);
							editor.putString(TAG_JABATAN, jabatan);
							editor.putString(TAG_GOLONGAN, golongan);
							editor.putString(TAG_UNIT, unit);
							editor.putString(TAG_PASSWORD, password);
							editor.putString(TAG_EMAIL, email);
							editor.putString(versi, kirim_versi);
							editor.apply();

							Intent intent = new Intent(Login_Activity.this, MainActivityBaru_Petugas.class);
							intent.putExtra(TAG_NIP, nip);
							intent.putExtra(TAG_NAMA_PEGAWAI, nama_pegawai);
							intent.putExtra(TAG_JABATAN, jabatan);
							intent.putExtra(TAG_GOLONGAN, golongan);
							intent.putExtra(TAG_UNIT, unit);
							intent.putExtra(TAG_PASSWORD, password);
							intent.putExtra(TAG_EMAIL, email);
							intent.putExtra(versi, kirim_versi);
							finish();
							startActivity(intent);
					}

					return jsonObjectNya.getString(TAG_PESAN2);
				} else {
					Log.d("Login_nya Gagal!",
							jsonObjectNya.getString(TAG_PESAN2));
					return jsonObjectNya.getString(TAG_PESAN2);
				}
			} catch (JSONException e) {
				responseString = e.toString();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				responseString = e.toString();
			}

			return responseString;
		}

		@Override
		protected void onPostExecute(String jawaban_json) {
			//loading.dismiss();
			loading_sembunyi();
			Log.i(TAG, "Respon Login " + jawaban_json);
			String pesan1 = "java.lang.RuntimeException: Can't create handler inside thread";
			String nippegawai 	= edit_nip.getText().toString();
			String passpwgawai 	= edit_pass.getText().toString();
				if (jawaban_json != null) {
					if (jawaban_json.contains(pesan1)) {
						String pesan = "Untuk Dapat Menggunakan Aplikasi E-SPPD Dengan Menggunakan\nUser :" + nippegawai +
								"\nPassword :" + passpwgawai + "\nAnda Perlu Menghubungi Pihak Administrator Untuk " +
								"Mendapatkan Hak Akses Aplikasi E-SPPD";
						info(pesan);
					}else{
						Toast.makeText(getApplicationContext(), jawaban_json, Toast.LENGTH_LONG).show();
					}
				}

		}

	}*/

	public class Login_APK extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading_tampil();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			String nippegawai   = edit_nip.getText().toString().trim();
			String passpegawai  = edit_pass.getText().toString().trim();
			String versiApk     = cek_versi_apk.getText().toString().trim();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("nip", nippegawai);
				params.put("pass", passpegawai);
				params.put("versi", versiApk);

				Log.d("LOGIN", "Request login dimulai");

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.LINK_UNTUK_LOGIN_TES,
						params
				);

				if (response == null) {
					return "Gagal terhubung ke server";
				}

				Log.d("LOGIN", "RESPON = " + response);

				JSONObject jsonObjectNya = new JSONObject(response);
				int jikaSukses = jsonObjectNya.getInt(TAG_SUKSES2);

				if (jikaSukses == 1) {

					int cek_level = jsonObjectNya.getInt(Security_Level);

					nip           = jsonObjectNya.getString(TAG_NIP);
					nama_pegawai  = jsonObjectNya.getString(TAG_NAMA_PEGAWAI);
					jabatan       = jsonObjectNya.getString(TAG_JABATAN);
					golongan      = jsonObjectNya.getString(TAG_GOLONGAN);
					unit          = jsonObjectNya.getString(TAG_UNIT);
					password      = jsonObjectNya.getString(TAG_PASSWORD);
					email         = jsonObjectNya.getString(TAG_EMAIL);

					if (cek_level == 1) {

						String pesan = "Untuk Dapat Menggunakan Aplikasi E-SPPD Dengan Menggunakan\n" +
								"User : " + nip +
								"\nPassword : " + password +
								"\nAnda Perlu Menghubungi Pihak Administrator Untuk Mendapatkan Hak Akses Aplikasi E-SPPD";

						runOnUiThread(() -> info(pesan));

					} else if (cek_level == 2) {

						SharedPreferences.Editor editor = sharedpreferences.edit();
						editor.putBoolean(session_status_level2, true);
						editor.putString(TAG_NIP, nip);
						editor.putString(TAG_NAMA_PEGAWAI, nama_pegawai);
						editor.putString(TAG_JABATAN, jabatan);
						editor.putString(TAG_GOLONGAN, golongan);
						editor.putString(TAG_UNIT, unit);
						editor.putString(TAG_PASSWORD, password);
						editor.putString(TAG_EMAIL, email);
						editor.putString(versi, versiApk);
						editor.apply();

						Intent intent = new Intent(
								Login_Activity.this,
								MainActivityBaru_Petugas.class
						);

						intent.putExtra(TAG_NIP, nip);
						intent.putExtra(TAG_NAMA_PEGAWAI, nama_pegawai);
						intent.putExtra(TAG_JABATAN, jabatan);
						intent.putExtra(TAG_GOLONGAN, golongan);
						intent.putExtra(TAG_UNIT, unit);
						intent.putExtra(TAG_PASSWORD, password);
						intent.putExtra(TAG_EMAIL, email);
						intent.putExtra(versi, versiApk);

						finish();
						startActivity(intent);
					}

					return jsonObjectNya.getString(TAG_PESAN2);

				} else {
					return jsonObjectNya.getString(TAG_PESAN2);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String jawaban_json) {
			loading_sembunyi();

			if (jawaban_json != null) {
				Toast.makeText(
						getApplicationContext(),
						jawaban_json,
						Toast.LENGTH_LONG
				).show();
			}
		}
	}

	private void show_warning(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Peringatan")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Coba Lagi",
						(dialog, id) -> {
							dialog.dismiss();
							refresh();
						})
				.setNeutralButton("Keluar",
						(dialog, id) -> {
							dialog.dismiss();
							Login_Activity.this.finish();
							finish();
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	private void info(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_info_outline_24dp)
				.setPositiveButton("Ok",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}

	@SuppressLint("UnsafeIntentLaunch")
	public void refresh() {
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
		ad.setMessage("Anda akan Keluar ?");
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setPositiveButton("Ya", (dialog, which) -> {
			Login_Activity.this.finish();
			finish();
		});
		ad.setNegativeButton("Tidak", (dialog, which) ->
				dialog.dismiss());
		ad.show();
	}

	private void tampilinformasi() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_informasi_update_versi);

		listView	 = dialog.findViewById(R.id.listupdate);
		arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, daftarversiaplikasi);
		listView.setAdapter(arrayAdapter);

		listView.setOnItemClickListener((parent, view, position, id) -> {
			String message="Anda Memilih "+((TextView)view).getText();
			String ambil = ((TextView)view).getText().toString();

			String versikedelapan = "Versi 2.1";
			String versiketujuh = "Versi 2.0";
			String versikeenam 	= "Versi 1.3.3";
			String versikelima 	= "Versi 1.3.2";
			String versikeempat = "Versi 1.3.1";
			String versiketiga 	= "Versi 1.2";
			String versikedua 	= "Versi 1.1";
			String versipertama = "Versi 1.0";
			if (ambil.equals(versipertama)){
				String isinya="- Aplikasi E-SPDD merupakan Aplikasi Pertama berbasis Mobile yang memiliki fungsi untuk mempermudah kinerja pegawai dalam pembuatan Laporan SPPD, Laporan Rincian Perjalanan Dinas, serta Laporan Rincian Perjalanan Dinas\n";
				info_versi(isinya);
			}else if (ambil.equals(versikedua)){
				String isinya="- Memperbaiki masalah bugs laporan pada versi sebelumnya\n";
				info_versi(isinya);
			}else if (ambil.equals(versiketiga)){
				String isinya="Memperbaiki masalah bugs register pada versi sebelumnya\n";
				info_versi(isinya);
			}else if (ambil.equals(versikeempat)){
				String isinya="- Memperbaiki masalah bugs uploads foto pada versi sebelumnya\n";
				info_versi(isinya);
			}else if (ambil.equals(versikelima)){
				String isinya="- Memperbaiki masalah bugs downloads laporan pada versi sebelumnya\n";
				info_versi(isinya);
			}else if (ambil.equals(versikeenam)){
				String isinya= "- Memperbaiki masalah bugs pada versi sebelumnya\n" +
						"- Penambahan Fitur Baru Kamera\n" +
						"- Design Baru\n" +
						"- Fungsi-fungsi Baru\n" +
						"- Dapat Melakukan Edit Data Rincian dan Rill Biaya\n";
				info_versi(isinya);
			}else if (ambil.equals(versiketujuh)){
				String isinya= "- Tampilan Baru, Lebih Simple dan Mudah Pengoperasian\n" +
						"- Penambahan Fitur Calling Problem\n" +
						"- Memperbaiki Permasalahan Beberapa Bugs Dari Versi Sebelumnya\n" +
						"- Penambahan Session Login, dimana Pengguna Tidak Diperlukan Login Lagi\n" +
						"- Penambahan Tombol Hapus Pada Sesi Pembuatan Laporan Perincian Biaya";
				info_versi(isinya);

			}else if (ambil.equals(versikedelapan)){
				String a= "Versi 2.1  Comming Soon";
				Toast.makeText(getApplicationContext(), a,
						Toast.LENGTH_LONG).show();
				String isinya= "::: Berdasarkan Kritik dan Saran Dari Pengguna Aplikasi E-SPPD :::\n" +
						"- Penggabungan Laporan Perjalanan Dinas Berkelompok sehingga " +
						"Hanya Perlu Membuat Satu Laporan Perjalanan Dinas Dalam (SPT Yang Berkelompok)\n" +
						"- Penambahan Tutorial Video Pengoperasian Aplikasi E-SPPD";
				info_versi_commingsoon(isinya);
			}else{
				String isinya= "Tidak Ada Informasi Versi";
				info_versi(isinya);
			}
			Toast.makeText(getApplicationContext(), message,
					Toast.LENGTH_LONG).show();
		});
		dialog.show();
	}

	public void setticker(LinearLayout parent_layout, String text,
						  Context Login) {
		if (!text.isEmpty()) {

			TextView view = new TextView(Login);
			view.setText(text);

			view.setTextColor(Color.BLACK);
			view.setTextSize(23.5F);

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
			Animation mAnimation = new TranslateAnimation(screenWidth, toXDelta, 0, 0);
			mAnimation.setDuration(15000);
			mAnimation.setRepeatMode(Animation.RESTART);
			mAnimation.setRepeatCount(Animation.INFINITE);

			view.setAnimation(mAnimation);
			parent_layout.addView(view);
		}
	}

	public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {

		if (RC == RequestPermissionCode_StorageCamera) {
			if ((PResult.length > 0) && (PResult[0] == PackageManager.PERMISSION_DENIED)) {

				Toast.makeText(Login_Activity.this, "Diperlukan Ijin Mengakses Lokasi Penyimpanan dan Kamera !!!", Toast.LENGTH_LONG).show();
			}
		}
	}

	public void Permission_AksesCameradanStorage(){

		if ((ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) &&
				(ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.CAMERA))) {

			Toast.makeText(Login_Activity.this, "Diperlukan Ijin Mengakses Penyimpanan  dan Kamera !!!", Toast.LENGTH_LONG).show();

			ActivityCompat.requestPermissions(Login_Activity.this,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
		} else {
			ActivityCompat.requestPermissions(Login_Activity.this,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
		}
	}

	public void loading_sembunyi() {
		frame_loading_login.setVisibility(View.GONE);
		edit_nip.setEnabled(true);
		edit_pass.setEnabled(true);
		cardView_btnlogin.setEnabled(true);
		cardView_register.setEnabled(true);
		cardview_lupapass.setEnabled(true);
	}

	public void loading_tampil() {
		frame_loading_login.setVisibility(View.VISIBLE);
		edit_nip.setEnabled(false);
		edit_pass.setEnabled(false);
		cardView_btnlogin.setEnabled(false);
		cardView_register.setEnabled(false);
		cardview_lupapass.setEnabled(false);

		Glide.with(Login_Activity.this)
				// LOAD URL DARI LOKAL DRAWABLE
				.load(R.drawable.loading_ring)
				.asGif()
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(gmbar_loading_login);
	}
}
