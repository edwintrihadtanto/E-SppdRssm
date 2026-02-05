package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

//import koneksi.JSONParser;
import koneksi.Java_Connection;
import koneksi.Koneksi;
public class Pembuatan_Lap_Setelah_Perj_Dinas extends AppCompatActivity {
	private static final String TAG = "Lap. Perjalanan Dinas";
	private Handler handler = new Handler();
	private TextView nip_lokal, status_laporan_petugas, tgl_sembunyi;
	private ProgressDialog ProgressDialog1;

	private EditText edit_dsr_sppt_1, edit_tgl_pelaksanaan_awal,
			edit_tgl_pelaksanaan_sampai, edit_lama_tgl_pelaksanaan,
			edit_tampil_nama_spt, edit_daerah_tujuan, edit_ins_yg_kunjungi,
			edit_acara, edit_hsl_rapat, edit_masalah, edit_saran,
			edit_lain_lain, edit_tgl_ttd, edit_tgl_dsr_sppt_;
	private Button simpan_laporan;
	static final int DATE_DIALOG_ID = 1;
	private String[] varBulan = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" };
	int jam, menit, tahun, bulan, hari;
//	JSONParser classJsonParser = new JSONParser();
	private static final String TAG_BERHASIL 	= "success";
	private static final String TAG_PESAN 		= "message";
	private String abc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_lap_set_perj_dinas);
		handler.postDelayed(runnable, 1000);

		nip_lokal 					= findViewById(R.id.tampil_niplagi);
		status_laporan_petugas 		= findViewById(R.id.status_laporan_petugas);
		tgl_sembunyi 				= findViewById(R.id.tgl_sembunyi);
		edit_dsr_sppt_1 			= findViewById(R.id.edit_dsr_sppt_1);
		edit_tgl_pelaksanaan_awal 	= findViewById(R.id.edit_tgl_pelaksanaan_awal);
		edit_tgl_pelaksanaan_sampai = findViewById(R.id.edit_tgl_pelaksanaan_sampai);
		edit_lama_tgl_pelaksanaan 	= findViewById(R.id.edit_lama_tgl_pelaksanaan);
		edit_tampil_nama_spt 		= findViewById(R.id.edit_tampil_nip_spt);
		edit_daerah_tujuan 			= findViewById(R.id.edit_daerah_tujuan);
		edit_ins_yg_kunjungi 		= findViewById(R.id.edit_ins_yg_kunjungi);
		edit_acara 					= findViewById(R.id.edit_acara);
		edit_hsl_rapat 				= findViewById(R.id.edit_hsl_rapat);
		edit_masalah 				= findViewById(R.id.edit_masalah);
		edit_saran 					= findViewById(R.id.edit_saran);
		edit_lain_lain 				= findViewById(R.id.edit_lain_lain);
		edit_tgl_ttd 				= findViewById(R.id.edit_tgl_ttd);
		edit_tgl_dsr_sppt_ 			= findViewById(R.id.edit_tgl_dsr_sppt_);
		simpan_laporan 				= findViewById(R.id.btn_simpan_lap_set_per_dinas);

		//Bundle b = getIntent().getExtras();
		//String transfer_nip = b.getString("transfer_nip");
		// nip_lokal.setText("NIP. "+transfer_nip);
		Tampil_data();

		String cek_status_laporan_petugas = status_laporan_petugas.getText()
				.toString();
		if (cek_status_laporan_petugas.contains("BELUM")) {
			Toast.makeText(Pembuatan_Lap_Setelah_Perj_Dinas.this,
					"Ups... Belum Ada Petugas Yang Membuat Laporan Perjalanan Dinas",
					Toast.LENGTH_LONG).show();
		}

		final Calendar c 	= Calendar.getInstance();
		tahun 				= c.get(Calendar.YEAR);
		bulan 				= c.get(Calendar.MONTH);
		hari 				= c.get(Calendar.DAY_OF_MONTH);

		/*edit_tgl_ttd.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
				return true;
			}
		});*/

		simpan_laporan.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            String hasil_pertemuan 	= getIntent().getStringExtra("hasil_pertemuan");
            String cek_status_laporan_petugas1 = status_laporan_petugas.getText()
                    .toString();
            if ((hasil_pertemuan.contains("null")) && (cek_status_laporan_petugas1.contains("BELUM"))){
                pertanyaan_simpan();
            }else {
                pertanyaan_edit_laporan();
            }
        });
	}

	/*public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}*/
	
	/*public class Simpan_Laporan_Perjalanan_Dinas extends
			AsyncTask<String, String, String> {
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(
					Pembuatan_Lap_Setelah_Perj_Dinas.this);
			ProgressDialog1.setMessage("Loading ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;
			String responseString = null; 		
			String ambil_nomor_spt 	= edit_dsr_sppt_1.getText().toString().trim();
			String nip 				= nip_lokal.getText().toString().trim();
			String hasil 			= edit_hsl_rapat.getText().toString().trim();
			String masalah 			= edit_masalah.getText().toString().trim();
			String saran 			= edit_saran.getText().toString().trim();
			String lain 			= edit_lain_lain.getText().toString().trim();
			String tgl_petugas_ttd 	= tgl_sembunyi.getText().toString().trim();

			try {
				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("ambil_nomor_spt", ambil_nomor_spt));
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair("hasil_pertemuan",hasil));
				parameterNya.add(new BasicNameValuePair("masalah", masalah));
				parameterNya.add(new BasicNameValuePair("saran", saran));
				parameterNya.add(new BasicNameValuePair("lain_lain", lain));
				parameterNya.add(new BasicNameValuePair("tgl_pembuatan_laporan", tgl_petugas_ttd));

				Log.d("Send Data Laporan ", "Go...");
				// kirim data dari user ke script di server
				JSONObject JSONObjectSimpanLaporan = classJsonParser.makeHttpRequest(
						Koneksi.insertupdate_data_laporan_petugas, "POST",parameterNya);

				Log.d("Info", JSONObjectSimpanLaporan.toString());

				// json berhasil
				berhasil = JSONObjectSimpanLaporan.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", JSONObjectSimpanLaporan.toString());
					finish();
					return JSONObjectSimpanLaporan.getString(TAG_PESAN);
				} else {
					Log.d("Failed !!!", JSONObjectSimpanLaporan.getString(TAG_PESAN));
					return JSONObjectSimpanLaporan.getString(TAG_PESAN);

				}
			} catch (JSONException e) {
				responseString = e.toString();
			}	catch (Exception e) {
				responseString = e.toString();
			}
			return responseString;
		}

		*//**
		 * kalau sudah selesai tugas background_nya matikan progressbar_nya
		 * **//*
		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			// matikan progressBar-nya setelah selesai di gunakan
			ProgressDialog1.dismiss();
			Log.e(TAG, "Respon Dari Server Pembuatan Laporan ::: " + url_registrasi_nya);
			
			if (url_registrasi_nya != null) {				
				String pesan = "org.json.JSONException: No value for sukses"; 
				if (url_registrasi_nya.contains(pesan)){
					String aa = "Koneksi Terputus\nPastikan Koneksi Data Internet Terhubung dan Lancar !!!";
					show_alert(aa);
				}else{
					Toast.makeText(Pembuatan_Lap_Setelah_Perj_Dinas.this, url_registrasi_nya, Toast.LENGTH_LONG).show();
				}
				
			}else{
				show_alert(url_registrasi_nya);
			}

		}

	}*/
	public class Simpan_Laporan_Perjalanan_Dinas extends AsyncTask<Void, Void, String> {
		Java_Connection jc = new Java_Connection();
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(
					Pembuatan_Lap_Setelah_Perj_Dinas.this);
			ProgressDialog1.setMessage("Loading ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {
				HashMap<String, String> params = new HashMap<>();

				params.put("ambil_nomor_spt",
						edit_dsr_sppt_1.getText().toString().trim());
				params.put("nip",
						nip_lokal.getText().toString().trim());
				params.put("hasil_pertemuan",
						edit_hsl_rapat.getText().toString().trim());
				params.put("masalah",
						edit_masalah.getText().toString().trim());
				params.put("saran",
						edit_saran.getText().toString().trim());
				params.put("lain_lain",
						edit_lain_lain.getText().toString().trim());
				params.put("tgl_pembuatan_laporan",
						tgl_sembunyi.getText().toString().trim());

				Log.d("Send Data Laporan", "Go...");


				String response = jc.sendPostRequest(
						Koneksi.insertupdate_data_laporan_petugas,
						params
				);

				if (response == null) {
					return null;
				}

				Log.d("Info", response);

				JSONObject json = new JSONObject(response);
				int berhasil = json.getInt(TAG_BERHASIL);

				if (berhasil == 1) {
					return json.getString(TAG_PESAN);
				} else {
					return json.getString(TAG_PESAN);
				}

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
							Pembuatan_Lap_Setelah_Perj_Dinas.this,
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


	private void pertanyaan_simpan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Pastikan Informasi yang Anda Buat Benar ...\n \nSimpan Laporan Perjalanan Dinas Anda ???");
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setPositiveButton("Simpan", (dialog, which) -> {
        //	String cek_lain_lain = edit_lain_lain.getText().toString();
            if (edit_hsl_rapat.getText().toString().isEmpty()) {
                String pesan = "Hasil Rapat Tidak Boleh Kosong";
                show_alert(pesan);
            } else if (edit_tgl_ttd.getText().toString().isEmpty()) {
                String pesan = "Tanggal Tidak Boleh Kosong";
                show_alert(pesan);
            } else {
                new Simpan_Laporan_Perjalanan_Dinas().execute();
            }

        });
		ad.setNegativeButton("Batal", (dialog, which) -> dialog.dismiss());
		ad.show();
	}
	
	private void pertanyaan_edit_laporan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Simpan Laporan Perjalanan Dinas ?");
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setPositiveButton("Simpan", (dialog, which) -> {
            //String cek_lain_lain = edit_lain_lain.getText().toString();
            if (edit_hsl_rapat.getText().toString().isEmpty()) {
                String pesan = "Hasil Rapat Tidak Boleh Kosong";
                show_alert(pesan);
            } else if (edit_tgl_ttd.getText().toString().isEmpty()) {
                String pesan = "Tanggal Tidak Boleh Kosong";
                show_alert(pesan);

            } else {
                dialog.dismiss();
                new Edit_Laporan_Perj_Dinas().execute();
            }

        });
		ad.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		ad.show();
	}

	/*public class Edit_Laporan_Perj_Dinas extends AsyncTask<String, String, String> {
		boolean failure = false;
	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(Pembuatan_Lap_Setelah_Perj_Dinas.this);
			ProgressDialog1.setMessage("Loading Update Laporan ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}
	
		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;
			String responseString = null;
			String nip_pembuatlaporanperj 	= getIntent().getStringExtra("nip_pembuatlaporanperj");
			String ambil_nomor_spt 			= edit_dsr_sppt_1.getText().toString().trim();
			// String ambil_nomor_sppd = getIntent().getStringExtra("nomor_surat_sppd");
			String nip 						= nip_lokal.getText().toString().trim();
			String hasil 					= edit_hsl_rapat.getText().toString().trim();
			String masalah 					= edit_masalah.getText().toString().trim();
			String saran 					= edit_saran.getText().toString().trim();
			String lain 					= edit_lain_lain.getText().toString().trim();
			String tgl_petugas_ttd 			= tgl_sembunyi.getText().toString().trim();

			try {
				// Cocokan parameternya yah 'username ke username dan
				// password ke password
				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("nip_pembuatlaporanperj", nip_pembuatlaporanperj));
				parameterNya.add(new BasicNameValuePair("ambil_nomor_spt", ambil_nomor_spt));
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair("hasil_pertemuan",hasil));
				parameterNya.add(new BasicNameValuePair("masalah", masalah));
				parameterNya.add(new BasicNameValuePair("saran", saran));
				parameterNya.add(new BasicNameValuePair("lain_lain", lain));
				parameterNya.add(new BasicNameValuePair("tgl_pembuatan_laporan", tgl_petugas_ttd));

	//			Log.d("Request ke server!", "dimulai");
	//			JSONParser ambil_classJSONParser = new JSONParser();
	//			// kirim data dari user ke script di server
	//			JSONObject jsonObjectNya = ambil_classJSONParser
	//					.makeHttpRequest(Koneksi.update_data_laporan_petugas, "POST",
	//							parameterNya);
	//
	//			// json response-nya
	//			Log.d("Try Again", jsonObjectNya.toString());

				Log.d("Send Data Laporan ", "Go...");
				JSONObject JSONObjectSimpanLaporan = classJsonParser.makeHttpRequest(
						Koneksi.insertupdate_data_laporan_petugas, "POST",parameterNya);

				Log.d("Info", JSONObjectSimpanLaporan.toString());

				// json berhasil
				berhasil = JSONObjectSimpanLaporan.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", JSONObjectSimpanLaporan.toString());
					//finish();
					return JSONObjectSimpanLaporan.getString(TAG_PESAN);
				} else {
					Log.d("Failed !!!", JSONObjectSimpanLaporan.getString(TAG_PESAN));
					//finish();
					abc = JSONObjectSimpanLaporan.getString(TAG_PESAN);
					return abc;

				}

			} catch (JSONException e) {
				responseString = e.toString();

			} catch (Exception e){
				responseString = e.toString();
			}

			return responseString;

		}

		*//**
		 * kalau sudah selesai tugas background_nya matikan progressbar_nya
		 * **//*
		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			// matikan progressBar-nya setelah selesai di gunakan
			ProgressDialog1.dismiss();
			if (url_registrasi_nya != null) {
				show_alert2(url_registrasi_nya);
			}

		}
	
	}*/

	public class Edit_Laporan_Perj_Dinas
			extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(
					Pembuatan_Lap_Setelah_Perj_Dinas.this);
			ProgressDialog1.setMessage("Loading Update Laporan ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {
				HashMap<String, String> params = new HashMap<>();

				params.put(
						"nip_pembuatlaporanperj",
						getIntent().getStringExtra("nip_pembuatlaporanperj")
				);
				params.put(
						"ambil_nomor_spt",
						edit_dsr_sppt_1.getText().toString().trim()
				);
				params.put(
						"nip",
						nip_lokal.getText().toString().trim()
				);
				params.put(
						"hasil_pertemuan",
						edit_hsl_rapat.getText().toString().trim()
				);
				params.put(
						"masalah",
						edit_masalah.getText().toString().trim()
				);
				params.put(
						"saran",
						edit_saran.getText().toString().trim()
				);
				params.put(
						"lain_lain",
						edit_lain_lain.getText().toString().trim()
				);
				params.put(
						"tgl_pembuatan_laporan",
						tgl_sembunyi.getText().toString().trim()
				);

				Log.d("Send Data Laporan", "Go...");

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.insertupdate_data_laporan_petugas,
						params
				);

				if (response == null) {
					return null;
				}

				Log.d("Info", response);

				JSONObject json = new JSONObject(response);
				int berhasil = json.getInt(TAG_BERHASIL);

				if (berhasil == 1) {
					return json.getString(TAG_PESAN);
				} else {
					return json.getString(TAG_PESAN);
				}

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
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setCancelable(false);
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});

		ad.show();
	}
	private void show_alert(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setCancelable(false);
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {				
					dialog.dismiss();					
			}
		});
		
		ad.show();
	}
	public void Tampil_data() {
		//String id_spt 					= getIntent().getStringExtra("id_spt");
		String nomor_spt 					= getIntent().getStringExtra("nomor_spt");
		//String nomor_sppd 				= getIntent().getStringExtra("nomor_surat_sppd");
		String nip 							= getIntent().getStringExtra("nip");
		String nama 						= getIntent().getStringExtra("nama_pegawai");
		String lama_perj 					= getIntent().getStringExtra("lama_perj");
		String tgl_brngkt 					= getIntent().getStringExtra("tgl_brngkt");
		String tgl_kembali 					= getIntent().getStringExtra("tgl_kembali");
		String daerah_tujuan 				= getIntent().getStringExtra("tempat_tujuan");
		String instansi_yg_dikunjungi 		= getIntent().getStringExtra("surat_masuk_dari");
		String acara 						= getIntent().getStringExtra("maksud_perj");
		String tgl_surat_masuk 				= getIntent().getStringExtra("tgl_surat_masuk");
		String ambil_status_laporan_petugas = getIntent().getStringExtra("status_laporan_petugas");

		//String nip_pembuatlaporanperj 		= getIntent().getStringExtra("nip_pembuatlaporanperj");
		//String nomor_spt_laporanperj 		= getIntent().getStringExtra("nomor_spt_laporanperj");
		String hasil_pertemuan 				= getIntent().getStringExtra("hasil_pertemuan");
		String masalah 						= getIntent().getStringExtra("masalah");
		String saran 						= getIntent().getStringExtra("saran");
		String lain_lain 					= getIntent().getStringExtra("lain_lain");
		String tgl_pembuatan_laporan 		= getIntent().getStringExtra("tgl_pembuatan_laporan");		
		
		edit_dsr_sppt_1.setText(nomor_spt);
		edit_tampil_nama_spt.setText(nama);
		edit_tgl_pelaksanaan_awal.setText(tgl_brngkt);
		edit_tgl_pelaksanaan_sampai.setText(tgl_kembali);
		edit_lama_tgl_pelaksanaan.setText(" || " + lama_perj + " Hari");
		edit_daerah_tujuan.setText(daerah_tujuan);
		edit_ins_yg_kunjungi.setText(instansi_yg_dikunjungi);
		edit_acara.setText(acara);
		nip_lokal.setText(nip);
		edit_tgl_dsr_sppt_.setText(tgl_surat_masuk);
		status_laporan_petugas.setText(ambil_status_laporan_petugas);

		if (hasil_pertemuan.contains("null")){
			edit_hsl_rapat.setText("");							
		}else{
			edit_hsl_rapat.setText(hasil_pertemuan);				
			edit_masalah.setText(masalah);
			edit_saran.setText(saran);
			edit_lain_lain.setText(lain_lain);
			edit_tgl_ttd.setText(tgl_pembuatan_laporan);
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, tgl_ttd_petugas_pembuatlaporan,
					tahun, bulan, hari);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener tgl_ttd_petugas_pembuatlaporan = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			tahun = year;
			bulan = monthOfYear;
			hari = dayOfMonth;

			String koncersibulan = LPad(hari + "", "0", 2) + "-"
					+ varBulan[bulan] + "-" + tahun;

			edit_tgl_ttd.setText(koncersibulan);

		}
	};

	private static String LPad(String schar, String spad, int len) {
		String sret = schar;
		for (int i = sret.length(); i < len; i++) {
			sret = spad + sret;
		}
		return new String(sret);
	}

	@Override
	public void onBackPressed() {
		String hasil_pertemuan 	= getIntent().getStringExtra("hasil_pertemuan");
		String cek_status_laporan_petugas = status_laporan_petugas.getText()
				.toString();
		if ((hasil_pertemuan.contains("null")) && (cek_status_laporan_petugas.contains("BELUM"))){
			infodialogback();
		}else if (hasil_pertemuan.contains(hasil_pertemuan)){
			infodialogback2();
		}
	}

	private void infodialogback() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Peringatan");
		ad.setIcon(R.drawable.ic_warning_black);
		ad.setMessage("Anda akan membatalkan pembuatan Laporan Perjalanan Dinas ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Pembuatan_Lap_Setelah_Perj_Dinas.this.finish();
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
	private void infodialogback2() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setIcon(R.drawable.ic_info_outline_24dp);
		ad.setMessage("Batal Perubahan Data Laporan Perjalanan Dinas ?");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Pembuatan_Lap_Setelah_Perj_Dinas.this.finish();
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

	private Runnable runnable = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();

			SimpleDateFormat tgl_skrng = new SimpleDateFormat("d MMMM yyyy");
			SimpleDateFormat tgl_skrng2 = new SimpleDateFormat("d-M-yyyy");
			String strtgl_skrng = tgl_skrng.format(c1.getTime());
			String strtgl_skrng2 = tgl_skrng2.format(c1.getTime());
			edit_tgl_ttd.setText(strtgl_skrng);
			tgl_sembunyi.setText(strtgl_skrng2);
			handler.postDelayed(this, 1000);
		}

	};
}
