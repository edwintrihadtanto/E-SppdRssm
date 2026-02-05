package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.e_sppd.rssm.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import koneksi.JSONParser;

public class Laporan_Riil extends AppCompatActivity implements OnClickListener {
	private Handler handler = new Handler();
	private ProgressDialog dialog;
	private TextView nip, nama, jab, info_no_sppd, info_tgl,
			nama_pembuat_dftr_riil, nip_pembuat_dftr_riil, status_riil_petugas,
			status_rincian_biaya, path, nomorbiaya_1, nomorbiaya_2,
			nomorbiaya_3, nomorbiaya_4, nomorbiaya_5, nomorbiaya_6,
			nomorbiaya_7, nomorbiaya_8, nomorbiaya_9, nomorbiaya_10;
	private EditText edit_rincian_1, edit_rincian_2, edit_rincian_3,
			edit_rincian_4, edit_rincian_5, edit_rincian_6, edit_rincian_7,
			edit_rincian_8, edit_rincian_9, edit_rincian_10,
			edit_jml_rincian_1, edit_jml_rincian_2, edit_jml_rincian_3,
			edit_jml_rincian_4, edit_jml_rincian_5, edit_jml_rincian_6,
			edit_jml_rincian_7, edit_jml_rincian_8, edit_jml_rincian_9,
			edit_jml_rincian_10, edit_grand_total_riil,
			edit_tgl_dibuat_dftr_riil, edit_dialog_uraian, edit_textjml_riil;

	private static final String update_data_pengeluaran_riil = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/simpan_data_pengeluaran_riil.php";
	private static final String upload_file_no_file = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/simpan_rincian_riil.php";

	private static final String hapus_rincian_riil_satu = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_1.php";
	private static final String hapus_rincian_riil_dua = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_2.php";
	private static final String hapus_rincian_riil_3 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_3.php";
	private static final String hapus_rincian_riil_4 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_4.php";
	private static final String hapus_rincian_riil_5 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_5.php";
	private static final String hapus_rincian_riil_6 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_6.php";
	private static final String hapus_rincian_riil_7 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_7.php";
	private static final String hapus_rincian_riil_8 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_8.php";
	private static final String hapus_rincian_riil_9 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_9.php";
	private static final String hapus_rincian_riil_10 = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/proses_hapus_data_riil/hapus_rincian_riil_10.php";

	private static final String TAG_BERHASIL = "sukses";
	private static final String TAG_PESAN = "pesan";
	private Button btn_upload_ket_1, btn_upload_ket_2, btn_upload_ket_3,
			btn_upload_ket_4, btn_upload_ket_5, btn_upload_ket_6,
			btn_upload_ket_7, btn_upload_ket_8, btn_upload_ket_9,
			btn_upload_ket_10, btn_penjumlahan_riil,
			btn_simpan_rincian_biaya_riil, btn_upload_image,
			btn_tambh_uraian_riil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rincian_riil);
		handler.postDelayed(runnable, 1000);

		nip = (TextView) findViewById(R.id.edit_nip_pembuat);
		nama = (TextView) findViewById(R.id.edit_nama_pembuat);
		jab = (TextView) findViewById(R.id.edit_jab_pembuat);
		info_no_sppd = (TextView) findViewById(R.id.info_no_sppd);
		info_tgl = (TextView) findViewById(R.id.info_tgl);
		nama_pembuat_dftr_riil = (TextView) findViewById(R.id.nama_pembuat_dftr_riil);
		nip_pembuat_dftr_riil = (TextView) findViewById(R.id.nip_pembuat_dftr_riil);
		status_riil_petugas = (TextView) findViewById(R.id.status_riil);
		status_rincian_biaya = (TextView) findViewById(R.id.status_rincian_biaya);

		btn_penjumlahan_riil = (Button) findViewById(R.id.btn_penjumlahan_riil);
		btn_tambh_uraian_riil = (Button) findViewById(R.id.btn_tambh_uraian_riil);
		btn_simpan_rincian_biaya_riil = (Button) findViewById(R.id.btn_simpan_rincian_biaya_riil);

		edit_tgl_dibuat_dftr_riil = (EditText) findViewById(R.id.edit_tgl_dibuat_dftr_riil);
		// --------------------
		nomorbiaya_1 = (TextView) findViewById(R.id.nomorbiaya_1);
		nomorbiaya_2 = (TextView) findViewById(R.id.nomorbiaya_2);
		nomorbiaya_3 = (TextView) findViewById(R.id.nomorbiaya_3);
		nomorbiaya_4 = (TextView) findViewById(R.id.nomorbiaya_4);
		nomorbiaya_5 = (TextView) findViewById(R.id.nomorbiaya_5);
		nomorbiaya_6 = (TextView) findViewById(R.id.nomorbiaya_6);
		nomorbiaya_7 = (TextView) findViewById(R.id.nomorbiaya_7);
		nomorbiaya_8 = (TextView) findViewById(R.id.nomorbiaya_8);
		nomorbiaya_9 = (TextView) findViewById(R.id.nomorbiaya_9);
		nomorbiaya_10 = (TextView) findViewById(R.id.nomorbiaya_10);

		edit_rincian_1 = (EditText) findViewById(R.id.edit_rincian_1);
		edit_rincian_2 = (EditText) findViewById(R.id.edit_rincian_2);
		edit_rincian_3 = (EditText) findViewById(R.id.edit_rincian_3);
		edit_rincian_4 = (EditText) findViewById(R.id.edit_rincian_4);
		edit_rincian_5 = (EditText) findViewById(R.id.edit_rincian_5);
		edit_rincian_6 = (EditText) findViewById(R.id.edit_rincian_6);
		edit_rincian_7 = (EditText) findViewById(R.id.edit_rincian_7);
		edit_rincian_8 = (EditText) findViewById(R.id.edit_rincian_8);
		edit_rincian_9 = (EditText) findViewById(R.id.edit_rincian_9);
		edit_rincian_10 = (EditText) findViewById(R.id.edit_rincian_10);

		edit_jml_rincian_1 = (EditText) findViewById(R.id.edit_jml_rincian_1);
		edit_jml_rincian_2 = (EditText) findViewById(R.id.edit_jml_rincian_2);
		edit_jml_rincian_3 = (EditText) findViewById(R.id.edit_jml_rincian_3);
		edit_jml_rincian_4 = (EditText) findViewById(R.id.edit_jml_rincian_4);
		edit_jml_rincian_5 = (EditText) findViewById(R.id.edit_jml_rincian_5);
		edit_jml_rincian_6 = (EditText) findViewById(R.id.edit_jml_rincian_6);
		edit_jml_rincian_7 = (EditText) findViewById(R.id.edit_jml_rincian_7);
		edit_jml_rincian_8 = (EditText) findViewById(R.id.edit_jml_rincian_8);
		edit_jml_rincian_9 = (EditText) findViewById(R.id.edit_jml_rincian_9);
		edit_jml_rincian_10 = (EditText) findViewById(R.id.edit_jml_rincian_10);

		btn_upload_ket_1 = (Button) findViewById(R.id.btn_upload_ket_1);
		btn_upload_ket_2 = (Button) findViewById(R.id.btn_upload_ket_2);
		btn_upload_ket_3 = (Button) findViewById(R.id.btn_upload_ket_3);
		btn_upload_ket_4 = (Button) findViewById(R.id.btn_upload_ket_4);
		btn_upload_ket_5 = (Button) findViewById(R.id.btn_upload_ket_5);
		btn_upload_ket_6 = (Button) findViewById(R.id.btn_upload_ket_6);
		btn_upload_ket_7 = (Button) findViewById(R.id.btn_upload_ket_7);
		btn_upload_ket_8 = (Button) findViewById(R.id.btn_upload_ket_8);
		btn_upload_ket_9 = (Button) findViewById(R.id.btn_upload_ket_9);
		btn_upload_ket_10 = (Button) findViewById(R.id.btn_upload_ket_10);

		edit_grand_total_riil = (EditText) findViewById(R.id.edit_grand_total_riil);
		// -------------------------------------------------------------------------------------
		edit_rincian_1.setEnabled(false);
		edit_rincian_2.setEnabled(false);
		edit_rincian_3.setEnabled(false);
		edit_rincian_4.setEnabled(false);
		edit_rincian_5.setEnabled(false);
		edit_rincian_6.setEnabled(false);
		edit_rincian_7.setEnabled(false);
		edit_rincian_8.setEnabled(false);
		edit_rincian_9.setEnabled(false);
		edit_rincian_10.setEnabled(false);

		edit_jml_rincian_1.setText("0");
		edit_jml_rincian_2.setText("0");
		edit_jml_rincian_3.setText("0");
		edit_jml_rincian_4.setText("0");
		edit_jml_rincian_5.setText("0");
		edit_jml_rincian_6.setText("0");
		edit_jml_rincian_7.setText("0");
		edit_jml_rincian_8.setText("0");
		edit_jml_rincian_9.setText("0");
		edit_jml_rincian_10.setText("0");

		edit_jml_rincian_1.setEnabled(false);
		edit_jml_rincian_2.setEnabled(false);
		edit_jml_rincian_3.setEnabled(false);
		edit_jml_rincian_4.setEnabled(false);
		edit_jml_rincian_5.setEnabled(false);
		edit_jml_rincian_6.setEnabled(false);
		edit_jml_rincian_7.setEnabled(false);
		edit_jml_rincian_8.setEnabled(false);
		edit_jml_rincian_9.setEnabled(false);
		edit_jml_rincian_10.setEnabled(false);

		edit_grand_total_riil.setEnabled(false);

		// SEMBUNYIKAN BUTTON UPLOAD

		btn_upload_ket_1.setVisibility(View.GONE);
		btn_upload_ket_2.setVisibility(View.GONE);
		btn_upload_ket_3.setVisibility(View.GONE);
		btn_upload_ket_4.setVisibility(View.GONE);
		btn_upload_ket_5.setVisibility(View.GONE);
		btn_upload_ket_6.setVisibility(View.GONE);
		btn_upload_ket_7.setVisibility(View.GONE);
		btn_upload_ket_8.setVisibility(View.GONE);
		btn_upload_ket_9.setVisibility(View.GONE);
		btn_upload_ket_10.setVisibility(View.GONE);
		/*
		 * btn_upload_ket_1.setVisibility(View.VISIBLE);
		 * btn_upload_ket_2.setVisibility(View.VISIBLE);
		 * btn_upload_ket_3.setVisibility(View.VISIBLE);
		 * btn_upload_ket_4.setVisibility(View.VISIBLE);
		 * btn_upload_ket_5.setVisibility(View.VISIBLE);
		 * btn_upload_ket_6.setVisibility(View.VISIBLE);
		 * btn_upload_ket_7.setVisibility(View.VISIBLE);
		 * btn_upload_ket_8.setVisibility(View.VISIBLE);
		 * btn_upload_ket_9.setVisibility(View.VISIBLE);
		 * btn_upload_ket_10.setVisibility(View.VISIBLE);
		 */
		btn_penjumlahan_riil.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(Laporan_Riil.this,
						"Tombol Penjumlahan Pengeluaran Riil",
						Toast.LENGTH_LONG).show();
				return true;
			}
		});

		Tampil_data();
		show_hide_button_upload();

		String cek_status_riil = status_riil_petugas.getText().toString();
		if (cek_status_riil.contains("SUDAH")) {
			Toast.makeText(Laporan_Riil.this,
					"Ups... Anda Sudah Membuat Laporan Pengeluaran Biaya Riil",
					Toast.LENGTH_LONG).show();
			Laporan_Riil.this.finish();
			/*
			 * Bundle ba = getIntent().getExtras();
			 * 
			 * String ambil_nip = ba.getString("nip"); String ambil_nama =
			 * ba.getString("nama"); String ambil_jab = ba.getString("jab");
			 * String ambil_sppd = ba.getString("sppd"); String ambil_tgl_sppd =
			 * ba.getString("tgl_sppd"); //String ambil_status_riil =
			 * ba.getString("status_riil"); finish(); Intent i = new
			 * Intent(Laporan_Riil.this,
			 * Laporan_Rincian_Biaya_Perj_Dinas.class);
			 * 
			 * Bundle s = new Bundle(); s.putString("nip", ambil_nip);
			 * s.putString("nama", ambil_nama); s.putString("jab", ambil_jab);
			 * s.putString("sppd", ambil_sppd); s.putString("tgl_sppd",
			 * ambil_tgl_sppd); //s.putString("status_riil", cek_status_riil);
			 * 
			 * i.putExtras(s); startActivity(i);
			 */
		} else if (cek_status_riil.contains("BELUM")) {
			Toast.makeText(Laporan_Riil.this,
					"Selesaikan Pembuatan Laporan Pengeluaran Biaya Riil Anda",
					Toast.LENGTH_LONG).show();
		}
		/*
		 * else {
		 * Toast.makeText(Laporan_Riil.this,"Ups... Anda Sudah Membuat Laporan Riil"
		 * , Toast.LENGTH_LONG).show(); Bundle ba = getIntent().getExtras();
		 * 
		 * String ambil_nip = ba.getString("nip"); String ambil_nama =
		 * ba.getString("nama"); String ambil_jab = ba.getString("jab"); String
		 * ambil_sppd = ba.getString("sppd"); String ambil_tgl_sppd =
		 * ba.getString("tgl_sppd"); //String ambil_status_riil =
		 * ba.getString("status_riil");
		 * 
		 * finish(); Intent i = new Intent(Laporan_Riil.this,
		 * Laporan_Rincian_Biaya_Perj_Dinas.class);
		 * 
		 * Bundle s = new Bundle(); s.putString("nip", ambil_nip);
		 * s.putString("nama", ambil_nama); s.putString("jab", ambil_jab);
		 * s.putString("sppd", ambil_sppd); s.putString("tgl_sppd",
		 * ambil_tgl_sppd); //s.putString("status_riil", cek_status_riil);
		 * 
		 * i.putExtras(s); startActivity(i); }
		 */
		btn_tambh_uraian_riil.setOnClickListener(this);
		btn_penjumlahan_riil.setOnClickListener(this);
		btn_simpan_rincian_biaya_riil.setOnClickListener(this);
		btn_upload_ket_1.setOnClickListener(this);
		btn_upload_ket_2.setOnClickListener(this);
		btn_upload_ket_3.setOnClickListener(this);
		btn_upload_ket_4.setOnClickListener(this);
		btn_upload_ket_5.setOnClickListener(this);
		btn_upload_ket_6.setOnClickListener(this);
		btn_upload_ket_7.setOnClickListener(this);
		btn_upload_ket_8.setOnClickListener(this);
		btn_upload_ket_9.setOnClickListener(this);
		btn_upload_ket_10.setOnClickListener(this);

		ImageView bantuan_riil = (ImageView)findViewById(R.id.bantuan_riil);
		bantuan_riil.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//String pesan = "Sipp Mantap";
				//showAlert(pesan);
				Intent i = null;
				i = new Intent(Laporan_Riil.this,
						Tampil_Bantuan.class);
				startActivity(i);
			}
		});
	}

	public void Tampil_data() {
		Bundle ba = getIntent().getExtras();

		String ambil_nip = ba.getString("nip");
		// String ambil_nama = ba.getString("nama");
		// String ambil_jab = ba.getString("jab");
		// String ambil_sppd = ba.getString("sppd");
		// String ambil_tgl_sppd = ba.getString("tgl_sppd");
		String ambil_status_riil = ba.getString("status_riil");
		String ambil_status_biaya = ba.getString("status_biaya");

		// String nip1 = getIntent().getStringExtra("nip");
		String ambil_nama = getIntent().getStringExtra("nama_pegawai");
		String ambil_jab = getIntent().getStringExtra("jabatan");
		String ambil_sppd = getIntent().getStringExtra("nomor_surat_sppd");
		String ambil_tgl_sppd = getIntent().getStringExtra("tgl_surat_masuk");
		// String ambil_status_laporan_petugas =
		// getIntent().getStringExtra("status_laporan_petugas");

		nama.setText(ambil_nama);
		nip.setText(ambil_nip);
		jab.setText(ambil_jab);
		info_no_sppd.setText(ambil_sppd);
		info_tgl.setText(ambil_tgl_sppd);
		nama_pembuat_dftr_riil.setText(ambil_nama);
		nip_pembuat_dftr_riil.setText("NIP/NPK" + "  " + ambil_nip);
		status_riil_petugas.setText(ambil_status_riil);
		status_rincian_biaya.setText(ambil_status_biaya);

	}

	private void pertanyaan_simpan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Pastikan Data Daftar Pengeluaran Riil Anda Sudah Benar \n"
				+ "Data Yang Sudah Tersimpan Tidak Dapat Dirubah Kembali \n"
				+ "Simpan Daftar Pengeluaran Riil ???");
		ad.setPositiveButton("Simpan", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
				new Update_Status_Rincian().execute();
				// String cek_biaya = status_rincian_biaya.getText().toString();

				// if (cek_biaya.contentEquals("SUDAH")) { //PROSES PENGECEKAN
				// STATUS BIAYA
				// new Update_Status_Rincian().execute();
				// }else{
				/*
				 * Bundle ba = getIntent().getExtras();
				 * 
				 * String ambil_nip = ba.getString("nip"); String ambil_nama =
				 * ba.getString("nama"); String ambil_jab = ba.getString("jab");
				 * String ambil_sppd = ba.getString("sppd"); String
				 * ambil_tgl_sppd = ba.getString("tgl_sppd");
				 * 
				 * Intent i = new Intent(Laporan_Riil.this,
				 * Laporan_Rincian_Biaya_Perj_Dinas.class);
				 * 
				 * Bundle s = new Bundle(); s.putString("nip", ambil_nip);
				 * s.putString("nama", ambil_nama); s.putString("jab",
				 * ambil_jab); s.putString("sppd", ambil_sppd);
				 * s.putString("tgl_sppd", ambil_tgl_sppd);
				 * 
				 * i.putExtras(s); startActivity(i); new
				 * Update_Status_Rincian().execute(); }
				 */
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

	class Update_Status_Rincian extends AsyncTask<String, String, String> {
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Laporan_Riil.this);
			dialog.setMessage("Loading ...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;

			String ambil_nomor_sppd = info_no_sppd.getText().toString().trim();
			String nip12 = nip.getText().toString().trim();

			String rincian_biaya_1 = edit_rincian_1.getText().toString().trim();
			String rincian_biaya_2 = edit_rincian_2.getText().toString().trim();
			String rincian_biaya_3 = edit_rincian_3.getText().toString().trim();
			String rincian_biaya_4 = edit_rincian_4.getText().toString().trim();
			String rincian_biaya_5 = edit_rincian_5.getText().toString().trim();
			String rincian_biaya_6 = edit_rincian_6.getText().toString().trim();
			String rincian_biaya_7 = edit_rincian_7.getText().toString().trim();
			String rincian_biaya_8 = edit_rincian_8.getText().toString().trim();
			String rincian_biaya_9 = edit_rincian_9.getText().toString().trim();
			String rincian_biaya_10 = edit_rincian_10.getText().toString()
					.trim();

			String jml_biaya_default = edit_jml_rincian_1.getText().toString()
					.trim();
			String jml_biaya_2 = edit_jml_rincian_2.getText().toString().trim();
			String jml_biaya_3 = edit_jml_rincian_3.getText().toString().trim();
			String jml_biaya_4 = edit_jml_rincian_4.getText().toString().trim();
			String jml_biaya_5 = edit_jml_rincian_5.getText().toString().trim();
			String jml_biaya_6 = edit_jml_rincian_6.getText().toString().trim();
			String jml_biaya_7 = edit_jml_rincian_7.getText().toString().trim();
			String jml_biaya_8 = edit_jml_rincian_8.getText().toString().trim();
			String jml_biaya_9 = edit_jml_rincian_9.getText().toString().trim();
			String jml_biaya_10 = edit_jml_rincian_10.getText().toString()
					.trim();

			String ambil_tgl_pembuat = edit_tgl_dibuat_dftr_riil.getText()
					.toString().trim();
			String grand_total = edit_grand_total_riil.getText().toString()
					.trim();

			try {

				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("ambil_nomor_sppd",
						ambil_nomor_sppd));
				parameterNya.add(new BasicNameValuePair("nip", nip12));

				parameterNya.add(new BasicNameValuePair(
						"rincian_biaya_default", rincian_biaya_1));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_2",
						rincian_biaya_2));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_3",
						rincian_biaya_3));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_4",
						rincian_biaya_4));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_5",
						rincian_biaya_5));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_6",
						rincian_biaya_6));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_7",
						rincian_biaya_7));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_8",
						rincian_biaya_8));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_9",
						rincian_biaya_9));
				parameterNya.add(new BasicNameValuePair("rincian_biaya_10",
						rincian_biaya_10));

				parameterNya.add(new BasicNameValuePair("jml_biaya_default",
						jml_biaya_default));
				parameterNya.add(new BasicNameValuePair("jml_biaya_2",
						jml_biaya_2));
				parameterNya.add(new BasicNameValuePair("jml_biaya_3",
						jml_biaya_3));
				parameterNya.add(new BasicNameValuePair("jml_biaya_4",
						jml_biaya_4));
				parameterNya.add(new BasicNameValuePair("jml_biaya_5",
						jml_biaya_5));
				parameterNya.add(new BasicNameValuePair("jml_biaya_6",
						jml_biaya_6));
				parameterNya.add(new BasicNameValuePair("jml_biaya_7",
						jml_biaya_7));
				parameterNya.add(new BasicNameValuePair("jml_biaya_8",
						jml_biaya_8));
				parameterNya.add(new BasicNameValuePair("jml_biaya_9",
						jml_biaya_9));
				parameterNya.add(new BasicNameValuePair("jml_biaya_10",
						jml_biaya_10));

				parameterNya.add(new BasicNameValuePair(
						"tgl_pembuatan_rincian", ambil_tgl_pembuat));
				parameterNya.add(new BasicNameValuePair("grand_total",
						grand_total));

				Log.d("Request ke server!", "dimulai");
				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser
						.makeHttpRequest(update_data_pengeluaran_riil, "POST",
								parameterNya);
				Log.d("Try Again", jsonObjectNya.toString());
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
			dialog.dismiss();
			if (url_registrasi_nya != null) {
			
				informasi_biasa(url_registrasi_nya);
			}
			

		}

	}

	@Override
	public void onBackPressed() {
		String cek_uraian = edit_rincian_1.getText().toString();
		String cek_biaya = status_rincian_biaya.getText().toString();
		if ((cek_biaya.contentEquals("SUDAH")) && (cek_uraian.isEmpty())) {
			Toast.makeText(
					Laporan_Riil.this,
					"Ups... Anda Tidak Bisa Kembali Lagi Ke Form Perincian Biaya",
					Toast.LENGTH_LONG).show();
			pertanyaan_kembali_jika_yes_dan_kosong();
		} else if ((cek_uraian.isEmpty()) && (cek_biaya.contentEquals(""))) {
			Laporan_Riil.this.finish();
			finish();
			/*
			 * Bundle ba = getIntent().getExtras();
			 * 
			 * String ambil_nip = ba.getString("nip"); String ambil_nama =
			 * ba.getString("nama"); String ambil_jab = ba.getString("jab");
			 * String ambil_sppd = ba.getString("sppd"); String ambil_tgl_sppd =
			 * ba.getString("tgl_sppd"); String ambil_status_riil_petugas =
			 * status_riil_petugas.getText().toString(); finish(); Intent i =
			 * new Intent(Laporan_Riil.this,
			 * Laporan_Rincian_Biaya_Perj_Dinas.class);
			 * 
			 * Bundle s = new Bundle(); s.putString("nip", ambil_nip);
			 * s.putString("nama", ambil_nama); s.putString("jab", ambil_jab);
			 * s.putString("sppd", ambil_sppd); s.putString("tgl_sppd",
			 * ambil_tgl_sppd); s.putString("status_riil",
			 * ambil_status_riil_petugas); i.putExtras(s); startActivity(i);
			 */
		} else if ((cek_uraian.length() > 1)
				&& (cek_biaya.contentEquals("SUDAH"))) {
			pertanyaan_kembali_jika_yes_dan_tdk_kosong();
		} else {
			pertanyaan_kembali_jika_kosngsemua();
		}

	}

	// ------------------------------------------------------------------------------------------
	private void pertanyaan_kembali_jika_kosngsemua() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Anda Belum Menyimpan Data Pengeluaran Riil Anda\n"
				+ "Apakah Anda akan tetap Keluar ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				Laporan_Riil.this.finish();
				finish();
				/*
				Bundle ba = getIntent().getExtras();

				String ambil_nip = ba.getString("nip");
				String ambil_nama = ba.getString("nama");
				String ambil_jab = ba.getString("jab");
				String ambil_sppd = ba.getString("sppd");
				String ambil_tgl_sppd = ba.getString("tgl_sppd");
				String ambil_status_riil_petugas = status_riil_petugas
						.getText().toString();
				finish();
				Intent i = new Intent(Laporan_Riil.this,
						Laporan_Rincian_Biaya_Perj_Dinas.class);

				Bundle s = new Bundle();
				s.putString("nip", ambil_nip);
				s.putString("nama", ambil_nama);
				s.putString("jab", ambil_jab);
				s.putString("sppd", ambil_sppd);
				s.putString("tgl_sppd", ambil_tgl_sppd);
				s.putString("status_riil", ambil_status_riil_petugas);
				i.putExtras(s);
				startActivity(i);
				*/
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

	private void pertanyaan_kembali_jika_yes_dan_kosong() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Anda Belum Mengisikan Daftar Pengeluaran Riil Anda\n"
				+ "Apakah Anda akan tetap Keluar ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Laporan_Riil.this.finish();
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

	private void pertanyaan_kembali_jika_yes_dan_tdk_kosong() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Anda Belum Menyimpan Data Pengeluaran Riil Anda\n"
				+ "Apakah Anda akan tetap Keluar ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Laporan_Riil.this.finish();
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

	// ------------------------------------------------------------------------------------------
	private Runnable runnable = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();

			SimpleDateFormat tgl_skrng = new SimpleDateFormat("d MMM yyyy");

			String strdate_tgl = tgl_skrng.format(c1.getTime());

			edit_tgl_dibuat_dftr_riil.setText(strdate_tgl);

			handler.postDelayed(this, 1000);
		}

	};

	@Override
	public void onClick(View view_data) {
		// TODO Auto-generated method stub
		switch (view_data.getId()) {

		case R.id.btn_refresh:
			finish();
			startActivity(getIntent());
			break;

		case R.id.btn_tambh_uraian_riil:

			dialog_pop_up_tambah_uraian();

			break;
		case R.id.btn_simpan_rincian_biaya_riil:
			if (edit_rincian_1.getText().toString().isEmpty()) {
				String pesan = "Upss,.. Data pada No. 1 Tidak Boleh Kosong !!!\nUrutkan Sesuai Penomoran yang telah disediakan";
				informasi_biasa(pesan);
				
			} else if (edit_grand_total_riil.getText().toString().isEmpty()) {
				String pesan = "Upss,.. Anda Belum Melakukan Penjumlahan Perincian Biaya Riil !!!";
				informasi_biasa(pesan);
				
			} else {
				pertanyaan_simpan();
			}
			break;

		case R.id.btn_penjumlahan_riil:
			function_jumlah();
			break;

		// PROSES HAPUS
		// -----------------------------------------------------------------
		// TURUN KEBAWAH
		case R.id.btn_upload_ket_1:
			String satu = edit_rincian_1.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + satu + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_satu().execute();

			edit_rincian_1.setText("");
			edit_jml_rincian_1.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_2:
			String dua = edit_rincian_2.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + dua + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_dua().execute();

			edit_rincian_2.setText("");
			edit_jml_rincian_2.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_3:
			String tiga = edit_rincian_3.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + tiga + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_3.setText("");
			edit_jml_rincian_3.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_4:
			String empat = edit_rincian_4.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + empat + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_4.setText("");
			edit_jml_rincian_4.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_5:
			String lima = edit_rincian_5.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + lima + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_5.setText("");
			edit_jml_rincian_5.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_6:
			String enam = edit_rincian_6.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + enam + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_6.setText("");
			edit_jml_rincian_6.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_7:
			String tujuh = edit_rincian_7.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + tujuh + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_7.setText("");
			edit_jml_rincian_7.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_8:
			String delapan = edit_rincian_8.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + delapan + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_8.setText("");
			edit_jml_rincian_8.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_9:
			String sembilan = edit_rincian_9.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + sembilan + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_9.setText("");
			edit_jml_rincian_9.setText("");
			function_jumlah();
			break;
		case R.id.btn_upload_ket_10:
			String sepuluh = edit_rincian_10.getText().toString();
			Toast.makeText(Laporan_Riil.this, "--Hapus =>" + sepuluh + "--",
					Toast.LENGTH_LONG).show();
			// new proses_hapus_tiga().execute();

			edit_rincian_10.setText("");
			edit_jml_rincian_10.setText("");
			function_jumlah();
			break;

		default:
			break;
		}
	}
	
	private void informasi_biasa(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(message)
				.setTitle("Informasi")
				.setCancelable(false)
				
				.setPositiveButton("Ok",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								dialog.dismiss();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public void function_jumlah() {
		String rincian_1 = edit_jml_rincian_1.getText().toString();
		String rincian_2 = edit_jml_rincian_2.getText().toString();
		String rincian_3 = edit_jml_rincian_3.getText().toString();
		String rincian_4 = edit_jml_rincian_4.getText().toString();
		String rincian_5 = edit_jml_rincian_5.getText().toString();
		String rincian_6 = edit_jml_rincian_6.getText().toString();
		String rincian_7 = edit_jml_rincian_7.getText().toString();
		String rincian_8 = edit_jml_rincian_8.getText().toString();
		String rincian_9 = edit_jml_rincian_9.getText().toString();
		String rincian_10 = edit_jml_rincian_10.getText().toString();

		if (rincian_1.isEmpty()) {
			edit_jml_rincian_1.setText("0");
		}
		if (rincian_2.isEmpty()) {
			edit_jml_rincian_2.setText("0");
		}
		if (rincian_3.isEmpty()) {
			edit_jml_rincian_3.setText("0");
		}
		if (rincian_4.isEmpty()) {
			edit_jml_rincian_4.setText("0");
		}
		if (rincian_5.isEmpty()) {
			edit_jml_rincian_5.setText("0");
		}
		if (rincian_6.isEmpty()) {
			edit_jml_rincian_6.setText("0");
		}
		if (rincian_7.isEmpty()) {
			edit_jml_rincian_7.setText("0");
		}
		if (rincian_8.isEmpty()) {
			edit_jml_rincian_8.setText("0");
		}
		if (rincian_9.isEmpty()) {
			edit_jml_rincian_9.setText("0");
		}
		if (rincian_10.isEmpty()) {
			edit_jml_rincian_10.setText("0");
		}

		int jml_1 = Integer.parseInt(edit_jml_rincian_1.getText().toString());
		int jml_2 = Integer.parseInt(edit_jml_rincian_2.getText().toString());
		int jml_3 = Integer.parseInt(edit_jml_rincian_3.getText().toString());
		int jml_4 = Integer.parseInt(edit_jml_rincian_4.getText().toString());
		int jml_5 = Integer.parseInt(edit_jml_rincian_5.getText().toString());
		int jml_6 = Integer.parseInt(edit_jml_rincian_6.getText().toString());
		int jml_7 = Integer.parseInt(edit_jml_rincian_7.getText().toString());
		int jml_8 = Integer.parseInt(edit_jml_rincian_8.getText().toString());
		int jml_9 = Integer.parseInt(edit_jml_rincian_9.getText().toString());
		int jml_10 = Integer.parseInt(edit_jml_rincian_10.getText().toString());

		String hasil = Integer.toString(jml_1 + jml_2 + jml_3 + jml_4 + jml_5
				+ jml_6 + jml_7 + jml_8 + jml_9 + jml_10);
		edit_grand_total_riil.setText("Rp. " + hasil);
	}

	public void function_parse_uraian() {
		String ambil_uraian = edit_dialog_uraian.getText().toString();
		String ambil_jml = edit_textjml_riil.getText().toString();
		// String ambil_nama_file = "Delete";

		if (ambil_uraian.isEmpty() || ambil_jml.isEmpty()) {
			Toast.makeText(Laporan_Riil.this, "Data Wajib Diisi",
					Toast.LENGTH_LONG).show();
		} else {
			dialog.dismiss();
			if (edit_rincian_1.getText().toString().isEmpty()) {
				edit_rincian_1.setText(ambil_uraian);
				edit_jml_rincian_1.setText(ambil_jml);
				// btn_upload_ket_1.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().isEmpty()) {
				edit_rincian_2.setText(ambil_uraian);
				edit_jml_rincian_2.setText(ambil_jml);
				// btn_upload_ket_2.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().isEmpty()) {
				edit_rincian_3.setText(ambil_uraian);
				edit_jml_rincian_3.setText(ambil_jml);
				// btn_upload_ket_3.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().isEmpty()) {
				edit_rincian_4.setText(ambil_uraian);
				edit_jml_rincian_4.setText(ambil_jml);
				// btn_upload_ket_4.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().isEmpty()) {
				edit_rincian_5.setText(ambil_uraian);
				edit_jml_rincian_5.setText(ambil_jml);
				// btn_upload_ket_5.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().length() > 1
					&& edit_rincian_6.getText().toString().isEmpty()) {
				edit_rincian_6.setText(ambil_uraian);
				edit_jml_rincian_6.setText(ambil_jml);
				// btn_upload_ket_6.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().length() > 1
					&& edit_rincian_6.getText().toString().length() > 1
					&& edit_rincian_7.getText().toString().isEmpty()) {
				edit_rincian_7.setText(ambil_uraian);
				edit_jml_rincian_7.setText(ambil_jml);
				// btn_upload_ket_7.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().length() > 1
					&& edit_rincian_6.getText().toString().length() > 1
					&& edit_rincian_7.getText().toString().length() > 1
					&& edit_rincian_8.getText().toString().isEmpty()) {
				edit_rincian_8.setText(ambil_uraian);
				edit_jml_rincian_8.setText(ambil_jml);
				// btn_upload_ket_8.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().length() > 1
					&& edit_rincian_6.getText().toString().length() > 1
					&& edit_rincian_7.getText().toString().length() > 1
					&& edit_rincian_8.getText().toString().length() > 1
					&& edit_rincian_9.getText().toString().isEmpty()) {
				edit_rincian_9.setText(ambil_uraian);
				edit_jml_rincian_9.setText(ambil_jml);
				// btn_upload_ket_9.setText(ambil_nama_file);
				function_jumlah();

			} else {
				edit_rincian_10.setText(ambil_uraian);
				edit_jml_rincian_10.setText(ambil_jml);
				// btn_upload_ket_10.setText(ambil_nama_file);
				function_jumlah();
			}
		}
	}

	private void dialog_pop_up_tambah_uraian() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up_perincian_riil);
		edit_dialog_uraian = (EditText) dialog
				.findViewById(R.id.edit_dialog_uraian);
		edit_textjml_riil = (EditText) dialog
				.findViewById(R.id.edit_textjml_riil);
		btn_upload_image = (Button) dialog.findViewById(R.id.btn_upload_image);
		Button btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
		TextView atau = (TextView) dialog.findViewById(R.id.atau);
		Button btn_cncl = (Button) dialog.findViewById(R.id.btn_cncl);
		Button btn_simpan = (Button) dialog.findViewById(R.id.btn_simpan);
		path = (TextView) dialog.findViewById(R.id.path);

		btn_upload_image.setVisibility(View.GONE);
		btn_camera.setVisibility(View.GONE);
		atau.setVisibility(View.GONE);
		dialog.show();
		
		btn_simpan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (edit_dialog_uraian.getText().toString().isEmpty()) {
					Toast.makeText(Laporan_Riil.this, "Data Wajib Diisi",
							Toast.LENGTH_SHORT).show();
				} else if (edit_textjml_riil.getText().toString().isEmpty()) {
					Toast.makeText(Laporan_Riil.this, "Data Wajib Diisi",
							Toast.LENGTH_SHORT).show();
				} else {
					dialog.dismiss();
					new uploadFile_cara_lain().execute(); // PROSES SIMPAN DATA
														  // APABILA TIDAK
														  // UPLOAD FOTO
				}
			}
		});

		btn_cncl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

	}

	// MATI SEMENATARA
	class uploadFile_cara_lain extends AsyncTask<String, String, String> {

		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Laporan_Riil.this);
			dialog.setMessage("Tunggu Sebentar Masih Dalam Proses Simpan...");
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... args) {
			/*
			int berhasil;
			String uraian = edit_dialog_uraian.getText().toString();
			String jml = edit_textjml_riil.getText().toString();
			String no_sppd = info_no_sppd.getText().toString();
			String ambilnip = nip.getText().toString();
			String tgl_buat = edit_tgl_dibuat_dftr_riil.getText().toString();
			
			 * try {
			 * 
			 * List<NameValuePair> parameterNya = new
			 * ArrayList<NameValuePair>(); parameterNya.add(new
			 * BasicNameValuePair("uraian", uraian)); parameterNya.add(new
			 * BasicNameValuePair("jml", jml)); parameterNya.add(new
			 * BasicNameValuePair("no_sppd", no_sppd)); parameterNya.add(new
			 * BasicNameValuePair("ambilnip", ambilnip)); parameterNya.add(new
			 * BasicNameValuePair("tgl_buat", tgl_buat));
			 * 
			 * 
			 * Log.d("Request ke server!", "dimulai"); JSONParser
			 * ambil_classJSONParser = new JSONParser(); JSONObject
			 * jsonObjectNya =
			 * ambil_classJSONParser.makeHttpRequest(upload_file_no_file,
			 * "POST", parameterNya);
			 * 
			 * //MASIH BELUM BERFUNGSI SCRIPT BAWAH INI, DIKARENAKAN STRUKTUR
			 * PADA PHP BUKAN PDO Log.d("Try Again", jsonObjectNya.toString());
			 * berhasil = jsonObjectNya.getInt(TAG_BERHASIL); if (berhasil == 1)
			 * { Log.d("Sukses !!!", jsonObjectNya.toString());
			 * 
			 * return jsonObjectNya.getString(TAG_PESAN); }else{
			 * Log.d("Failed !!!", jsonObjectNya.getString(TAG_PESAN)); return
			 * jsonObjectNya.getString(TAG_PESAN); }
			 * 
			 * } catch (JSONException e) { e.printStackTrace(); }
			 */
			return null;

		}

		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			// matikan progressBar-nya setelah selesai di gunakan
			//dialog.dismiss();
			//if (url_registrasi_nya != null) {
			//	Toast.makeText(Laporan_Riil.this, url_registrasi_nya,
			//			Toast.LENGTH_SHORT).show();
			//}
			function_parse_uraian();
		}

	}

	// _______________________________________________________________________________________________

	// FUNDTION SHIW HIDE BUTTON BELUM DIGUNAKAN
	public void show_hide_button_upload() {

		edit_rincian_1.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_1.getText().toString().length() > 1) {
					btn_upload_ket_1.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_1.setVisibility(View.GONE);

				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		edit_rincian_2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_2.getText().toString().length() > 1) {
					btn_upload_ket_2.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_2.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_3.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_3.getText().toString().length() > 1) {
					btn_upload_ket_3.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_3.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_4.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_4.getText().toString().length() > 1) {
					btn_upload_ket_4.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_4.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_5.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_5.getText().toString().length() > 1) {
					btn_upload_ket_5.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_5.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_6.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_6.getText().toString().length() > 1) {
					btn_upload_ket_6.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_6.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_7.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_7.getText().toString().length() > 1) {
					btn_upload_ket_7.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_7.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_8.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_8.getText().toString().length() > 1) {
					btn_upload_ket_8.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_8.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		// --------------------------------------------------------------------------------------------------------------------------------
		edit_rincian_9.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_9.getText().toString().length() > 1) {
					btn_upload_ket_9.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_9.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		edit_rincian_10.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (edit_rincian_10.getText().toString().length() > 1) {
					btn_upload_ket_10.setVisibility(View.VISIBLE);
				} else {
					btn_upload_ket_10.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		// --------------------------------------------------------------------------------------------------------------------------------

		// --------------------------------------------------------------------------------------------------------------------------------

	}

	/*
	 * class proses_hapus_satu extends AsyncTask<String, String, String> {
	 * boolean failure = false;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); dialog =
	 * new ProgressDialog( Laporan_Riil.this); dialog.setMessage("Loading...");
	 * dialog.setIndeterminate(false); dialog.setCancelable(true);
	 * dialog.show(); }
	 * 
	 * @Override protected String doInBackground(String... args) {
	 * 
	 * int berhasil;
	 * 
	 * String uraian_1 = edit_rincian_1.getText().toString(); String no_sppd =
	 * info_no_sppd.getText().toString(); String nomor_text =
	 * nomorbiaya_1.getText().toString();
	 * 
	 * try {
	 * 
	 * List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
	 * parameterNya.add(new BasicNameValuePair("no_sppd", no_sppd));
	 * parameterNya.add(new BasicNameValuePair("uraian_1", uraian_1));
	 * parameterNya.add(new BasicNameValuePair("nomor_text", nomor_text));
	 * 
	 * Log.d("Request ke server!", "dimulai"); JSONParser ambil_classJSONParser
	 * = new JSONParser(); JSONObject jsonObjectNya =
	 * ambil_classJSONParser.makeHttpRequest(hapus_rincian_riil_satu, "POST",
	 * parameterNya); Log.d("Try Again", jsonObjectNya.toString());
	 * 
	 * berhasil = jsonObjectNya.getInt(TAG_BERHASIL); if (berhasil == 1) {
	 * Log.d("Sukses !!!", jsonObjectNya.toString()); return
	 * jsonObjectNya.getString(TAG_PESAN); } else { Log.d("Failed !!!",
	 * jsonObjectNya.getString(TAG_PESAN)); return
	 * jsonObjectNya.getString(TAG_PESAN); } } catch (JSONException e) {
	 * e.printStackTrace(); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * protected void onPostExecute(String url_registrasi_nya) { // matikan
	 * progressBar-nya setelah selesai di gunakan dialog.dismiss(); if
	 * (url_registrasi_nya != null) { Toast.makeText(Laporan_Riil.this,
	 * url_registrasi_nya, Toast.LENGTH_LONG).show();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * class proses_hapus_dua extends AsyncTask<String, String, String> {
	 * boolean failure = false;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); dialog =
	 * new ProgressDialog(Laporan_Riil.this); dialog.setMessage("Loading...");
	 * dialog.setIndeterminate(false); dialog.setCancelable(true);
	 * dialog.show(); }
	 * 
	 * @Override protected String doInBackground(String... args) {
	 * 
	 * int berhasil;
	 * 
	 * String uraian_2 = edit_rincian_2.getText().toString(); String no_sppd =
	 * info_no_sppd.getText().toString(); String nomor_text =
	 * nomorbiaya_2.getText().toString();
	 * 
	 * try {
	 * 
	 * List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
	 * parameterNya.add(new BasicNameValuePair("no_sppd", no_sppd));
	 * parameterNya.add(new BasicNameValuePair("uraian_2", uraian_2));
	 * parameterNya.add(new BasicNameValuePair("nomorbiaya_2", nomor_text));
	 * 
	 * Log.d("Request ke server!", "dimulai"); JSONParser ambil_classJSONParser
	 * = new JSONParser(); JSONObject jsonObjectNya =
	 * ambil_classJSONParser.makeHttpRequest(hapus_rincian_riil_dua, "POST",
	 * parameterNya); Log.d("Try Again", jsonObjectNya.toString());
	 * 
	 * berhasil = jsonObjectNya.getInt(TAG_BERHASIL); if (berhasil == 1) {
	 * Log.d("Sukses !!!", jsonObjectNya.toString()); return
	 * jsonObjectNya.getString(TAG_PESAN); } else { Log.d("Failed !!!",
	 * jsonObjectNya.getString(TAG_PESAN)); return
	 * jsonObjectNya.getString(TAG_PESAN); } } catch (JSONException e) {
	 * e.printStackTrace(); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * protected void onPostExecute(String url_registrasi_nya) { // matikan
	 * progressBar-nya setelah selesai di gunakan dialog.dismiss(); if
	 * (url_registrasi_nya != null) { Toast.makeText(Laporan_Riil.this,
	 * url_registrasi_nya, Toast.LENGTH_LONG).show();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 * 
	 * class proses_hapus_tiga extends AsyncTask<String, String, String> {
	 * boolean failure = false;
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute(); dialog =
	 * new ProgressDialog(Laporan_Riil.this); dialog.setMessage("Loading...");
	 * dialog.setIndeterminate(false); dialog.setCancelable(true);
	 * dialog.show(); }
	 * 
	 * @Override protected String doInBackground(String... args) {
	 * 
	 * int berhasil;
	 * 
	 * String uraian_3 = edit_rincian_3.getText().toString(); String no_sppd =
	 * info_no_sppd.getText().toString();
	 * 
	 * try {
	 * 
	 * List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
	 * parameterNya.add(new BasicNameValuePair("no_sppd", no_sppd));
	 * parameterNya.add(new BasicNameValuePair("uraian_3",uraian_3));
	 * 
	 * Log.d("Request ke server!", "dimulai"); JSONParser ambil_classJSONParser
	 * = new JSONParser(); JSONObject jsonObjectNya =
	 * ambil_classJSONParser.makeHttpRequest(hapus_rincian_riil_3, "POST",
	 * parameterNya); Log.d("Try Again", jsonObjectNya.toString());
	 * 
	 * berhasil = jsonObjectNya.getInt(TAG_BERHASIL); if (berhasil == 1) {
	 * Log.d("Sukses !!!", jsonObjectNya.toString()); return
	 * jsonObjectNya.getString(TAG_PESAN); } else { Log.d("Failed !!!",
	 * jsonObjectNya.getString(TAG_PESAN)); return
	 * jsonObjectNya.getString(TAG_PESAN); } } catch (JSONException e) {
	 * e.printStackTrace(); }
	 * 
	 * return null;
	 * 
	 * }
	 * 
	 * protected void onPostExecute(String url_registrasi_nya) { // matikan
	 * progressBar-nya setelah selesai di gunakan dialog.dismiss(); if
	 * (url_registrasi_nya != null) { Toast.makeText(Laporan_Riil.this,
	 * url_registrasi_nya, Toast.LENGTH_LONG).show();
	 * 
	 * }
	 * 
	 * }
	 * 
	 * }
	 */

}
