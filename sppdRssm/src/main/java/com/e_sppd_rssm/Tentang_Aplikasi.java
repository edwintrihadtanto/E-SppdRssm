package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.e_sppd.rssm.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import bantuan_tutorial.Tutorial_Ke2;
import koneksi.Java_Connection;
import koneksi.Koneksi;
public class Tentang_Aplikasi extends AppCompatActivity {
	private TextView nip_lokal, tanggl_kritik, text_version, judul;
	private CardView feedback, tutorial;
	private final Handler handler = new Handler();
	//private static final String simpan_kritik = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/simpan_kritik.php";
	String info = "© ESPPD 2017-2024,\nDeveloped by I.T.I.S.I - RSSM";
	@SuppressLint("SetTextI18n")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tentang_apk);
		handler.postDelayed(runnable, 1000);

		nip_lokal 		= findViewById(R.id.nip_lokal);
		tutorial 		= findViewById(R.id.tutorial);
		feedback 		= findViewById(R.id.feedback);
		tanggl_kritik 	= findViewById(R.id.tgl_kritik);
		text_version 	= findViewById(R.id.text5);
		judul 			= findViewById(R.id.text4);
		judul.setText(R.string.suatu_sistem_berbasis_mobile_aplikasi_surat_perintah_perjalanan_dinas_sppd_pada_rumah_sakit_dr_soedomo_madiun);
		Bundle b = getIntent().getExtras();
        assert b != null;
        String transferan_nip = b.getString("transfer_nip");
		if (transferan_nip == null){
			Toast.makeText(Tentang_Aplikasi.this, "NIP : Null",
					Toast.LENGTH_LONG).show();
		}else{
			nip_lokal.setText(transferan_nip);
			//nip_lokal.setVisibility(View.VISIBLE);
		}
		String versi = b.getString("versi");

		text_version.setText("versi " + versi);
		feedback.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            popUp();
        });

		tutorial.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            Intent i;
            i = new Intent(Tentang_Aplikasi.this, Tutorial_Ke2.class);
            startActivity(i);
        });

	}

	private void popUp() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up);
		CardView cancel = dialog.findViewById(R.id.btn_cncl);
		CardView save 	= dialog.findViewById(R.id.btn_simpan);

		dialog.show();

		cancel.setOnClickListener(v -> {
            dialog.dismiss();
            Toast.makeText(Tentang_Aplikasi.this, "Batal Kritik dan Saran",
                    Toast.LENGTH_LONG).show();
        });

		save.setOnClickListener(v -> {
            EditText editkritik = dialog.findViewById(R.id.editkritik);
            dialog.dismiss();
            String nip = nip_lokal.getText().toString().trim();
            String feedback = editkritik.getText().toString().trim();
            String tgl_kritik = tanggl_kritik.getText().toString().trim();

            if (feedback.isEmpty()) {
                Toast.makeText(Tentang_Aplikasi.this,
                        "Maaf Anda Belum Memberikan Kritik dan Saran",
                        Toast.LENGTH_LONG).show();
            } else if (feedback.length() > 150) {
                Toast.makeText(Tentang_Aplikasi.this,
                        "Maks. Panjang Karakter 150", Toast.LENGTH_LONG)
                        .show();
            } else {
                simpan_kritik(nip, feedback, tgl_kritik);
            }

        });
	}

	private final Runnable runnable = new Runnable() {

		@SuppressLint({"SimpleDateFormat", "SetTextI18n"})
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();

			SimpleDateFormat tgl_skrng = new SimpleDateFormat("yyyy/M/d");
			SimpleDateFormat jam_skrng = new SimpleDateFormat("h:m:s");
			// SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
			String strdate_tgl = tgl_skrng.format(c1.getTime());
			String strdate_jam = jam_skrng.format(c1.getTime());

			tanggl_kritik.setText(strdate_tgl + " " + strdate_jam);

			handler.postDelayed(this, 1000);
		}

	};

	private void simpan_kritikXX(String nip, String feedback, String tgl_kritik) {

		@SuppressLint("StaticFieldLeak")
		class Input_Baru extends AsyncTask<String, Void, String> {

			ProgressDialog tampilloading;
			final Java_Connection ruc = new Java_Connection();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				tampilloading = ProgressDialog.show(Tentang_Aplikasi.this, "",
						"Sedang Mengirim Kritik dan Saran...", true, true);
				tampilloading.setCancelable(false);
			}

			@Override
			protected void onPostExecute(String s) {
				super.onPostExecute(s);
				tampilloading.dismiss();
				Toast.makeText(Tentang_Aplikasi.this, "Terima Kasih Atas Kritik dan Saran Anda",
						Toast.LENGTH_LONG).show();
			}

			@Override
			protected String doInBackground(String... params) {
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("nip", params[0]);
				data.put("feedback", params[1]);
				data.put("tgl_kritik", params[2]);

				String result = null;
				if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
					result = ruc.sendPostRequest(Koneksi.simpan_kritik, data);
				}
				return result;

			}
		}

		Input_Baru ru = new Input_Baru();
		ru.execute(nip, feedback, tgl_kritik);
	}

	private void simpan_kritik(String nip, String feedback, String tgl_kritik) {

		@SuppressLint("StaticFieldLeak")
		class Input_Baru extends AsyncTask<String, Void, String> {

			ProgressDialog tampilloading;
			final Java_Connection ruc = new Java_Connection();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				tampilloading = ProgressDialog.show(
						Tentang_Aplikasi.this,
						"",
						"Sedang Mengirim Kritik dan Saran...",
						true,
						false
				);
			}

			@Override
			protected String doInBackground(String... params) {

				HashMap<String, String> data = new HashMap<>();
				data.put("nip", params[0]);
				data.put("feedback", params[1]);
				data.put("tgl_kritik", params[2]);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    return ruc.sendPostRequest(Koneksi.simpan_kritik, data);
                }
                return null;
            }

			@Override
			protected void onPostExecute(String result) {

				tampilloading.dismiss();

				if (result == null || result.isEmpty()) {
					Toast.makeText(
							Tentang_Aplikasi.this,
							"Server tidak merespon.\nCoba lagi nanti.",
							Toast.LENGTH_LONG
					).show();
					return;
				}

				// 🔍 LOG RAW RESPONSE
				Log.d("KRITIK_RESPONSE", result);

				// Kalau server balikin HTML (WAF / error)
				if (result.startsWith("<")) {
					Toast.makeText(
							Tentang_Aplikasi.this,
							"Gagal mengirim kritik.\nTerjadi gangguan server.",
							Toast.LENGTH_LONG
					).show();
					return;
				}

				// Kalau server balikin JSON
				try {
					JSONObject json = new JSONObject(result);

					boolean success = json.optBoolean("success", false);
					String message = json.optString("message", "");

					if (success) {
						Toast.makeText(
								Tentang_Aplikasi.this,
								"Terima kasih atas kritik dan saran Anda 🙏",
								Toast.LENGTH_LONG
						).show();
					} else {
						Toast.makeText(
								Tentang_Aplikasi.this,
								message.isEmpty()
										? "Gagal menyimpan kritik"
										: message,
								Toast.LENGTH_LONG
						).show();
					}

				} catch (Exception e) {
					// Kalau bukan JSON
					Toast.makeText(
							Tentang_Aplikasi.this,
							"Kritik terkirim, tetapi respon server tidak dikenali.",
							Toast.LENGTH_LONG
					).show();
				}
			}
		}

		new Input_Baru().execute(nip, feedback, tgl_kritik);
	}

	public void kembali_activity(View view){
		super.onBackPressed();
	}
}
