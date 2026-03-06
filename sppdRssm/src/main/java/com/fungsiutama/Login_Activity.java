package com.fungsiutama;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.view.WindowCompat;

import com.bumptech.glide.Glide;
import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import koneksi.Java_Connection;
import koneksi.Koneksi;
import koneksi.PermissionHelper;

public class Login_Activity extends AppCompatActivity {
	private final static String TAG = "Login_Activity";
//	public static final String nippegawai 		= "nip";
	private static final String versi 			= "versi";

	private static final String TAG_CODE 		= "code";
	private static final String TAG_PESAN_CEK	= "pesan";
	private static final String TAG_WARNING		= "warning";
	private static final String TAG_VERSIBARU	= "versiygbaru";
	private static final String TAG_LINK	    = "link";

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
	public  static final int RequestPermissionCode_StorageCamera  = 11 ;
	private String nip, nama_pegawai, jabatan, golongan, unit, password, email, kirim_versi;
	public static final String my_shared_preferences = "my_shared_preferences";
	public static final String session_status_level1 = "session_status_level1";
	public static final String session_status_level2 = "session_status_level2";
	public String pesan, warning, versiygbaru, linkupdate;
	private Dialog dialogDownload;
	private ProgressBar progressBar;
	private TextView txtProgress;
	boolean doubleBackToExitPressedOnce = false;
	private final ExecutorService executor = Executors.newSingleThreadExecutor();
	private final Handler mainHandler = new Handler(Looper.getMainLooper());
	PermissionHelper permissionHelper;
	ImageView img_showpass_login1, img_showpass_login2, gmbar_loading_login;
	EditText edit_pass, edit_nip;
	TextView cek_versi_apk, develpe, txt_signup, txt_bantuan;

	SharedPreferences sharedpreferences;
	Boolean session_1 = false;
	Boolean session_2 = false;
	Animation animAlpha, animkekiri, animkekanan;
	RelativeLayout frame_loading_login;
	CardView btnlogin;
	private Uri fileUri;
	String info = "© ESPPD 2017-2026,\nCrafted with ❤ by ITISI - RSUD dr. Soedono";
	String versinya = null;
	@SuppressLint("SetTextI18n")
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
		setContentView(R.layout.login_activity);
		Permission_AksesCameradanStorage();
		//getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		permissionHelper = new PermissionHelper(this);

        try {
            versinya = getPackageManager()
                    .getPackageInfo(getPackageName(), 0)
                    .versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }

        animAlpha 	= AnimationUtils.loadAnimation(this, R.anim.anim_menghilang);
		animkekiri 	= AnimationUtils.loadAnimation(this, R.anim.anim_kekiri);
		animkekanan = AnimationUtils.loadAnimation(this, R.anim.anim_kekanan);
		gmbar_loading_login = findViewById(R.id.gmbar_loading_login);
		frame_loading_login = findViewById(R.id.frame_loading_login);
		edit_pass 			= findViewById(R.id.edit_pass);
		edit_nip 			= findViewById(R.id.edit_nip);
		cek_versi_apk		= findViewById(R.id.txt_version);
		btnlogin 			= findViewById(R.id.btnlogin);
		txt_signup 			= findViewById(R.id.txt_signup);
		txt_bantuan 		= findViewById(R.id.txt_bantuan);

		develpe				= findViewById(R.id.txt_creator);
		img_showpass_login1 = findViewById(R.id.img_showpass_login1);
		img_showpass_login2 = findViewById(R.id.img_showpass_login2);
		develpe.setText(info);

//		String versi = "3.0"; //JANGAN LUPA VERSI INI DIRUBAH SESUAI UPDATENYA
		cek_versi_apk.setText("V. " + versinya);
//		kirim_versi = versinya; //cek_versi_apk.getText().toString();
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
		Log.e(TAG, String.valueOf(session_1));
		if (session_2) {
			Intent intent = new Intent(Login_Activity.this, MainActivityUtama.class);
			intent.putExtra(TAG_NIP, nip);
			intent.putExtra(TAG_NAMA_PEGAWAI, nama_pegawai);
			intent.putExtra(TAG_JABATAN, jabatan);
			intent.putExtra(TAG_GOLONGAN, golongan);
			intent.putExtra(TAG_UNIT, unit);
			intent.putExtra(TAG_PASSWORD, password);
			intent.putExtra(TAG_EMAIL, email);
			intent.putExtra(versi, versinya);
			finish();
			startActivity(intent);
		}else if (session_1){
			Toast.makeText(Login_Activity.this, "Aplikasi e-SPPD khusus admin sementara tidak bisa di gunakan\nSilahkan Hubungi TIM IT untuk Hak Akses",
					Toast.LENGTH_LONG).show();
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

		getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {

				if (doubleBackToExitPressedOnce) {
					setEnabled(false);   // penting agar tidak loop
					getOnBackPressedDispatcher().onBackPressed();
					return;
				}

				doubleBackToExitPressedOnce = true;
				Toast.makeText(Login_Activity.this,
						"Tekan tombol kembali [2x] untuk keluar aplikasi.",
						Toast.LENGTH_SHORT).show();

				new Handler(Looper.getMainLooper()).postDelayed(
						() -> doubleBackToExitPressedOnce = false,
						2000
				);
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
		Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		dialog.getWindow().setLayout(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT
		);
	}
	public void validasi_nip(EditText editText) {

		edit_nip.setError(null);

		if (edit_nip.length() == 0) {
			editText.setError(
					Html.fromHtml(
							"NIP Tidak Boleh Kosong",
							Html.FROM_HTML_MODE_LEGACY
					)
			);
		}
	}

	public void validasi_pass(EditText editText) {

		edit_pass.setError(null);

		if (edit_pass.length() == 0) {
			editText.setError(
					Html.fromHtml(
							"Password Tidak Boleh Kosong",
							Html.FROM_HTML_MODE_LEGACY
					)
			);
		}
	}
	private boolean terkoneksi_roaming(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting();

	}
	public void bantuan (View view){
		lupa_password();
//		view.startAnimation(animkekanan);
		view.startAnimation(animAlpha);
	}


	public void signup (View view){
//		view.startAnimation(animkekiri);
		view.startAnimation(animAlpha);
		Intent i = new Intent(Login_Activity.this, Register_Activity.class);
		startActivity(i);
//		finish();
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
			cekVersiDulu();
		}


	}

	private void cekVersiDulu() {

		showLoading();
		String versiApk = versinya;
		executor.execute(() -> {

			String code = "0";
			Java_Connection jc = new Java_Connection();

			try {

				HashMap<String, String> params = new HashMap<>();
				params.put("versi_apk", versiApk);

				Log.d("CEK_VERSI", "Request dimulai");

				String response = jc.sendPostRequest(
						Koneksi.CEK_VERSI,
						params
				);

				if (response != null) {

					Log.d("CEK_VERSI", "RESPON = " + response);

					JSONObject json = new JSONObject(response);

					int kode = json.getInt(TAG_CODE);

					pesan = json.optString(TAG_PESAN_CEK, "");
					warning = json.optString(TAG_WARNING, "");
					versiygbaru = json.optString(TAG_VERSIBARU, "");
					linkupdate = json.optString(TAG_LINK, "");

					code = String.valueOf(kode);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

			String finalCode = code;

			mainHandler.post(() -> {

				hideLoading();

				switch (finalCode) {

					case "1": // versi terbaru
						loginAPK();
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

			});

		});

	}

	private void jikainfo(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(warning)
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
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

	private void info_download(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle(warning)
				.setCancelable(false)
				.setIcon(R.drawable.ic_download)
				.setPositiveButton("✅ Update",
						(dialog, id) -> {
							dialog.dismiss();
							Intent in = new Intent();
							in.setAction(Intent.ACTION_VIEW);
							in.addCategory(Intent.CATEGORY_BROWSABLE);
							in.setData(Uri.parse(linkupdate));
							startActivity(in);
						})
				.setNeutralButton("Download",
						(dialog, id) -> {
							try {
								String linkupdate = Koneksi.download_apk + "e-Sppd.v" + URLEncoder.encode(versiygbaru, "UTF-8")+".apk";
								downloadApk(linkupdate);
							} catch (Exception ex) {
								ex.getMessage();
							}
						})
				.setNegativeButton("❌ Nanti",
						(dialog, id) -> {
							dialog.dismiss();
							hideLoading();
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@SuppressLint("SetTextI18n")
    public void downloadApk(String urlDownload) {

		showDownloadDialog();

		executor.execute(() -> {

			String error = null;

			try {

				URL url = new URL(urlDownload);
				URLConnection connection = url.openConnection();
				connection.connect();

				int lengthOfFile = connection.getContentLength();

				ContentValues values = new ContentValues();
				values.put(MediaStore.MediaColumns.DISPLAY_NAME,
						"e-Sppd.v" + versiygbaru + ".apk");
				values.put(MediaStore.MediaColumns.MIME_TYPE,
						"application/vnd.android.package-archive");

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
					values.put(MediaStore.MediaColumns.RELATIVE_PATH,
							Environment.DIRECTORY_DOWNLOADS);
				}

				fileUri = getContentResolver().insert(
						MediaStore.Files.getContentUri("external"), values);

				if (fileUri == null) {
					error = "Gagal membuat file download";
					throw new Exception(error);
				}

				InputStream input = new BufferedInputStream(url.openStream());
				OutputStream output = getContentResolver().openOutputStream(fileUri);

				byte[] data = new byte[1024];
				int count;
				long total = 0;

				while ((count = input.read(data)) != -1) {

					total += count;

					int progress = 0;

					if (lengthOfFile > 0) {
						progress = (int) ((total * 100) / lengthOfFile);
					}

					int finalProgress = progress;

					mainHandler.post(() -> {

						if (progressBar != null)
							progressBar.setProgress(finalProgress);

						if (txtProgress != null)
							txtProgress.setText(finalProgress + "%");

					});

					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();

			} catch (Exception e) {
				error = e.toString();
			}

			String finalError = error;

			mainHandler.post(() -> {

				if (dialogDownload != null && dialogDownload.isShowing()) {
					dialogDownload.dismiss();
				}

				if (finalError == null) {
					String pesan =
							"Download E-SPPD V" + versiygbaru + " berhasil.\n\n" +
									"File tersimpan di folder Download.\n" +
									"Silakan install ulang aplikasi.";

					showprogress_download(pesan);
				} else {
					showErrorSnackbar("Download gagal: " + finalError);
				}
			});
		});
	}
	private void showErrorSnackbar(String message) {
		View rootView = findViewById(android.R.id.content);
		Snackbar.make(rootView, message, Snackbar.LENGTH_LONG)
				.setAction("OK", v -> {})
				.show();
	}

	private void showprogress_download(String a) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(a)
				.setTitle("Informasi")
				.setCancelable(false)
				.setIcon(R.drawable.ic_warning_black)
				.setPositiveButton("Ok",
						(dialog, id) -> {
							dialog.dismiss();
							finish();
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void loginAPK() {

		showLoading();

		String nippegawai  = edit_nip.getText().toString().trim();
		String passpegawai = edit_pass.getText().toString().trim();
		String versiApk    = versinya;

		executor.execute(() -> {

			String jawaban_json;

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
					jawaban_json = "Gagal terhubung ke server";
				} else {

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

							mainHandler.post(() -> {
								String pesan = "Hubungi administrator untuk Hak Akses Aplikasi e-SPPD!";
								info(pesan);
							});

						} else if (cek_level == 2) {

							mainHandler.post(() -> {

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
										MainActivityUtama.class
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

							});

						}

						jawaban_json = jsonObjectNya.getString(TAG_PESAN2);

					} else {
						jawaban_json = jsonObjectNya.getString(TAG_PESAN2);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				jawaban_json = e.toString();
			}

			String finalJawaban = jawaban_json;

			mainHandler.post(() -> {

				hideLoading();

                Toast.makeText(
                        getApplicationContext(),
                        finalJawaban,
                        Toast.LENGTH_LONG
                ).show();

            });

		});

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
				.setIcon(R.drawable.ic_warning_black)
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

	private void infodialogback() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Keluar dari login aplikasi ?");
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setPositiveButton("Ya", (dialog, which) -> {
			Login_Activity.this.finish();
			finish();
		});
		ad.setNegativeButton("Tidak", (dialog, which) ->
				dialog.dismiss());
		ad.show();
	}

	public void onRequestPermissionsResult(int RC, @NonNull String[] per, @NonNull int[] PResult) {

        super.onRequestPermissionsResult(RC, per, PResult);
        if (RC == RequestPermissionCode_StorageCamera) {
			if ((PResult.length > 0) && (PResult[0] == PackageManager.PERMISSION_DENIED)) {
				makeText(Login_Activity.this, "Diperlukan ijin akses lokasi penyimpanan data dan akses galery!", LENGTH_LONG).show();
			}
		}
	}

	public void Permission_AksesCameradanStorage(){

		if ((ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) &&
				(ActivityCompat.shouldShowRequestPermissionRationale(Login_Activity.this, Manifest.permission.CAMERA))) {

			makeText(Login_Activity.this, "Diperlukan ijin akses lokasi penyimpanan data dan akses kamera!", LENGTH_LONG).show();
			ActivityCompat.requestPermissions(Login_Activity.this,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
		} else {
			ActivityCompat.requestPermissions(Login_Activity.this,
					new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, RequestPermissionCode_StorageCamera);
		}
	}

	public void showLoading() {
		frame_loading_login.setVisibility(View.GONE);
		edit_nip.setEnabled(true);
		edit_pass.setEnabled(true);
		btnlogin.setEnabled(true);
		txt_signup.setEnabled(true);
		txt_bantuan.setEnabled(true);
	}

	public void hideLoading() {
		frame_loading_login.setVisibility(View.VISIBLE);
		edit_nip.setEnabled(false);
		edit_pass.setEnabled(false);
		btnlogin.setEnabled(false);
		txt_signup.setEnabled(false);
		txt_bantuan.setEnabled(false);

		Glide.with(Login_Activity.this)
				.load(R.drawable.loading_blue)
				.into(gmbar_loading_login);
	}
	public void showDownloadDialog() {

		dialogDownload = new Dialog(this);
		dialogDownload.setContentView(R.layout.dialog_download_progress);

		progressBar = dialogDownload.findViewById(R.id.progressBar);
		txtProgress = dialogDownload.findViewById(R.id.txtProgress);

		progressBar.setProgress(0);
		txtProgress.setText("0%");

		dialogDownload.setCancelable(false);
		dialogDownload.show();
	}
}
