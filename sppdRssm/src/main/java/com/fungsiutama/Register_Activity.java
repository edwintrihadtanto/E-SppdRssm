package com.fungsiutama;

import static com.e_sppd.rssm.R.animator.fade_in;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//import koneksi.JSONParser;
import koneksi.Java_Connection;
import koneksi.Koneksi;

public class Register_Activity extends AppCompatActivity {

	public ImageView imgshowpass1, imgshowpass2, img_contentcopy, img_emailinfo, img_silanghapus, gmbar_loading_register;
	private EditText enip_pegawai, enama_pegawai, ejabatan, passbaru, e_cari, e_email ;
	Animation anim_hilang, anim_putar, anim_flash;
	RelativeLayout frame_loading_register;
	private static final String TAG_BERHASIL 	= "success";
	public final static String TAG_PESAN_DIALOG	= "message";
	public final static String TAG_NIP 			= "nip";
	public final static String TAG_NAMA_PEGAWAI = "nama_pegawai";
	public final static String TAG_JABATAN 		= "jabatan";
	public final static String TAG_GOLONGAN 	= "golongan";
	public final static String TAG_PASSWORD 	= "password";
	public final static String TAG_EMAIL 		= "email";

	public String nip, nama_pegawai, jabatan, golongan, password, email, pesannya;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final Handler mainHandler = new Handler(Looper.getMainLooper());
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
			Toast.makeText(Register_Activity.this, "..NIP/NPK Pegawai Sudah Tercopy..", Toast.LENGTH_LONG).show();
		});

		img_emailinfo.setOnClickListener(v -> {
			String pesan = "Alamat email digunakan untuk Notifikasi e-SPPD\nHub. ITISI jika ada kendala!";
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
	private void cekDataPegawai() {

		showLoading();

		String nipCari = e_cari.getText().toString().trim();
		executor.execute(() -> {

			String hasil = null;

			try {

				HashMap<String, String> params = new HashMap<>();
				params.put("nip", nipCari);

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.LINK_PENCARIAN,
						params
				);

				if (response != null) {

					JSONObject json = new JSONObject(response);

					nip           = json.getString(TAG_NIP);
					nama_pegawai  = json.getString(TAG_NAMA_PEGAWAI);
					jabatan       = json.getString(TAG_JABATAN);
					golongan      = json.getString(TAG_GOLONGAN);
					password      = json.getString(TAG_PASSWORD);
					email         = json.getString(TAG_EMAIL);
					pesannya      = json.getString(TAG_PESAN_DIALOG);

					hasil = json.getString(TAG_BERHASIL);

					Log.i("Cek_Data_Pegawai", json.toString());
				}

			} catch (Exception e) {

				Log.e("Cek_Data_Pegawai", "ERROR", e);
				hasil = e.toString();

			}

			String finalHasil = hasil;

			mainHandler.post(() -> {

				hideLoading();

				Log.i("Cek_Data_Pegawai", "Hasil: " + finalHasil);

				if (finalHasil == null) {
					Toast.makeText(
							Register_Activity.this,
							"Error Koneksi Server\nHubungi IT RSSM",
							Toast.LENGTH_LONG
					).show();
					return;
				}

				pesan_cekdataregister(pesannya);
				e_email.setEnabled(email.isEmpty());

				switch (finalHasil) {

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

			});

		});

	}
	private void SimpanPassword() {

		String nip       = enip_pegawai.getText().toString().trim();
		String passBaru  = passbaru.getText().toString().trim();
		String emailBaru = e_email.getText().toString().trim();

		showLoading();
		executor.execute(() -> {

			String hasil = null;
			String pesan = "";

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

				if (response != null) {

					JSONObject json = new JSONObject(response);

					pesan = json.getString(TAG_PESAN_DIALOG);
					hasil = json.getString(TAG_BERHASIL);

					Log.d("req_register", json.toString());
				}

			} catch (Exception e) {
				Log.e("Proses_Simpan_Pass", "ERROR", e);
				hasil = e.toString();
			}

			String finalHasil = hasil;
			String finalPesan = pesan;

			mainHandler.post(() -> {

				hideLoading();

				Log.e("Proses_Simpan_Pass", "Hasil : " + finalHasil);

				if (finalHasil == null) {

					Toast.makeText(
							Register_Activity.this,
							"Koneksi server bermasalah",
							Toast.LENGTH_LONG
					).show();

					return;
				}

				Toast.makeText(
						Register_Activity.this,
						finalPesan,
						Toast.LENGTH_LONG
				).show();

			});

		});
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

	public void pencarian_data(View view) {
		view.startAnimation(anim_hilang);
		datakosong();
		if (e_cari.getText().toString().isEmpty()){
			Snackbar.make(view, "Pencarian data nip belum diisi!", Snackbar.LENGTH_LONG) .setAction("Snackbar", null).show();
		}else{
			if (cek_koneksi_internet(Register_Activity.this)) {
				String a = "Tidak ada sambungan Internet.\nPastikan koneksi data selular aktif atau terhubung dengan jaringan Wifi, lalu coba lagi";
				pesan_cekdataregister(a);
			}else {
				cekDataPegawai();
			}
		}

	}
	public void simpan_password(View view) {
		view.startAnimation(anim_hilang);
		String a = enip_pegawai.getText().toString();
		String b = enama_pegawai.getText().toString();
		String c = ejabatan.getText().toString();
		String d = passbaru.getText().toString();

		if ((a.isEmpty())&&(b.isEmpty())&&(c.isEmpty())){
			Snackbar.make(view, "Anda harus melakukan pencarian data nip terlebih dahulu", Snackbar.LENGTH_LONG)
					.setAction("Snackbar", null).show();
		}else if (d.isEmpty()){
			Snackbar.make(view, "Password belum diisi", Snackbar.LENGTH_LONG)
					.setAction("Snackbar", null).show();
		}else{
			if (cek_koneksi_internet(Register_Activity.this)) {
				String cek = "Tidak ada sambungan Internet.\nPastikan koneksi data selular aktif atau terhubung dengan jaringan Wi-fi, lalu coba lagi";
				pesan_cekdataregister(cek);
			}else {
				SimpanPassword();
			}
		}
	}

	private boolean cek_koneksi_internet(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo == null || !netInfo.isConnectedOrConnecting();
	}

	public void showLoading() {
		frame_loading_register.setVisibility(View.VISIBLE);

		Glide.with(Register_Activity.this)
				.load(R.drawable.loading_blue)
				.into(gmbar_loading_register);
	}
	public void hideLoading() {
		frame_loading_register.setVisibility(View.GONE);
	}
}
