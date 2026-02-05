package com.e_sppd_rssm;

import static com.e_sppd.rssm.R.animator.fade_in;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;

//import koneksi.JSONParser;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class Register_Activity_Baru extends AppCompatActivity {

	public ImageView imgshowpass1, imgshowpass2, img_contentcopy, img_emailinfo, img_silanghapus, gmbar_loading_register;
	private EditText enip_pegawai, enama_pegawai, ejabatan, passbaru, e_cari, e_email ;
	ProgressDialog loading;
//	JSONParser classJsonParser = new JSONParser();
	Animation anim_hilang, anim_putar, anim_flash;
	RelativeLayout frame_loading_register;
	private static final String TAG_BERHASIL 	= "success";
//	private static final String TAG_PESAN 		= "message";

	public final static String TAG_PESAN_DIALOG	= "message";
	public final static String TAG_NIP 			= "nip";
	public final static String TAG_NAMA_PEGAWAI = "nama_pegawai";
	public final static String TAG_JABATAN 		= "jabatan";
	public final static String TAG_GOLONGAN 	= "golongan";
	public final static String TAG_PASSWORD 	= "password";
	public final static String TAG_EMAIL 		= "email";

	public String nip, nama_pegawai, jabatan, golongan, password, email, pesannya;
	Button simpan_pass;
	@SuppressLint("ResourceType")
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register_activity2);

		anim_hilang 	= AnimationUtils.loadAnimation(this, R.anim.anim_menghilang);
		anim_putar		= AnimationUtils.loadAnimation(this, R.anim.anim_berputar);
		anim_flash		= AnimationUtils.loadAnimation(getApplicationContext(), fade_in);

		imgshowpass1 	= findViewById(R.id.img_showpass1);
		imgshowpass2 	= findViewById(R.id.img_showpass2);
		e_cari  		= findViewById(R.id.e_cari);
		enip_pegawai	= findViewById(R.id.nip_pegawai);
		enama_pegawai	= findViewById(R.id.nama_pegawai);
		ejabatan		= findViewById(R.id.jabatan);
		passbaru 		= findViewById(R.id.pass_baru);
		img_contentcopy = findViewById(R.id.img_contentcopy);
		img_emailinfo 	= findViewById(R.id.img_emailinfo);
		e_email			= findViewById(R.id.editemail);
		simpan_pass		= findViewById(R.id.btn_simpan_pass);
		img_silanghapus = findViewById(R.id.img_silanghapus);

		gmbar_loading_register = findViewById(R.id.gmbar_loading_register);
		frame_loading_register = findViewById(R.id.frame_loading_register);

		passbaru.setTransformationMethod(PasswordTransformationMethod.getInstance());

		imgshowpass1.setOnClickListener(v -> {
			passbaru.setTransformationMethod(PasswordTransformationMethod.getInstance());
			imgshowpass1.setVisibility(View.GONE);
			imgshowpass2.setVisibility(View.VISIBLE);
		});

		imgshowpass2.setOnClickListener(v -> {
			passbaru.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			imgshowpass1.setVisibility(View.VISIBLE);
			imgshowpass2.setVisibility(View.GONE);
		});

		img_contentcopy.setOnClickListener(v -> {
			v.startAnimation(anim_hilang);
			String copas = enip_pegawai.getText().toString().trim();

			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("copas text", copas);
			clipboard.setPrimaryClip(clip);
			Toast.makeText(Register_Activity_Baru.this, "..NIP/NPK Pegawai Sudah Tercopy..", Toast.LENGTH_LONG).show();
		});

		img_emailinfo.setOnClickListener(v -> {
			String pesan = "Alamat Email Diperlukan Untuk Bantuan Notifikasi E-SPPD\nTerima Kasih Atas Perhatian dan Dukungannya";
			pesan_cekdataregister(pesan);
		});

		img_silanghapus.setOnClickListener(v -> {
			v.startAnimation(anim_hilang);
			e_cari.setText("");
		});

		img_emailinfo.setAnimation(anim_flash);
		anim_flash.setAnimationListener(new Animation.AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				finish();
			}
		});

	}

	@SuppressLint("StaticFieldLeak")
	/*public class Cek_Data_Pegawai extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {

//			int jikaSukses;
			String responseString;
			String e_cari_string 	= e_cari.getText().toString();

			try {

				List<NameValuePair> nippegawai = new ArrayList<>();
				nippegawai.add(new BasicNameValuePair("nip", e_cari_string));

				Log.d("req_register ", "Start");

				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest
						(Koneksi.LINK_PENCARIAN, "POST", nippegawai);

				Log.d("req_register ", jsonObjectNya.toString());

//				jikaSukses 	= jsonObjectNya.getInt(TAG_BERHASIL);

				nip 			= jsonObjectNya.getString(TAG_NIP);
				nama_pegawai 	= jsonObjectNya.getString(TAG_NAMA_PEGAWAI);
				jabatan 		= jsonObjectNya.getString(TAG_JABATAN);
				golongan 		= jsonObjectNya.getString(TAG_GOLONGAN);
				password		= jsonObjectNya.getString(TAG_PASSWORD);
				email			= jsonObjectNya.getString(TAG_EMAIL);
				pesannya 		= jsonObjectNya.getString(TAG_PESAN_DIALOG);
				Log.i("pesan", jsonObjectNya.getString(TAG_PESAN));
				return jsonObjectNya.getString(TAG_BERHASIL);

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
			Log.i("Cek_Data_Pegawai", "Hasil: " + jawaban_json);
			Log.e("error", jawaban_json);
			String error = "java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.String org.json.JSONObject.toString()' on a null object reference";
			if (jawaban_json.equals(error)){
				Toast.makeText(Register_Activity_Baru.this, "Error Koneksi Server\nHubungi IT RSSM", Toast.LENGTH_LONG).show();
				return;
			}
			pesan_cekdataregister(pesannya);
			e_email.setEnabled(email.isEmpty());
			if (jawaban_json.equals("1")){
				passbaru.setEnabled(false);
				simpan_pass.setVisibility(View.GONE);
				data();
			}else if (jawaban_json.equals("0")){
				passbaru.setEnabled(true);
				passbaru.requestFocus();
				simpan_pass.setVisibility(View.VISIBLE);
				data();
			}else{
				passbaru.setEnabled(false);
				datakosong();
			}

		}
	}*/
	public class Cek_Data_Pegawai extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading_tampil();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			String nipCari = e_cari.getText().toString().trim();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("nip", nipCari);

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.LINK_PENCARIAN,
						params
				);

				if (response == null) return null;

				JSONObject json = new JSONObject(response);

				nip           = json.getString(TAG_NIP);
				nama_pegawai  = json.getString(TAG_NAMA_PEGAWAI);
				jabatan       = json.getString(TAG_JABATAN);
				golongan      = json.getString(TAG_GOLONGAN);
				password      = json.getString(TAG_PASSWORD);
				email         = json.getString(TAG_EMAIL);
				pesannya      = json.getString(TAG_PESAN_DIALOG);

				Log.i("Cek_Data_Pegawai", json.toString());

				return json.getString(TAG_BERHASIL);

			} catch (Exception e) {
				Log.e("Cek_Data_Pegawai", "ERROR", e);
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			loading_sembunyi();

			Log.i("Cek_Data_Pegawai", "Hasil: " + hasil);

			if (hasil == null) {
				Toast.makeText(
						Register_Activity_Baru.this,
						"Error Koneksi Server\nHubungi IT RSSM",
						Toast.LENGTH_LONG
				).show();
				return;
			}

			pesan_cekdataregister(pesannya);
			e_email.setEnabled(email.isEmpty());

			switch (hasil) {
				case "1":
					passbaru.setEnabled(false);
					simpan_pass.setVisibility(View.GONE);
					data();
					break;

				case "0":
					passbaru.setEnabled(true);
					passbaru.requestFocus();
					simpan_pass.setVisibility(View.VISIBLE);
					data();
					break;

				default:
					passbaru.setEnabled(false);
					datakosong();
					break;
			}
		}
	}

	@SuppressLint("StaticFieldLeak")
	/*public class Proses_Simpan_Password_Baru extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading = new ProgressDialog(Register_Activity_Baru.this);
			loading.setMessage("Sedang memuat...");
			loading.setIndeterminate(false);
			loading.setIcon(R.drawable.ic_info_outline_24dp);
			loading.setCancelable(false);
			loading.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;
			String responseString;
			String nip 			= enip_pegawai.getText().toString();
			String pasbaru 		= passbaru.getText().toString();
			String emailbaru	= e_email.getText().toString();

			try {

				List<NameValuePair> nippegawai = new ArrayList<>();
				nippegawai.add(new BasicNameValuePair("nippegawai", nip));
				nippegawai.add(new BasicNameValuePair("password", pasbaru));
				nippegawai.add(new BasicNameValuePair("email", emailbaru));

				Log.d("req_register ", "Start");

				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest
						(Koneksi.simpan_pass_baru, "POST", nippegawai);

				Log.d("req_register ", jsonObjectNya.toString());

				jikaSukses 	= jsonObjectNya.getInt(TAG_BERHASIL);

				if (jikaSukses == 1) {
					pesannya 		= jsonObjectNya.getString(TAG_PESAN_DIALOG);
					Log.i("pesan sukses ", jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_BERHASIL);
				}else{
					pesannya 		= jsonObjectNya.getString(TAG_PESAN_DIALOG);
					Log.i("pesan gagal ", jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_BERHASIL);
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
			loading.dismiss();
			Log.e("Isinya", "jsonnya : " + jawaban_json);
			Toast.makeText(Register_Activity_Baru.this, pesannya, Toast.LENGTH_LONG).show();

		}
	}*/

	public class Proses_Simpan_Password_Baru extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading = new ProgressDialog(Register_Activity_Baru.this);
			loading.setMessage("Sedang memuat...");
			loading.setIndeterminate(false);
			loading.setIcon(R.drawable.ic_info_outline_24dp);
			loading.setCancelable(false);
			loading.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			String nip        = enip_pegawai.getText().toString().trim();
			String passBaru   = passbaru.getText().toString().trim();
			String emailBaru  = e_email.getText().toString().trim();

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("nippegawai", nip);
				params.put("password", passBaru);
				params.put("email", emailBaru);

				Log.d("req_register", "Start");

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.simpan_pass_baru,
						params
				);

				if (response == null) return null;

				JSONObject json = new JSONObject(response);

				pesannya = json.getString(TAG_PESAN_DIALOG);

				Log.d("req_register", json.toString());

				return json.getString(TAG_BERHASIL);

			} catch (Exception e) {
				Log.e("Proses_Simpan_Pass", "ERROR", e);
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			loading.dismiss();

			Log.e("Proses_Simpan_Pass", "Hasil : " + hasil);

			if (hasil == null) {
				Toast.makeText(
						Register_Activity_Baru.this,
						"Koneksi server bermasalah",
						Toast.LENGTH_LONG
				).show();
				return;
			}

			Toast.makeText(
					Register_Activity_Baru.this,
					pesannya,
					Toast.LENGTH_LONG
			).show();
		}
	}

	public void data(){
		enip_pegawai.setText(nip);
		enama_pegawai.setText(nama_pegawai);
		ejabatan.setText(jabatan);
		passbaru.setText(password);
		e_email.setText(email);
	}
	public void datakosong(){
		enip_pegawai.setText("");
		enama_pegawai.setText("");
		ejabatan.setText("");
		passbaru.setText("");
		e_email.setText("");
	}

	private void pesan_cekdataregister(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(true)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Ok",
						(dialog, id) -> dialog.dismiss());
		AlertDialog alert = builder.create();
		alert.show();
	}

	//KUMPULAN ONCLIK //-------------------------------------------------------

	public void refresh(View view) {
		view.startAnimation(anim_putar);
		loading_tampil();
		finish();
		startActivity(getIntent());
	}
	public void kembali_activity(View view) {
		Register_Activity_Baru.this.finish();
		Intent i = new Intent(Register_Activity_Baru.this, Login_Activity.class);
		startActivity(i);
		finish();
	}
	@Override
	public void onBackPressed() {
		Register_Activity_Baru.this.finish();
		Intent i = new Intent(Register_Activity_Baru.this, Login_Activity.class);
		startActivity(i);
		finish();
	}
	public void pencarian_data(View view) {
		view.startAnimation(anim_hilang);
		datakosong();
		if (e_cari.getText().toString().isEmpty()){
			Snackbar.make(view, "Pencarian Harus Diisi ya...\nTerima Kasih Atas Dukungan dan Partisinya...", Snackbar.LENGTH_LONG)
					.setAction("Snackbar", null).show();
		}else{
			if (cek_koneksi_internet(Register_Activity_Baru.this)) {
				String a = "Tidak ada sambungan Internet.\nPastikan koneksi data selular aktif atau terhubung dengan jaringan Wifi, lalu coba lagi";
				pesan_cekdataregister(a);
			}else {
				new Cek_Data_Pegawai().execute();
			}
		}

	}
	public void simpan_password(View view) {
		view.startAnimation(anim_putar);
		String a = enip_pegawai.getText().toString();
		String b = enama_pegawai.getText().toString();
		String c = ejabatan.getText().toString();
		String d = passbaru.getText().toString();

		if ((a.isEmpty())&&(b.isEmpty())&&(c.isEmpty())){
			Snackbar.make(view, "Anda Harus Melakukan Pencarian Data Terlebih Dahulu", Snackbar.LENGTH_LONG)
					.setAction("Snackbar", null).show();
		}else if (d.isEmpty()){
			Snackbar.make(view, "Password Anda Belum Diisi", Snackbar.LENGTH_LONG)
					.setAction("Snackbar", null).show();
		}else{
			if (cek_koneksi_internet(Register_Activity_Baru.this)) {
				String cek = "Tidak ada sambungan Internet.\nPastikan koneksi data selular aktif atau terhubung dengan jaringan Wi-fi, lalu coba lagi";
				pesan_cekdataregister(cek);
			}else {
				new Proses_Simpan_Password_Baru().execute();
			}
		}
	}

	private boolean cek_koneksi_internet(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo == null || !netInfo.isConnectedOrConnecting();
	}

	public void loading_tampil() {
		frame_loading_register.setVisibility(View.VISIBLE);

		Glide.with(Register_Activity_Baru.this)
				// LOAD URL DARI LOKAL DRAWABLE
				.load(R.drawable.loading_ring)
				.asGif()
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(gmbar_loading_register);
	}
	public void loading_sembunyi() {
		frame_loading_register.setVisibility(View.GONE);
	}
}
