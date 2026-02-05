package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import koneksi.JSONParser;
import koneksi.Koneksi;
public class Profil extends AppCompatActivity implements OnClickListener {
	private static final String TAG = "Profil";
	private TextView nip_lokal, update_nip, update_namapeg, update_jabatan, update_golongan, update_reg_pass;
	Button btn_change,btn_batal, btn_update_pass;
	ProgressDialog loading;
	EditText update_reg_pass_ulangi, update_unit, update_email;
	ImageView imgedit_email1, imgedit_email2, imgedit_unit, imgedit_unit2, img_showpass_profil1, img_showpass_profil2, gmbar_loading, img_refresh;
	Animation anim_hilang, anim_putar;
	String nipp, namap, jabatanp, golonganp, passwordp, emailp, unitkerjap, path;
	RelativeLayout frame_loading, frame_profil;
	private static final String TAG_BERHASIL = "sukses";
	private static final String TAG_PESAN 	 = "pesan";
	private static final String NAMA 		= "nama";
	private static final String JABATAN 	= "jabatan";
	private static final String GOLONGAN 	= "golongan";
	private static final String PASSWORD 	= "password";
	private static final String EMAIL 		= "email";
	private static final String UNIT 		= "unit";
	private static final String PATH 		= "path";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_profil);
		anim_hilang 			= AnimationUtils.loadAnimation(this, R.anim.anim_menghilang);
		anim_putar				= AnimationUtils.loadAnimation(this, R.anim.anim_berputarrepeat);

		gmbar_loading 			= findViewById(R.id.gmbar_loading);
		img_refresh 			= findViewById(R.id.btn_refresh_profil);
		frame_loading           = findViewById(R.id.frame_loading);
		frame_profil            = findViewById(R.id.RelativeLayoutlist_profil);

		nip_lokal 				= findViewById(R.id.nip_lokal);
		btn_change 				= findViewById(R.id.btn_change);
		btn_batal				= findViewById(R.id.btn_batal);
		btn_update_pass 		= findViewById(R.id.btn_update_pass);

		update_nip 				= findViewById(R.id.update_nip);
		update_namapeg 			= findViewById(R.id.update_namapeg);
		update_jabatan 			= findViewById(R.id.update_jabatan);
		update_golongan 		= findViewById(R.id.update_golongan);
		update_reg_pass 		= findViewById(R.id.update_reg_pass);
		update_reg_pass_ulangi  = findViewById(R.id.update_reg_pass_ulangi);
		update_email			= findViewById(R.id.update_email);
		update_email.setEnabled(false);
		update_unit				= findViewById(R.id.update_unit);
		update_unit.setEnabled(false);

		imgedit_email1			= findViewById(R.id.imgedit_email1);
		imgedit_email2			= findViewById(R.id.imgedit_email2);
		imgedit_unit 			= findViewById(R.id.imgedit_unit);
		imgedit_unit2 			= findViewById(R.id.imgedit_unit2);

		img_showpass_profil1	= findViewById(R.id.img_showpass_profil1);
		img_showpass_profil2	= findViewById(R.id.img_showpass_profil2);

		Glide.with(Profil.this)
				// LOAD URL DARI LOKAL DRAWABLE
				.load(R.drawable.loading_ring)
				.asGif()
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(img_refresh);
		
		imgedit_email1.setOnClickListener(v -> {
			imgedit_email1.setVisibility(View.GONE);
			imgedit_email2.setVisibility(View.VISIBLE);
			update_email.setEnabled(true);
			update_email.requestFocus();
			Snackbar.make(v, "Silahkan Anda Lakukan Perubahan Email Anda\nAbaikan jika Batal Perubahan Email", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();
		});

		imgedit_email2.setOnClickListener(v -> {
			imgedit_email1.setVisibility(View.VISIBLE);
			imgedit_email2.setVisibility(View.GONE);
			update_email.setEnabled(false);
			v.startAnimation(anim_hilang);
			new Proses_update_email().execute();
		});

		imgedit_unit.setOnClickListener(v -> {
			imgedit_unit.setVisibility(View.GONE);
			imgedit_unit2.setVisibility(View.VISIBLE);
			update_unit.setEnabled(true);
			update_unit.requestFocus();
			Snackbar.make(v, "Silahkan Anda Lakukan Perubahan Unit Kerja Anda\nAbaikan jika Batal Perubahan Unit Kerja", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();
		});

		imgedit_unit2.setOnClickListener(v -> {
			imgedit_unit.setVisibility(View.VISIBLE);
			imgedit_unit2.setVisibility(View.GONE);
			update_unit.setEnabled(false);
			v.startAnimation(anim_hilang);
			new Proses_update_unitkerja().execute();
		});

		img_showpass_profil1.setOnClickListener(v -> {
			update_reg_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
			img_showpass_profil1.setVisibility(View.GONE);
			img_showpass_profil2.setVisibility(View.VISIBLE);
		});

		img_showpass_profil2.setOnClickListener(v -> {
			update_reg_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
			img_showpass_profil1.setVisibility(View.VISIBLE);
			img_showpass_profil2.setVisibility(View.GONE);
		});

		Bundle b = getIntent().getExtras();
		nipp = null;
		if (b != null) {
			nipp = b.getString("transfer_nip");
			nip_lokal.setText(nipp);
			new Loading_Profil().execute();
		}
//		String nama_pegawai = null;
//		if (b != null) {
//			nama_pegawai = b.getString("transfer_nama_pegawai");
//		}
//		String jabatan = null;
//		if (b != null) {
//			jabatan = b.getString("transfer_jabatan");
//		}
//		String golongan = null;
//		if (b != null) {
//			golongan = b.getString("transfer_golongan");
//		}
//		String password = null;
//		if (b != null) {
//			password = b.getString("transfer_password");
//		}
//
//		String email = null;
//		if (b != null) {
//			email = b.getString("email");
//		}

		btn_change.setOnClickListener(this);
		btn_update_pass.setOnClickListener(this);
		btn_batal.setOnClickListener(this);
	}

	public void isi_text() {
		update_nip.setText(nipp);
		update_namapeg.setText(namap);
		update_jabatan.setText(jabatanp);
		update_golongan.setText(golonganp);
		update_email.setText(emailp);
		update_unit.setText(unitkerjap);
		if (passwordp != null) {
			if (passwordp.isEmpty()){
				update_reg_pass.setText(null);
			}else {
				update_reg_pass.setText(passwordp);
			}
		}
		/*
		ViewFlipper viewFlipper = findViewById(R.id.viewFlippermainactivity);
		viewFlipper.setFlipInterval(2500);
		viewFlipper.startFlipping();
		Log.e(TAG, "" + path);
//				String path = "";
				File imageFile = new File("https://apprssm.rssoedono.jatimprov.go.id/esppd_webbase/mobile/profil/foto.jpg");
				Bitmap bitmapImage = BitmapFactory.decodeFile(imageFile.toString());
				ImageView imageView = new ImageView(this);
				imageView.setImageBitmap(bitmapImage);
				viewFlipper.addView(imageView);
				*/
	}

	public void aktif() {
		update_nip.setEnabled(false);
		update_namapeg.setEnabled(false);
		update_jabatan.setEnabled(false);
		update_golongan.setEnabled(false);
		update_reg_pass.setEnabled(false);

		update_reg_pass_ulangi.setVisibility(View.VISIBLE);
		update_reg_pass_ulangi.requestFocus();
		btn_update_pass.setVisibility(View.VISIBLE);

		btn_batal.setVisibility(View.VISIBLE);
		btn_change.setVisibility(View.GONE);
	}
	public void batal() {
		update_nip.setEnabled(false);
		update_namapeg.setEnabled(false);
		update_jabatan.setEnabled(false);
		update_golongan.setEnabled(false);
		update_reg_pass.setEnabled(false);
		update_reg_pass_ulangi.setText("");
		update_reg_pass_ulangi.setVisibility(View.GONE);
		btn_update_pass.setVisibility(View.GONE);
		btn_batal.setVisibility(View.GONE);
		btn_change.setVisibility(View.VISIBLE);
	}

	public void kembali_activity(View view){
		super.onBackPressed();
	}

	@SuppressLint("NonConstantResourceId")
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_change:
			v.startAnimation(anim_hilang);
			aktif();
			break;
		case R.id.btn_batal:
			v.startAnimation(anim_hilang);
			batal();
			break;
		case R.id.btn_update_pass:
			v.startAnimation(anim_hilang);
			if (update_reg_pass_ulangi.getText().toString().isEmpty()) {
				Snackbar.make(v, "Password Harap Diisi Dengan Benar !!!\nPassword Kurang Lebih Minimal Lima (5) Karakter", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();
			} else if (update_reg_pass_ulangi.getText().toString().length() < 5) {
				Snackbar.make(v, "Password Kurang Lebih Minimal Lima (5) Karakter", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();
			} else {
				new Proses_update_Pass().execute();
			}

			break;
		default:
		}
	}

	@SuppressLint("StaticFieldLeak")
	public class Proses_update_Pass extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			loading = new ProgressDialog(Profil.this);
//			loading.setMessage("Sedang memuat...");
//			loading.setIndeterminate(false);
//			loading.setCancelable(false);
//			loading.show();
			loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {
			int berhasil;

			String nip_pegawai = nip_lokal.getText().toString().trim();
			String ambil_pass_baru = update_reg_pass_ulangi.getText()
					.toString().trim();

			try {

				List<NameValuePair> parameterNya = new ArrayList<>();
				parameterNya.add(new BasicNameValuePair("nip_pegawai", nip_pegawai));
				parameterNya.add(new BasicNameValuePair("ambil_pass_baru", ambil_pass_baru));

				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser.makeHttpRequest(Koneksi.update_pass, "POST", parameterNya);

				Log.d("ProsesUpdatePass:", jsonObjectNya.toString());

				berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", jsonObjectNya.toString());
					finish();
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					Log.d("Failed !!!", jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_PESAN);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			//loading.dismiss();
			Log.e(TAG, "ProsesUpdatePass : " + url_registrasi_nya);
			if (url_registrasi_nya != null) {
				loading_sembunyi();
				Toast.makeText(Profil.this, url_registrasi_nya,
						Toast.LENGTH_LONG).show();
			}
		}
	}

	@SuppressLint("StaticFieldLeak")
	public class Proses_update_email extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			loading = new ProgressDialog(Profil.this);
//			loading.setMessage("Sedang memuat...");
//			loading.setIndeterminate(false);
//			loading.setCancelable(false);
//			loading.show();
			loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {
			int berhasil;

			String nip 	 = nip_lokal.getText().toString().trim();
			String email = update_email.getText().toString().trim();

			try {

				List<NameValuePair> parameterNya = new ArrayList<>();
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair("email", email));

				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser.makeHttpRequest(Koneksi.update_email, "POST", parameterNya);

				Log.d("ProsesUpdateEmail:", jsonObjectNya.toString());

				berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", jsonObjectNya.toString());
					//finish();
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					Log.d("Failed !!!", jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_PESAN);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			//loading.dismiss();
			Log.i(TAG, "ProsesUpdateEmail : " + url_registrasi_nya);
			if (url_registrasi_nya != null) {
				loading_sembunyi();
				Toast.makeText(Profil.this, url_registrasi_nya,
						Toast.LENGTH_LONG).show();
			}
		}
	}
	@SuppressLint("StaticFieldLeak")
	public class Proses_update_unitkerja extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			loading_tampil();
		}
		@Override
		protected String doInBackground(String... args) {
			int berhasil;

			String nip 	 		= nip_lokal.getText().toString().trim();
			String unitkerja 	= update_unit.getText().toString().trim();

			try {

				List<NameValuePair> parameterNya = new ArrayList<>();
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair("unitkerja", unitkerja));

				Log.d("ProsesUpdateUnit:", "proses update start");

				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser.makeHttpRequest(Koneksi.update_unit, "POST", parameterNya);

				Log.d("ProsesUpdateUnit:", jsonObjectNya.toString());

				berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", jsonObjectNya.toString());
					//finish();
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					Log.d("Failed !!!", jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_PESAN);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String url_registrasi_nya) {

			Log.i(TAG, "ProsesUpdateUnit : " + url_registrasi_nya);
			if (url_registrasi_nya != null) {
				loading_sembunyi();
				Toast.makeText(Profil.this, url_registrasi_nya, Toast.LENGTH_LONG).show();
			}
		}
	}
	@SuppressLint("StaticFieldLeak")
	public class Loading_Profil extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			loading = new ProgressDialog(Profil.this);
//			loading.setMessage("Sedang memuat data pegawai...");
//			loading.setIndeterminate(false);
//			loading.setCancelable(false);
//			loading.show();
            loading_tampil();
		}

		@Override
		protected String doInBackground(String... args) {
			int berhasil;

			String nip 	 = nip_lokal.getText().toString().trim();

			try {

				List<NameValuePair> parameterNya = new ArrayList<>();
				parameterNya.add(new BasicNameValuePair("nip", nip));

				Log.d("Loading_Profil:", "proses update start");

				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser.makeHttpRequest(Koneksi.loading_profil, "POST", parameterNya);

				Log.d("Loading_Profil:", jsonObjectNya.toString());
				Log.d("Loading_Profil:", jsonObjectNya.getString(TAG_PESAN));

				berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					namap 		= jsonObjectNya.getString(NAMA);
					jabatanp 	= jsonObjectNya.getString(JABATAN);
					golonganp 	= jsonObjectNya.getString(GOLONGAN);
					passwordp 	= jsonObjectNya.getString(PASSWORD);
					emailp 		= jsonObjectNya.getString(EMAIL);
					unitkerjap	= jsonObjectNya.getString(UNIT);
					path	 	= jsonObjectNya.getString(PATH);
					return jsonObjectNya.getString(TAG_PESAN);

				}else if (berhasil == 404) {
					finish();
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					return jsonObjectNya.getString(TAG_PESAN);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			//loading.dismiss();
			Log.i(TAG, "Loading_Profil : " + url_registrasi_nya);
			if (url_registrasi_nya != null) {
				loading_sembunyi();
				Toast.makeText(Profil.this, url_registrasi_nya, Toast.LENGTH_LONG).show();
			}else{
				loading_tampil();
			}
			isi_text();
		}
	}

	public void clik_foto(View view) {
		Snackbar.make(view, "Pilih Foto dengan Ukuran File Maks. 1 Mb", Snackbar.LENGTH_LONG).setAction("Snackbar", null).show();
	}

	public void refresh_profil(View view) {

        view.startAnimation(anim_putar);
        anim_putar.setAnimationListener(new Animation.AnimationListener() {

            @SuppressLint("UnsafeIntentLaunch")
			@Override
            public void onAnimationStart(Animation animation) {
                finish();
                startActivity(getIntent());
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }
        });
	}

	public void loading_sembunyi() {
        frame_loading.setVisibility(View.GONE);
		frame_profil.setVisibility(View.VISIBLE);
    }

    public void loading_tampil() {
        frame_loading.setVisibility(View.VISIBLE);
		frame_profil.setVisibility(View.GONE);
		Glide.with(Profil.this)
				// LOAD URL DARI LOKAL DRAWABLE
				.load(R.drawable.loading_ring)
				.asGif()
				.diskCacheStrategy(DiskCacheStrategy.SOURCE)
				.into(gmbar_loading);
    }
}
