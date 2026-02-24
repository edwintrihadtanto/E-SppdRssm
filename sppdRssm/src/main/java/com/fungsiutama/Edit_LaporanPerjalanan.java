package com.fungsiutama;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

//import koneksi.JSONParser;
import koneksi.Java_Connection;
import koneksi.Koneksi;
public class Edit_LaporanPerjalanan extends AppCompatActivity {
	private static final String TAG = "LAPORAN";
	private final Handler handler = new Handler();
	private ProgressDialog ProgressDialog1;

	private EditText hasilRapat, hasilMasalah, hasilSaran, hasilLainnya;

	int tahun, bulan, hari;
//	JSONParser classJsonParser = new JSONParser();
	private static final String TAG_BERHASIL 	= "success";
	private static final String TAG_PESAN 		= "message";
	TextView noSPT, tglBerangkat, tglSampai, lamaperjalanan, nama_petugas, kotatujuan, acratujuan, acara, tglSPT, nipTTD, tgl_ttd;
	String nomor_spt, nip, nama, lama_perj, tgl_brngkt, tgl_kembali, daerah_tujuan, instansi_yg_dikunjungi, acaraSPPD, tgl_surat_masuk, stsLaporan, tglSekarang;
	String idLaporanSPPD = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_laporansppd);
		handler.postDelayed(runnable, 1000);

		nipTTD 					= findViewById(R.id.viewnipTTD);
		tgl_ttd 				= findViewById(R.id.txt_kota_tanggal);
		noSPT 					= findViewById(R.id.edit_dsr_sppt_1);
		tglSPT 					= findViewById(R.id.edit_tgl_dsr_sppt_);
		tglBerangkat 			= findViewById(R.id.edit_tgl_pelaksanaan_awal);
		tglSampai 				= findViewById(R.id.edit_tgl_pelaksanaan_sampai);
		lamaperjalanan 			= findViewById(R.id.edit_lama_tgl_pelaksanaan);
		nama_petugas 			= findViewById(R.id.edit_tampil_nip_spt);
		kotatujuan 				= findViewById(R.id.edit_daerah_tujuan);
		acratujuan 				= findViewById(R.id.edit_ins_yg_kunjungi);
		acara 					= findViewById(R.id.edit_acara);
		hasilRapat 				= findViewById(R.id.edit_hsl_rapat);
		hasilMasalah 			= findViewById(R.id.edit_masalah);
		hasilSaran 				= findViewById(R.id.edit_saran);
		hasilLainnya 			= findViewById(R.id.edit_lain_lain);
        Button simpanlaporan 	= findViewById(R.id.btnSimpanLaporan);
		Button hapuslaporan 	= findViewById(R.id.btnHapusLaporan);

		final Calendar c 	= Calendar.getInstance();
		tahun 				= c.get(Calendar.YEAR);
		bulan 				= c.get(Calendar.MONTH);
		hari 				= c.get(Calendar.DAY_OF_MONTH);

		//Bundle b = getIntent().getExtras();
		//String transfer_nip = b.getString("transfer_nip");
		// nip_lokal.setText("NIP. "+transfer_nip);
		Tampil_data();
//		String cek_status_laporan_petugas = status_laporan_petugas.getText().toString();
		if (stsLaporan.equalsIgnoreCase("BELUM")) {
			showSnackbar("Silahkan Isi Data Laporan Perjalanan Dinas Anda");
		}

		simpanlaporan.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if ((idLaporanSPPD.isEmpty()) && (stsLaporan.contains("BELUM"))){
                pertanyaanSIMPAN();
            }else {
                pertanyaan_edit_laporan();
            }
        });

		hapuslaporan.setOnClickListener(v -> pertanyaanHAPUS());
	}

	@SuppressLint("SetTextI18n")
	public void Tampil_data() {
		nomor_spt 					= getIntent().getStringExtra("nomor_spt");
		nip 						= getIntent().getStringExtra("nip");
		nama 						= getIntent().getStringExtra("nama_pegawai");
		lama_perj 					= getIntent().getStringExtra("lama_perj");
		tgl_brngkt 					= getIntent().getStringExtra("tgl_brngkt");
		tgl_kembali 				= getIntent().getStringExtra("tgl_kembali");
		daerah_tujuan 				= getIntent().getStringExtra("tempat_tujuan");
		instansi_yg_dikunjungi 		= getIntent().getStringExtra("surat_masuk_dari");
		acaraSPPD 					= getIntent().getStringExtra("maksud_perj");
		tgl_surat_masuk 			= getIntent().getStringExtra("tgl_surat_masuk");
		stsLaporan 					= getIntent().getStringExtra("status_laporan_petugas");

		/*
		String nip_pembuatlaporanperj 		= getIntent().getStringExtra("nip_pembuatlaporanperj");
		String nomor_spt_laporanperj 		= getIntent().getStringExtra("nomor_spt_laporanperj");
		String hasil_pertemuan 				= getIntent().getStringExtra("hasil_pertemuan");
		String masalah 						= getIntent().getStringExtra("masalah");
		String saran 						= getIntent().getStringExtra("saran");
		String lain_lain 					= getIntent().getStringExtra("lain_lain");
		String hasilTglLaporan 				= getIntent().getStringExtra("tgl_pembuatan_laporan");
		*/

		noSPT.setText(nomor_spt);
		tglSPT.setText(tgl_surat_masuk);
		nama_petugas.setText(nama);
		tglBerangkat.setText(tgl_brngkt);
		tglSampai.setText(tgl_kembali);
		lamaperjalanan.setText("Waktu : " +lama_perj + " Hari");
		kotatujuan.setText(daerah_tujuan);
		acratujuan.setText(instansi_yg_dikunjungi);
		acara.setText(acaraSPPD);
		nipTTD.setText("NIP : "+ nip);

		new LoadLaporan().execute();
//		if (hasil_pertemuan.contains("null")){
//			hasilRapat.setText("");
//		}else{
//			hasilRapat.setText(hasil_pertemuan);
//			hasilMasalah.setText(masalah);
//			hasilSaran.setText(saran);
//			hasilLainnya.setText(lain_lain);
//		}
	}
	private void showSnackbar(String message) {

		View rootView = findViewById(android.R.id.content);
		Snackbar.make(rootView, message, Snackbar.LENGTH_INDEFINITE)
				.setAction("OK", v -> {})
				.show();
	}
	private void pertanyaanSIMPAN() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Simpan laporan perjalanan dinas ?");
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setPositiveButton("Simpan", (dialog, which) -> {
			//	String cek_lain_lain = edit_lain_lain.getText().toString();
			if (hasilRapat.getText().toString().isEmpty()) {
				String pesan = "Form masih kosong";
				show_alert(pesan);
			} else {
				new Simpan_Laporan_Perjalanan_Dinas().execute();
			}

		});
		ad.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
		ad.show();
	}
	private void pertanyaanHAPUS() {//
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Peringatan");
		ad.setMessage("Hapus laporan perjalanan dinas ?");
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setPositiveButton("Simpan", (dialog, which) -> {
			//	String cek_lain_lain = edit_lain_lain.getText().toString();
			if (idLaporanSPPD.isEmpty()) {
				String pesan = "Id Laporan SPPD tidak diketahui!";
				show_alert(pesan);
			} else {
				new Hapus_Laporan_Perj_Dinas().execute();
			}

		});
		ad.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
		ad.show();
	}
	private void pertanyaan_edit_laporan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Simpan Perubahan Laporan Perjalanan Dinas ?");
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setPositiveButton("Simpan", (dialog, which) -> {
			if (hasilRapat.getText().toString().isEmpty()) {
				String pesan = "Isian laporan perjalanan dinas tidak boleh kosong";
				showSnackbar(pesan);
			} else {
				dialog.dismiss();
				new Edit_Laporan_Perj_Dinas().execute();
			}

		});
		ad.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
		ad.show();
	}
	@SuppressLint("StaticFieldLeak")
	public class LoadLaporan extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(Edit_LaporanPerjalanan.this);
			ProgressDialog1.setMessage("Memuat data laporan...");
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
		@Override
		protected JSONObject doInBackground(Void... voids) {

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("noSPT", nomor_spt.trim());
				params.put("nip", nip.trim());

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.load_data_laporan_petugas,
						params
				);

				if (response == null) return null;

				Log.d("LOAD_LAPORAN", response);

				return new JSONObject(response);

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}

		@SuppressLint("SetTextI18n")
        @Override
		protected void onPostExecute(JSONObject json) {

			if (ProgressDialog1 != null && ProgressDialog1.isShowing()) {
				ProgressDialog1.dismiss();
			}

			if (json == null) {
				show_alert("Gagal memuat data laporan");
				return;
			}

			try {
				int success = json.getInt("success");

				if (success == 1) {

					JSONObject data = json.getJSONObject("data");
					idLaporanSPPD = data.getString("idLaporan");
					hasilRapat.setText(data.getString("hasil_pertemuan"));
					hasilMasalah.setText(data.getString("masalah"));
					hasilSaran.setText(data.getString("saran"));
					hasilLainnya.setText(data.getString("lain_lain"));

					String tgl = data.getString("tgl_pembuatan_laporan");
					tglSekarang = tgl;
					tgl_ttd.setText("Madiun, " + tgl);

					Log.d(TAG, String.valueOf(data));

				} else {
					show_alert(json.getString("message"));
				}

			} catch (Exception e) {
				e.printStackTrace();
				show_alert("Terjadi kesalahan parsing data");
			}
		}
	}
	@SuppressLint("StaticFieldLeak")
    public class Simpan_Laporan_Perjalanan_Dinas extends AsyncTask<Void, Void, String> {
		Java_Connection jc = new Java_Connection();
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(
					Edit_LaporanPerjalanan.this);
			ProgressDialog1.setMessage("Loading ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {
			nomor_spt 					= getIntent().getStringExtra("nomor_spt");
			nip 						= getIntent().getStringExtra("nip");
			nama 						= getIntent().getStringExtra("nama_pegawai");
			lama_perj 					= getIntent().getStringExtra("lama_perj");
			tgl_brngkt 					= getIntent().getStringExtra("tgl_brngkt");
			tgl_kembali 				= getIntent().getStringExtra("tgl_kembali");
			daerah_tujuan 				= getIntent().getStringExtra("tempat_tujuan");
			instansi_yg_dikunjungi 		= getIntent().getStringExtra("surat_masuk_dari");
			acaraSPPD 					= getIntent().getStringExtra("maksud_perj");
			tgl_surat_masuk 			= getIntent().getStringExtra("tgl_surat_masuk");
			stsLaporan 					= getIntent().getStringExtra("status_laporan_petugas");

			try {
				HashMap<String, String> params = new HashMap<>();

				params.put("ambil_nomor_spt", nomor_spt.trim());
				params.put("nip", nip.trim());
				params.put("hasil_pertemuan", hasilRapat.getText().toString().trim());
				params.put("masalah", hasilMasalah.getText().toString().trim());
				params.put("saran", hasilMasalah.getText().toString().trim());
				params.put("lain_lain", hasilLainnya.getText().toString().trim());
				params.put("tgl_pembuatan_laporan", tglSekarang.trim());

				String response = jc.sendPostRequest(
						Koneksi.simpanupdate_laporan_petugas,
						params
				);

				if (response == null) {
					return null;
				}

				Log.d("Info", response);

				JSONObject json = new JSONObject(response);
//				int berhasil = json.getInt(TAG_BERHASIL);

                return json.getString(TAG_PESAN);

            } catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String hasil) {

			if (ProgressDialog1 != null && ProgressDialog1.isShowing()) {
				ProgressDialog1.dismiss();
			}

			Log.e(TAG, "Respon Dari Server Pembuatan Laporan ::: " + hasil);

			if (hasil != null) {

				String errorJson = "org.json.JSONException: No value for sukses";
				if (hasil.contains(errorJson)) {

					String pesan =
							"Koneksi Terputus\n" +
									"Pastikan Koneksi Data Internet Terhubung dan Lancar !!!";
					show_alert(pesan);

				} else {
					Toast.makeText(
							Edit_LaporanPerjalanan.this,
							hasil,
							Toast.LENGTH_LONG
					).show();
					finish();
				}

			} else {
				show_alert(
						"Koneksi Terputus\nPastikan Internet Aktif"
				);
			}
		}
	}
	@SuppressLint("StaticFieldLeak")
    public class Edit_Laporan_Perj_Dinas extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(
					Edit_LaporanPerjalanan.this);
			ProgressDialog1.setMessage("Sedang memperbarui laporan..");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {
				HashMap<String, String> params = new HashMap<>();

				params.put("nip_pembuatlaporanperj", getIntent().getStringExtra("nip_pembuatlaporanperj"));
				params.put("ambil_nomor_spt", nomor_spt.trim());
				params.put("nip", nip.trim());
				params.put("hasil_pertemuan", hasilRapat.getText().toString().trim());
				params.put("masalah", hasilMasalah.getText().toString().trim());
				params.put("saran", hasilSaran.getText().toString().trim());
				params.put("lain_lain", hasilLainnya.getText().toString().trim());
				params.put("tgl_pembuatan_laporan", tglSekarang.trim());

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.simpanupdate_laporan_petugas,
						params
				);

				if (response == null) {
					return null;
				}

				Log.d(TAG, response);

				JSONObject json = new JSONObject(response);
//				int berhasil = json.getInt(TAG_BERHASIL);

                return json.getString(TAG_PESAN);

            } catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String hasil) {

			if (ProgressDialog1 != null && ProgressDialog1.isShowing()) {
				ProgressDialog1.dismiss();
			}

			if (hasil != null) {
				show_alert2(hasil);
			}
		}
	}
	@SuppressLint("StaticFieldLeak")
	public class Hapus_Laporan_Perj_Dinas extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(Edit_LaporanPerjalanan.this);
			ProgressDialog1.setMessage("Sedang menghapus laporan..");
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {
				HashMap<String, String> params = new HashMap<>();

				params.put("idlaporan", idLaporanSPPD.trim());
				params.put("noSPT", nomor_spt.trim());
				params.put("nip", nip.trim());

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.hapus_laporan_petugas,
						params
				);

				if (response == null) {
					return "Server tidak merespon";
				}

				JSONObject json = new JSONObject(response);

//				boolean success = json.getBoolean("success");   // ✔ sesuai backend baru

                return json.getString("message");

			} catch (Exception e) {
				e.printStackTrace();
				return e.toString();
			}
		}

		@Override
		protected void onPostExecute(String hasil) {

			if (ProgressDialog1 != null && ProgressDialog1.isShowing()) {
				ProgressDialog1.dismiss();
			}

			if (hasil != null) {
				show_alert2(hasil);
			}
		}
	}

	private void show_alert2(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setCancelable(false);
		ad.setPositiveButton("Ok", (dialog, which) -> {
            dialog.dismiss();
            finish();
        });

		ad.show();
	}
	private void show_alert(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setCancelable(false);
		ad.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
		
		ad.show();
	}
	private final Runnable runnable = new Runnable() {

		@SuppressLint({"SimpleDateFormat", "SetTextI18n"})
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();

//			SimpleDateFormat tgl_skrng = new SimpleDateFormat("d MMMM yyyy");
//			SimpleDateFormat tgl_skrng2 = new SimpleDateFormat("yyyy-M-d");
			SimpleDateFormat tgl_skrng2 = new SimpleDateFormat("d-M-yyyy");
//			String strtgl_skrng = tgl_skrng.format(c1.getTime());
			String strtgl_skrng2 = tgl_skrng2.format(c1.getTime());
			tglSekarang = strtgl_skrng2;
			tgl_ttd.setText("Madiun, "+strtgl_skrng2);
			handler.postDelayed(this, 1000);
		}

	};
}
