package com.e_sppd_rssm;

//import java.sql.Date;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.e_sppd.rssm.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;
import koneksi.Daftar_String;
import koneksi.JSONParser;
import koneksi.Koneksi;

@SuppressLint({ "NewApi", "SimpleDateFormat" })
public class Sppd extends AppCompatActivity implements OnQueryTextListener {
	private Handler handler = new Handler();

	private static final String simpan_data_sppdbaru = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/simpan_data_sppdbaru.php";
	private static final String TAG = "Sppd";
	private Spinner spin_JnsKendaraan, edit_akun;
	private EditText edit_nippeg, edit_namapeg, edit_PdanG, edit_JaborIns,
			edit_BiayaPerjDinas, edit_Tujuan, edit_AwalBerangkat,
			edit_AkhirBerangkat, edit_lamaperj, edit_TglBerangkat,
			edit_TglKembali, pengikut1, pengikut2, pengikut3, pengikut4,
			pengikut5, edit_nmrsrt1, edit_nmrsrt2, edit_di_keluarkan,
			edit_tgldi_keluarkan;

	int jam, menit, tahun, bulan, hari;
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	// private String[] arrMonth = { "Januari", "Februari", "Maret", "April",
	// "Mei",
	// "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"
	// };
	private String[] varBulan = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12" };
	private RelativeLayout RelativePengikut;
	private Button btn_tampilrelative, btn_tutuprelative, btn_refresh_sppd,
			btn_simpan;
	private TextView nip_lokal, tgl_aktiviti, jam_aktiviti, nmrsrt3,
			TextView_ambil_nomor_surat, nomor_spt_yg_telah_disetujui,
			jml_petugas_spt;

	// --------- PRIVATE BERHUBUNGAN DATABASE -------------
	private ProgressDialog ProgressDialog1;
	private ListView listView;
	private Koneksi Koneksi_Server;
	private List<Daftar_String> list;
	private List_menampilkan_data_spt adapter_baru;
	private SearchManager SearchManager;
	private SearchView pencarian_searc_nm_peawai;
	private Daftar_String selectedList;
	private static final String TAG_BERHASIL = "sukses";
	private static final String TAG_PESAN = "pesan";
	public static final String nippegawai = "nip";

	private RelativeLayout rel_list_gone;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sppd);
		handler.postDelayed(runnable, 1000);

		nmrsrt3 						= (TextView) findViewById(R.id.nmrsrt3);
		jml_petugas_spt 				= (TextView) findViewById(R.id.jml_petugas_spt);
		TextView_ambil_nomor_surat 		= (TextView) findViewById(R.id.TextView_ambil_nomor_surat);
		nomor_spt_yg_telah_disetujui 	= (TextView) findViewById(R.id.nomor_spt_yg_telah_disetujui);

		SearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		pencarian_searc_nm_peawai = (SearchView) findViewById(
				R.id.searc_nm_pegawai_sppd).getParent();
		pencarian_searc_nm_peawai
				.setQueryHint("Pencarian Nomor SPT/Nama Petugas");
		pencarian_searc_nm_peawai.setSearchableInfo(SearchManager
				.getSearchableInfo(getComponentName()));
		pencarian_searc_nm_peawai.setOnQueryTextListener(this);

		edit_nippeg 		= (EditText) findViewById(R.id.edit_nippeg);
		edit_namapeg 		= (EditText) findViewById(R.id.edit_namapeg);
		edit_PdanG 			= (EditText) findViewById(R.id.edit_PdanG);
		edit_JaborIns 		= (EditText) findViewById(R.id.edit_JaborIns);

		edit_BiayaPerjDinas = (EditText) findViewById(R.id.edit_BiayaPerjDinas);
		edit_Tujuan 		= (EditText) findViewById(R.id.edit_Tujuan);
		edit_AwalBerangkat 	= (EditText) findViewById(R.id.edit_AwalBerangkat);
		edit_AkhirBerangkat = (EditText) findViewById(R.id.edit_AkhirBerangkat);
		edit_lamaperj 		= (EditText) findViewById(R.id.edit_lamaperj);
		edit_TglBerangkat 	= (EditText) findViewById(R.id.edit_TglBerangkat);
		edit_TglKembali 	= (EditText) findViewById(R.id.edit_TglKembali);
		edit_nmrsrt1 		= (EditText) findViewById(R.id.edit_nmrsrt1);
		edit_nmrsrt2 		= (EditText) findViewById(R.id.edit_nmrsrt2);
		edit_nmrsrt2.requestFocus(View.FOCUS_RIGHT);

		edit_di_keluarkan 		= (EditText) findViewById(R.id.edit_di_keluarkan);
		edit_tgldi_keluarkan 	= (EditText) findViewById(R.id.edit_tgldi_keluarkan);
		edit_akun 				= (Spinner) findViewById(R.id.edit_akun);
		spin_JnsKendaraan 		= (Spinner) findViewById(R.id.spin_JnsKendaraan);
		edit_nmrsrt1.setText("094");

		ArrayAdapter<CharSequence> adapterakun = ArrayAdapter
				.createFromResource(this, R.array.akun,
						android.R.layout.simple_spinner_item);
		adapterakun
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_akun.setAdapter(adapterakun);

		ArrayAdapter<CharSequence> adapterkendaraan = ArrayAdapter
				.createFromResource(this, R.array.kendaraan,
						android.R.layout.simple_spinner_item);

		adapterkendaraan
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_JnsKendaraan.setAdapter(adapterkendaraan);

		final Calendar c = Calendar.getInstance();
		tahun = c.get(Calendar.YEAR);
		bulan = c.get(Calendar.MONTH);
		hari = c.get(Calendar.DAY_OF_MONTH);

		edit_TglBerangkat.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub

				if (edit_lamaperj.getText().toString().trim().isEmpty()) {
					Toast.makeText(Sppd.this,
							"Lama Perjalanan Dinas Belum Diisi",
							Toast.LENGTH_LONG).show();
				} else {
					showDialog(DATE_DIALOG_ID);
				}
				return true;
			}
		});

		RelativePengikut = (RelativeLayout) findViewById(R.id.RelativePengikut);
		pengikut1 = (EditText) findViewById(R.id.pengikut1);
		pengikut2 = (EditText) findViewById(R.id.pengikut2);
		pengikut3 = (EditText) findViewById(R.id.pengikut3);
		pengikut4 = (EditText) findViewById(R.id.pengikut4);
		pengikut5 = (EditText) findViewById(R.id.pengikut5);

		btn_tampilrelative = (Button) findViewById(R.id.btn_tampilrelative);
		btn_tutuprelative = (Button) findViewById(R.id.btn_tutuprelative);
		btn_refresh_sppd = (Button) findViewById(R.id.btn_refresh_sppd);
		btn_simpan = (Button) findViewById(R.id.btn_simpan);

		btn_tampilrelative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_tutuprelative.setVisibility(View.VISIBLE);
				btn_tampilrelative.setVisibility(View.GONE);
				RelativePengikut.setVisibility(View.VISIBLE);
				EditText pengikut1 = (EditText) findViewById(R.id.pengikut1);
				pengikut1.requestFocus(View.FOCUSABLES_ALL);
			}
		});

		btn_tutuprelative.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btn_tutuprelative.setVisibility(View.GONE);
				btn_tampilrelative.setVisibility(View.VISIBLE);
				pengikut1.setText("");
				pengikut2.setText("");
				pengikut3.setText("");
				pengikut4.setText("");
				pengikut5.setText("");
				RelativePengikut.setVisibility(View.GONE);
			}
		});
		nip_lokal = (TextView) findViewById(R.id.nip_lokal);
		tgl_aktiviti = (TextView) findViewById(R.id.tgl_aktiviti);
		jam_aktiviti = (TextView) findViewById(R.id.jam_aktiviti);

		Bundle b = getIntent().getExtras();

		String transfer_nip = b.getString("transfer_nip");
		String nomor_spt = b.getString("nomor_spt");
		String ambil_spt_kota = b.getString("ambil_spt_kota");
		String tgl_spt_dikeluarkan = b.getString("tgl_dikeluarkan");
		String lama_perj_spt = b.getString("lama_perj_spt");
		String tgl_berangkat_spt = b.getString("tgl_berangkat_spt");
		String tgl_tiba_spt = b.getString("tgl_tiba_spt");
		String jml_prtugas = b.getString("jml_prtugas");
		String lokasi_keberangkatan = b.getString("lokasi_keberangkatan");
		String lokasi_tiba = b.getString("lokasi_tiba");
		String maksud_perj = b.getString("maksud_perj");
		String alat_angkutan = b.getString("alat_angkutan");
		// String akun = b.getString("akun");

		// pencarian_searc_nm_peawai.setQuery(nomor_spt,true);

		nip_lokal.setText(transfer_nip);
		nomor_spt_yg_telah_disetujui.setText(nomor_spt);
		edit_di_keluarkan.setText(ambil_spt_kota);
		edit_tgldi_keluarkan.setText(tgl_spt_dikeluarkan);
		edit_lamaperj.setText(lama_perj_spt);
		edit_TglBerangkat.setText(tgl_berangkat_spt);
		edit_TglKembali.setText(tgl_tiba_spt);
		jml_petugas_spt.setText(jml_prtugas);
		edit_Tujuan.setText(maksud_perj);
		edit_AwalBerangkat.setText(lokasi_keberangkatan);
		edit_AkhirBerangkat.setText(lokasi_tiba);
		// edit_akun.setText(akun);

		Koneksi_Server = new Koneksi();
		listView = (ListView) findViewById(R.id.list_view_activitysppd);

		list = new ArrayList<Daftar_String>();

		String cek_no_spt = nomor_spt_yg_telah_disetujui.getText().toString();
		if (cek_no_spt.isEmpty()) {
			// pertanyaan_ada_tidak_nomor_spt();
			popUp_question();
		} else {
			// pencarian_searc_nm_peawai.setQuery(cek_no_spt, true);
			edit_lamaperj.setEnabled(false);
			edit_TglBerangkat.setEnabled(false);
			edit_TglKembali.setEnabled(false);
			new MainActivityAsync().execute("load");

		}

		rel_list_gone = (RelativeLayout) findViewById(R.id.ambil_listviewnm);

		pencarian_searc_nm_peawai
				.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						rel_list_gone.setVisibility(View.VISIBLE);
						listView.setVisibility(View.VISIBLE);
					}
				});
		edit_Tujuan.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				listView.setVisibility(View.GONE);
				rel_list_gone.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				listView.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
			}
		});

		edit_lamaperj.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				edit_TglBerangkat.setText("");
				edit_TglKembali.setText("");
				listView.setVisibility(View.GONE);
				rel_list_gone.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				listView.setVisibility(View.GONE);
			}

			@Override
			public void afterTextChanged(Editable s) {
				validasi_lama_perj(edit_lamaperj);
			}
		});

		btn_simpan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!terkoneksi(Sppd.this)) {
					Toast.makeText(Sppd.this, "Not Connected",
							Toast.LENGTH_LONG).show();
					return;
				} else if ((edit_nippeg.length() < 1)
						|| (edit_Tujuan.length() < 1)
						|| (edit_AwalBerangkat.length() < 1)
						|| (edit_AkhirBerangkat.length() < 1)
						|| (edit_lamaperj.length() < 1)
						|| (edit_TglBerangkat.length() < 1)
						|| (edit_di_keluarkan.length() < 1)
						|| (edit_tgldi_keluarkan.length() < 1)) {
					Toast.makeText(
							Sppd.this,
							"Ups,.. Data Tidak Boleh Kosong \n Mohon Diperiksa Kembali",
							Toast.LENGTH_LONG).show();
					return;
				} else if (edit_akun.getSelectedItem().toString()
						.contentEquals("-Pilih-")) {
					Toast.makeText(
							Sppd.this,
							"Ups,.. Akun Pembebanan Anggaran Belum Dipilih \nMohon Diperiksa Kembali",
							Toast.LENGTH_LONG).show();
					return;
				} else {
					pertanyaan();
					return;
				}
			}
		});
	}

	private void popUp_question() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pertanyaan_cek_no_spt);
		Button btn_kembali = (Button) dialog.findViewById(R.id.btn_kembali);
		Button btn_lanjut_ke_spt = (Button) dialog
				.findViewById(R.id.btn_lanjut_ke_spt);
		Button btn_lanjut_ke_sppd = (Button) dialog
				.findViewById(R.id.btn_lanjut_ke_sppd);

		dialog.show();

		btn_kembali.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				Sppd.this.finish();
				finish();
			}
		});

		btn_lanjut_ke_spt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Sppd.this.finish();
				finish();
				String nip2 = nip_lokal.getText().toString();
				Intent i = null;
				i = new Intent(Sppd.this, Pembuatan_SPT.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("transfer_nip", nip2);

				i.putExtras(Bundle);
				startActivity(i);

			}
		});

		btn_lanjut_ke_sppd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				new MainActivityAsync().execute("load");

			}
		});

	}

	private void pertanyaan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Simpan Data Lembar 1 SPPD ?");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				String cek_jml_petugas = jml_petugas_spt.getText().toString();

				if (cek_jml_petugas.contains("1")) {
					new Menyimpan_serta_Cek_Nomor_surat().execute();
				} else {
					pertanyaan_lanjutan();
				}
				// Simpan_Data_SPPDBARU(nip, biaya_perj, maksud_perj,
				// alat_angkutan, tempat_brngkt, tempat_tujuan, lama_perj,
				// tgl_brngkt, tgl_kembali, tambh_pengikut1, tambh_pengikut2,
				// tambh_pengikut3, tambh_pengikut4, tambh_pengikut5,
				// tgl_aktivitas, jam_aktivitas,ambil_nomor_surat,
				// nip_petugas_admin);

				// Sppd.this.finish();
				// finish();

				// String nip_transfer = nip_lokal.getText().toString();

				// Toast.makeText(getApplicationContext(),
				// "Sukses", Toast.LENGTH_LONG).show();
				// menu_refresh();
				// Intent i = new Intent(Sppd.this, MainActivityBaru_Admin.class);
				// i.putExtra(nippegawai, nip_transfer);
				// startActivity(i);
				// finish();
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

	private void pertanyaan_lanjutan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Buat Lagi dengan Nomor SPT yang sama ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Sppd.this.finish();
				finish();

				String transfer_nip = nip_lokal.getText().toString().trim();
				String nomor_spt = nomor_spt_yg_telah_disetujui.getText()
						.toString().trim();
				String ambil_spt_kota = edit_di_keluarkan.getText().toString()
						.trim();
				String tgl_spt_dikeluarkan = edit_tgldi_keluarkan.getText()
						.toString().trim();
				String lama_perj_spt = edit_lamaperj.getText().toString()
						.trim();
				String maksud_perj = edit_Tujuan.getText().toString().trim();
				String alat_angkutan = spin_JnsKendaraan.getSelectedItem()
						.toString().trim();
				String tempat_brngkt = edit_AwalBerangkat.getText().toString()
						.trim();
				String tempat_tujuan = edit_AkhirBerangkat.getText().toString()
						.trim();
				String tgl_brngkt = edit_TglBerangkat.getText().toString()
						.trim();
				String tgl_kembali = edit_TglKembali.getText().toString()
						.trim();
				String jml_petugas = jml_petugas_spt.getText().toString()
						.trim();
				String akun = edit_akun.getSelectedItem().toString().trim();

				// BELUM SELSAI

				new Menyimpan_serta_Cek_Nomor_surat().execute();
				Intent i = null;
				i = new Intent(Sppd.this, Sppd.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("nomor_spt", nomor_spt);

				Bundle.putString("transfer_nip", transfer_nip);
				Bundle.putString("ambil_spt_kota", ambil_spt_kota);
				Bundle.putString("tgl_dikeluarkan", tgl_spt_dikeluarkan);
				Bundle.putString("lama_perj_spt", lama_perj_spt);
				Bundle.putString("tgl_berangkat_spt", tgl_brngkt);
				Bundle.putString("tgl_tiba_spt", tgl_kembali);
				Bundle.putString("jml_prtugas", jml_petugas);
				Bundle.putString("lokasi_keberangkatan", tempat_brngkt);
				Bundle.putString("lokasi_tiba", tempat_tujuan);
				Bundle.putString("maksud_perj", maksud_perj);
				Bundle.putString("alat_angkutan", alat_angkutan);
				Bundle.putString("akun", akun);

				i.putExtras(Bundle);
				startActivity(i);

				/*
				 * TextView_ambil_nomor_surat.setText(ambil_nomor_spt);
				 * nip_lokal.setText(nip_petugas_admin);
				 * edit_Tujuan.setText(maksud_perj);
				 * spin_JnsKendaraan.equals(alat_angkutan);
				 * edit_AwalBerangkat.setText(tempat_brngkt);
				 * edit_AkhirBerangkat.setText(tempat_tujuan);
				 * edit_lamaperj.setText(lama_perj);
				 * edit_TglBerangkat.setText(tgl_brngkt);
				 * edit_TglKembali.setText(tgl_kembali);
				 * pengikut1.setText(tambh_pengikut1);
				 * pengikut2.setText(tambh_pengikut2);
				 * pengikut3.setText(tambh_pengikut3);
				 * pengikut4.setText(tambh_pengikut4);
				 * pengikut5.setText(tambh_pengikut5);
				 * tgl_aktiviti.setText(tgl_aktivitas);
				 * jam_aktiviti.setText(jam_aktivitas);
				 */

			}
		});
		ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				new Menyimpan_serta_Cek_Nomor_surat().execute();
				// Toast.makeText(Sppd.this,
				// "Ups,.. Batal Simpan Data", Toast.LENGTH_LONG)
				// .show();
			}
		});
		ad.show();
	}

	class Menyimpan_serta_Cek_Nomor_surat extends
			AsyncTask<String, String, String> {

		/**
		 * saat setelah tekan tombol registrasi tunjukanlah progressBar kepada
		 * *pengguna agar ia tahu aplikasi sedang apa
		 * */
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog1 = new ProgressDialog(Sppd.this);
			ProgressDialog1.setMessage("Loading ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(true);
			ProgressDialog1.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;
			String ambil_nomor_spt = nomor_spt_yg_telah_disetujui.getText()
					.toString().trim();
			String ambil_nomor_surat = TextView_ambil_nomor_surat.getText()
					.toString().trim();
			String nip = edit_nippeg.getText().toString().trim();
			String nip_petugas_admin = nip_lokal.getText().toString().trim();
			String biaya_perj = edit_BiayaPerjDinas.getText().toString().trim();
			String maksud_perj = edit_Tujuan.getText().toString().trim();
			String alat_angkutan = spin_JnsKendaraan.getSelectedItem()
					.toString().trim();
			String tempat_brngkt = edit_AwalBerangkat.getText().toString()
					.trim();
			String tempat_tujuan = edit_AkhirBerangkat.getText().toString()
					.trim();
			String lama_perj = edit_lamaperj.getText().toString().trim();
			String tgl_brngkt = edit_TglBerangkat.getText().toString().trim();
			String tgl_kembali = edit_TglKembali.getText().toString().trim();
			String tambh_pengikut1 = pengikut1.getText().toString().trim();
			String tambh_pengikut2 = pengikut2.getText().toString().trim();
			String tambh_pengikut3 = pengikut3.getText().toString().trim();
			String tambh_pengikut4 = pengikut4.getText().toString().trim();
			String tambh_pengikut5 = pengikut5.getText().toString().trim();
			String tgl_aktivitas = tgl_aktiviti.getText().toString().trim();
			String jam_aktivitas = jam_aktiviti.getText().toString().trim();
			String akun = edit_akun.getSelectedItem().toString().trim();

			try {
				// Cocokan parameternya yah 'username ke username dan
				// password ke password
				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("ambil_nomor_spt",
						ambil_nomor_spt));
				parameterNya.add(new BasicNameValuePair("ambil_nomor_surat",
						ambil_nomor_surat));
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair("nip_petugas_admin",
						nip_petugas_admin));
				parameterNya.add(new BasicNameValuePair("biaya_perj",
						biaya_perj));
				parameterNya.add(new BasicNameValuePair("maksud_perj",
						maksud_perj));
				parameterNya.add(new BasicNameValuePair("alat_angkutan",
						alat_angkutan));
				parameterNya.add(new BasicNameValuePair("tempat_brngkt",
						tempat_brngkt));
				parameterNya.add(new BasicNameValuePair("tempat_tujuan",
						tempat_tujuan));
				parameterNya
						.add(new BasicNameValuePair("lama_perj", lama_perj));
				parameterNya.add(new BasicNameValuePair("tgl_brngkt",
						tgl_brngkt));
				parameterNya.add(new BasicNameValuePair("tgl_kembali",
						tgl_kembali));
				parameterNya.add(new BasicNameValuePair("tambh_pengikut1",
						tambh_pengikut1));
				parameterNya.add(new BasicNameValuePair("tambh_pengikut2",
						tambh_pengikut2));
				parameterNya.add(new BasicNameValuePair("tambh_pengikut3",
						tambh_pengikut3));
				parameterNya.add(new BasicNameValuePair("tambh_pengikut4",
						tambh_pengikut4));
				parameterNya.add(new BasicNameValuePair("tambh_pengikut5",
						tambh_pengikut5));
				parameterNya.add(new BasicNameValuePair("tgl_aktivitas",
						tgl_aktivitas));
				parameterNya.add(new BasicNameValuePair("jam_aktivitas",
						jam_aktivitas));
				parameterNya.add(new BasicNameValuePair("akun", akun));

				Log.d("Request ke server!", "dimulai");
				JSONParser ambil_classJSONParser = new JSONParser();
				// kirim data dari user ke script di server
				JSONObject jsonObjectNya = ambil_classJSONParser
						.makeHttpRequest(simpan_data_sppdbaru, "POST",
								parameterNya);

				// json response-nya
				Log.d("Try Again", jsonObjectNya.toString());

				// json berhasil
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

		/**
		 * kalau sudah selesai tugas background_nya matikan progressbar_nya
		 * **/
		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			// matikan progressBar-nya setelah selesai di gunakan
			ProgressDialog1.dismiss();
			Toast.makeText(Sppd.this, url_registrasi_nya, Toast.LENGTH_LONG)
					.show();

		}

	}

	/*
	 * private void Simpan_Datsa_SPPDBARU(String nip, String biaya_perj, String
	 * maksud_perj, String alat_angkutan, String tempat_brngkt, String
	 * tempat_tujuan, String lama_perj, String tgl_brngkt, String tgl_kembali,
	 * String tambh_pengikut1, String tambh_pengikut2, String
	 * tambh_pengikut3,String tambh_pengikut4,String tambh_pengikut5, String
	 * tgl_aktivitas, String jam_aktivitas, String ambil_nomor_surat, String
	 * nip_petugas_admin) {
	 * 
	 * class Input_Baru extends AsyncTask<String, Void, String> {
	 * 
	 * ProgressDialog tampilloading; Java_Connection ruc = new
	 * Java_Connection();
	 * 
	 * @Override protected void onPreExecute() { super.onPreExecute();
	 * tampilloading = ProgressDialog.show(Sppd.this, "", "Loading...", true,
	 * true); }
	 * 
	 * @Override protected void onPostExecute(String s) {
	 * super.onPostExecute(s); tampilloading.dismiss();
	 * 
	 * kembali_keMENU_AWAL();
	 * 
	 * }
	 * 
	 * @Override protected String doInBackground(String... params) {
	 * HashMap<String, String> data = new HashMap<String, String>();
	 * 
	 * data.put("nip", params[0]); data.put("biaya_perj", params[1]);
	 * data.put("maksud_perj", params[2]); data.put("alat_angkutan", params[3]);
	 * data.put("tempat_brngkt", params[4]); data.put("tempat_tujuan",
	 * params[5]); data.put("lama_perj", params[6]); data.put("tgl_brngkt",
	 * params[7]); data.put("tgl_kembali", params[8]);
	 * data.put("tambh_pengikut1", params[9]); data.put("tambh_pengikut2",
	 * params[10]); data.put("tambh_pengikut3", params[11]);
	 * data.put("tambh_pengikut4", params[12]); data.put("tambh_pengikut5",
	 * params[13]); data.put("tgl_aktiviti", params[14]);
	 * data.put("jam_aktiviti", params[15]); data.put("ambil_nomor_surat",
	 * params[16]); data.put("nip_petugas_admin", params[17]);
	 * 
	 * String result = ruc.sendPostRequest(simpan_data_sppdbaru, data); return
	 * result;
	 * 
	 * } }
	 * 
	 * Input_Baru ru = new Input_Baru(); ru.execute(nip, biaya_perj,
	 * maksud_perj, alat_angkutan, tempat_brngkt, tempat_tujuan, lama_perj,
	 * tgl_brngkt, tgl_kembali, tambh_pengikut1, tambh_pengikut2,
	 * tambh_pengikut3, tambh_pengikut4, tambh_pengikut5, tgl_aktivitas,
	 * jam_aktivitas, ambil_nomor_surat, nip_petugas_admin ); }
	 */

	public boolean validasi_lama_perj(EditText editText) {

		edit_lamaperj.setError(null);
		// length 0 means there is no text
		if (edit_lamaperj.length() == 0) {
			editText.setError(Html
					.fromHtml("<font color='white', style='background:#FFFFFF'>Lama Perjalanan Tidak Boleh Kosong</font>"));
			return false;
		}
		return true;
	}

	public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}

	private class MainActivityAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			ProgressDialog1 = new ProgressDialog(Sppd.this);
			ProgressDialog1.setMessage("Loading...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(false);
			ProgressDialog1.show();
		}

		@Override
		protected String doInBackground(String... params) {

			/** Mengirimkan request ke server dan memproses JSON response */
			// String nip_pegawai = nip_lokal.getText().toString().trim();

			// String Cek = String.valueOf(nip_pegawai);
			// String url;

			// Super PENTINGGGGGGGGGGGGGGGGGGGGGG PUOOLLLLLLLLLL
			// Barokallah Alhamdulilah KETEMU CARANE MENGATASI
			// DATA NIP YANG BERSIFAT VARCHAR DENGAN SPASI BANYAK
			// try {

			// url = Koneksi_Server.sendGetRequest(Koneksi.profil_pegawai +
			// "?nip_pegawai="
			// + URLEncoder.encode(Cek, "UTF-8"));
			// list = proses_pengambilan_data(url);

			// }catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			// }

			/** Mengirimkan request ke server dan memproses JSON response */
			String response = Koneksi_Server
					.sendGetRequest(Koneksi.tampil_data_nip_berdasarkan_spt);
			list = proses_pengambilan_data(response);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			ProgressDialog1.dismiss();
			runOnUiThread(new Runnable() {
				@Override
				public void run() {

					menampilkan_nama_pegawai();

				}
			});
		}

	}

	private List<Daftar_String> proses_pengambilan_data(String response) {
		List<Daftar_String> list_Daftar_String = new ArrayList<Daftar_String>();
		try {
			JSONObject jsonObj = new JSONObject(response);
			JSONArray jsonArray = jsonObj.getJSONArray("tampil_data_nip");
			Log.d(TAG, "data lengt: " + jsonArray.length());
			Daftar_String mhs = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				mhs = new Daftar_String();
				mhs.setnomor_SPT(obj.getString("nomor_spt"));
				mhs.setnip(obj.getString("nip"));
				mhs.setnama_pegawai(obj.getString("nama_pegawai"));
				mhs.setjabatan(obj.getString("jabatan"));
				mhs.setgolongan(obj.getString("golongan"));
				mhs.setjml_petugas(obj.getString("jml_petugas"));
				mhs.setlokasi_dikeluarkan(obj.getString("dikeluarkan"));
				mhs.settgl_dikeluarkan(obj.getString("tgl_dikeluarkan"));
				mhs.setlama_perj(obj.getString("lama_pelaksanaan"));
				mhs.settgl_brngkt(obj.getString("tgl_berangkat"));
				mhs.settgl_kembali(obj.getString("tgl_tiba"));

				list_Daftar_String.add(mhs);
			}
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		return list_Daftar_String;
	}

	private void menampilkan_nama_pegawai() {
		if (!terkoneksi(Sppd.this)) {
			Toast.makeText(Sppd.this, "Not Connected", Toast.LENGTH_LONG)
					.show();
			return;
		} else {

			adapter_baru = new List_menampilkan_data_spt(
					getApplicationContext(), list);
			listView.setAdapter(adapter_baru);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> adapterView, View v,
						int pos, long id) {
					selectedList = (Daftar_String) adapter_baru.getItem(pos);

					if (pencarian_searc_nm_peawai.getQuery().toString()
							.isEmpty()) {
						Toast.makeText(
								Sppd.this,
								"Anda Belum Melakukan Pencarian NIP/NIK atau Nama Pegawai atau Nomor SPT Petugas yang Ditugaskan ",
								Toast.LENGTH_LONG).show();
					} else if (pencarian_searc_nm_peawai.getQuery().toString()
							.length() > 1) { // lebihdari

						if ((edit_nmrsrt1.getText().toString().isEmpty())
								|| (edit_nmrsrt2.getText().toString().isEmpty())) {
							Toast.makeText(Sppd.this, "Nomor SPPD Wajib Diisi",
									Toast.LENGTH_LONG).show();
						} else {
							tampil_data();
							listView.setVisibility(View.GONE);
							rel_list_gone.setVisibility(View.GONE);
							pencarian_searc_nm_peawai.setQuery("", true);
						}

					}

				}
			});

		}
	}

	private void tampil_data() {
		nomor_spt_yg_telah_disetujui.setText(selectedList.getnomor_SPT());
		edit_nippeg.setText(selectedList.getnip());
		edit_namapeg.setText(selectedList.getnama_pegawai());
		edit_PdanG.setText(selectedList.getgolongan());
		edit_JaborIns.setText(selectedList.getjabatan());

		jml_petugas_spt.setText(selectedList.getjml_petugas());
		edit_di_keluarkan.setText(selectedList.getlokasi_dikeluarkan());
		edit_tgldi_keluarkan.setText(selectedList.gettgl_dikeluarkan());

		edit_lamaperj.setText(selectedList.getlama_perj());
		edit_TglBerangkat.setText(selectedList.gettgl_brngkt());
		edit_TglKembali.setText(selectedList.gettgl_kembali());

		edit_lamaperj.setEnabled(false);
		edit_TglBerangkat.setEnabled(false);
		edit_TglKembali.setEnabled(false);

		String ambil_TextView_ambil_nomor_surat = edit_nmrsrt1.getText()
				.toString().trim()
				+ "/"
				+ edit_nmrsrt2.getText().toString().trim()
				+ nmrsrt3.getText().toString().trim();

		TextView_ambil_nomor_surat.setText(ambil_TextView_ambil_nomor_surat);
	}

	private boolean terkoneksi(Context mContext) {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			Toast.makeText(
					getApplication(),
					"Koneksi Internet Anda : " + netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
			return true;

		} else if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			Toast.makeText(
					getApplication(),
					"Koneksi Internet Anda Sedang Proses"
							+ netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
			return true;
		} else if (netInfo != null && netInfo.isRoaming()) {
			Toast.makeText(
					getApplication(),
					"Jaringan Internet Roaming" + netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
			return true;

		} else if (netInfo != null && netInfo.isAvailable()) {
			Toast.makeText(
					getApplication(),
					"Masih Mercari Jaringan" + netInfo.getTypeName() + " "
							+ netInfo.getSubtypeName(), Toast.LENGTH_SHORT)
					.show();
		}
		return false;

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, tahun, bulan,
					hari);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			tahun = year;
			bulan = monthOfYear;
			hari = dayOfMonth;
			// String sdate = arrMonth[bulan] + " " + LPad(hari + "", "0", 2)
			// + ", " + tahun;

			// String sdate_TglBerangkat= LPad(hari + "", "0", 2) + " " +
			// arrMonth[bulan]
			// + " " + tahun;
			String koncersibulan = LPad(hari + "", "0", 2) + "-"
					+ varBulan[bulan] + "-" + tahun;

			edit_TglBerangkat.setText(koncersibulan);
			edit_TglKembali.setEnabled(false);

			// PROSES PENJUMLAHAN ANTAR INPUTAN TANGGAL DAN LAMA HARI

			String ambil_edit_TglBerangkat = edit_TglBerangkat.getText()
					.toString();
			String amnil_edit_lamaperj = edit_lamaperj.getText().toString();

			DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			try {
				Date d1 = df.parse(ambil_edit_TglBerangkat);
				// String ambil = String.valueOf(df.format(d1));

				Locale lokal = null;
				String pola = "dd-MM-yyyy";
				// Date waktuPermulaan = new Date();

				int jmlTambahanWaktu = Integer.parseInt(amnil_edit_lamaperj);
				String satuan = "hari";

				// Menambah hari
				Calendar cal = MenambahMengurangDate.tambahWaktu(d1,
						jmlTambahanWaktu - 1, satuan);

				String tanggalStr = MenambahMengurangDate
						.tampilkanTanggalDanWaktu(cal.getTime(), pola, lokal);

				// edit_TglKembali.setText("("+
				// MenambahMengurangDate.tampilkanTanggalDanWaktu(d1, pola,
				// lokal) + ") ditambah "+ jmlTambahanWaktu + " " + satuan +
				// " = " + tanggalStr);
				edit_TglKembali.setText(tanggalStr);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// /---------------------------//-------------------------------------//
		}
	};

	private static String LPad(String schar, String spad, int len) {
		String sret = schar;
		for (int i = sret.length(); i < len; i++) {
			sret = spad + sret;
		}
		return new String(sret);
	}

	// Contoh KONVERSI
	public class konversi {
		public void main(String[] args) {
			String data_string = "10";
			int data_integer = Integer.valueOf(data_string);
			long data_long = Long.valueOf(data_string);
			float data_float = Float.valueOf(data_string);
			double data_double = Integer.valueOf(data_string);
			boolean data_boolean = Boolean.valueOf(data_string);

			System.out.println("\ndata_string = " + data_string
					+ " ke data_integer = " + data_integer);
			System.out.println("\ndata_string = " + data_string
					+ " ke data_long = " + data_long);
			System.out.println("\ndata_string = " + data_string
					+ " ke data_float = " + data_float);
			System.out.println("\ndata_string = " + data_string
					+ " ke data_double = " + data_double);
			System.out.println("\ndata_string = " + data_string
					+ " ke data_boolean = " + data_boolean);
		}
	}// Batas Contoh KONVERSI
		// ------------------------------------------------------------------//

	// Salah Satu Fungsi Menambah Tanggal, Bulan Tahun Jam, Menit , Milisecond,
	// Detik
	public static class MenambahMengurangDate {

		protected static Calendar tambahWaktu(Date waktuPermulaan,
				int jmlTambahanWaktu, String satuan) {
			/*
			 * Untuk mengurangi hari gunakan nilai minus (-) pada
			 * jmlTambahanWaktu
			 */
			Calendar cal = Calendar.getInstance();
			cal.setTime(waktuPermulaan);
			if (satuan.equals("hari")) {
				cal.add(Calendar.DATE, jmlTambahanWaktu);

			} else if (satuan.equals("bulan")) {
				cal.add(Calendar.MONTH, jmlTambahanWaktu);

			} else if (satuan.equals("tahun")) {
				cal.add(Calendar.YEAR, jmlTambahanWaktu);

			} else if (satuan.equals("jam")) {
				cal.add(Calendar.HOUR, jmlTambahanWaktu);

			} else if (satuan.equals("menit")) {
				cal.add(Calendar.MINUTE, jmlTambahanWaktu);

			} else if (satuan.equals("detik")) {
				cal.add(Calendar.SECOND, jmlTambahanWaktu);

			} else if (satuan.equals("milidetik")) {
				cal.add(Calendar.MILLISECOND, jmlTambahanWaktu);

			}
			return cal;
		}

		// Menampilkan Format Tanggal
		protected static String tampilkanTanggalDanWaktu(Date tanggalDanWaktu,
				String pola, Locale lokal) {
			String tanggalStr;
			SimpleDateFormat formatter;
			if (lokal == null) {
				formatter = new SimpleDateFormat(pola);
			} else {
				formatter = new SimpleDateFormat(pola, lokal);
			}
			tanggalStr = formatter.format(tanggalDanWaktu);
			return tanggalStr;
		}
	}// BATAS ::: Salah Satu Fungsi Menambah Tanggal, Bulan Tahun Jam, Menit ,
		// Milisecond, Detik --------------------------------//

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		infodialogback();
	}

	private void infodialogback() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Anda akan membatalkan LAPORAN SPPD RSSM");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Sppd.this.finish();
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

			SimpleDateFormat tgl_skrng = new SimpleDateFormat("yyyy/M/d");
			SimpleDateFormat jam_skrng = new SimpleDateFormat("h:m:s");
			SimpleDateFormat thn_skrng = new SimpleDateFormat("yyyy");
			// SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");
			String strdate_tgl = tgl_skrng.format(c1.getTime());
			String strdate_jam = jam_skrng.format(c1.getTime());
			String strdate_thn_skrng = thn_skrng.format(c1.getTime());

			tgl_aktiviti.setText(strdate_tgl);
			jam_aktiviti.setText(strdate_jam);

			nmrsrt3.setText("/303/" + strdate_thn_skrng);
			handler.postDelayed(this, 1000);
		}

	};

	private void back_to_menu() {

		String nip_transfer = nip_lokal.getText().toString();
		Intent i = null;

		i = new Intent(Sppd.this, Main_Activity_Admin.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Bundle b = new Bundle();
		b.putString(nippegawai, nip_transfer);
		i.putExtras(b);
		finish();
		startActivity(i);
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		adapter_baru.getFilter().filter(newText);
		return true;
	}
}