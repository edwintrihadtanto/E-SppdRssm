package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.SearchView.OnQueryTextListener;

import com.e_sppd.rssm.R;
import com.e_sppd_rssm.Sppd.MenambahMengurangDate;

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

import koneksi.Daftar_String;
import koneksi.JSONParser;
import koneksi.Koneksi;
@SuppressLint({ "NewApi", "SimpleDateFormat" })
@SuppressWarnings("unused")
public class Pembuatan_SPT extends AppCompatActivity implements OnQueryTextListener {
	private static final String simpan_data_spt = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/simpan_data_SPT.php";

	private Handler handler = new Handler();
	public CheckBox checkbox_pemberi_perintah;
	private Spinner spin_jml_petugas, edit_pemberi_perintah;
	private RelativeLayout Layout_1, Layout_2, Layout_3, Layout_4, Layout_5,
			Layout_6, Layout_7, Layout_8, Layout_9, Layout_10, lay_kepada,
			lay_untuk;
	private ProgressDialog ProgressDialog1;
	private ListView listView;
	private Koneksi Koneksi_Server;
	private List<Daftar_String> list;
	private List_Tampil_NIP adapter;
	private SearchManager SearchManager;
	private SearchView cari_nama_pegawai;
	private Daftar_String selectedList;
	private static final String TAG = "Pembuatan_SPT";
	public static final String nomor_spt = "SPT";
	private TextView nmrsrt3, TextView_ambil_nomor_surat_spt, nip_lokal;

	private EditText edit_nmrsrt_spt1, edit_nmrsrt_spt2, edit_didapat_spt,
			edit_diperoleh_tgl, edit_dasar_spt, edit_untuk,
			edit_lama_perjalanan, edit_tgl_pelaksanaan_dimulai_tgl,
			edit_tgl_pelaksanaan_sampai_tgl, edit_dikeluarkan, edit_pada_tgl,
			edit_nip_pemberi_perintah, edit_atas_nama, edit_atas_namadua, nama,
			nip, pangkat, jabatan, nama2, nip2, pangkat2, jabatan2, nama3,
			nip3, pangkat3, jabatan3, nama4, nip4, pangkat4, jabatan4, nama5,
			nip5, pangkat5, jabatan5, nama6, nip6, pangkat6, jabatan6, nama7,
			nip7, pangkat7, jabatan7, nama8, nip8, pangkat8, jabatan8, nama9,
			nip9, pangkat9, jabatan9, nama10, nip10, pangkat10, jabatan10,
			nip11;
	private Button btn_simpan_spt;
	String Change_Nama = "";
	String Change_Nip = "";
	String Change_Pangkat = "";
	String Change_jab = "";
	private static final String TAG_BERHASIL = "sukses";
	private static final String TAG_PESAN = "pesan";
	public static final String nippegawai = "nip";

	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	static final int DATE_DIALOG_ID_2 = 2;
	static final int DATE_DIALOG_ID_3 = 3;
	private String[] varBulan = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12" };
	int jam, menit, tahun, bulan, hari;

	@SuppressLint("WrongViewCast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_surat_perintah_tugas);
		handler.postDelayed(runnable, 1000);

		checkbox_pemberi_perintah = (CheckBox) findViewById(R.id.cekbox_pemberi_perintah);
		nmrsrt3 = (TextView) findViewById(R.id.nmrsrt3);
		// Parsing data NIP LOGIN
		nip_lokal = (TextView) findViewById(R.id.nip_lokal);
		Bundle b = getIntent().getExtras();
		String transfer_nip = b.getString("transfer_nip");
		nip_lokal.setText(transfer_nip);

		TextView_ambil_nomor_surat_spt = (TextView) findViewById(R.id.TextView_ambil_nomor_surat_spt);
		edit_nmrsrt_spt1 = (EditText) findViewById(R.id.edit_nmrsrt_spt1);
		edit_nmrsrt_spt2 = (EditText) findViewById(R.id.edit_nmrsrt_spt2);
		edit_didapat_spt = (EditText) findViewById(R.id.edit_didapat_spt);
		edit_diperoleh_tgl = (EditText) findViewById(R.id.edit_diperoleh_tgl);
		edit_dasar_spt = (EditText) findViewById(R.id.edit_dasar_spt);
		edit_untuk = (EditText) findViewById(R.id.edit_untuk);
		edit_lama_perjalanan = (EditText) findViewById(R.id.edit_lama_perjalanan);
		edit_tgl_pelaksanaan_dimulai_tgl = (EditText) findViewById(R.id.edit_tgl_pelaksanaan_dimulai_tgl);
		edit_tgl_pelaksanaan_sampai_tgl = (EditText) findViewById(R.id.edit_tgl_pelaksanaan_sampai_tgl);
		edit_dikeluarkan = (EditText) findViewById(R.id.edit_dikeluarkan);
		edit_pada_tgl = (EditText) findViewById(R.id.edit_pada_tgl);
		spin_jml_petugas = (Spinner) findViewById(R.id.spin_jml_petugas);
		edit_pemberi_perintah = (Spinner) findViewById(R.id.edit_pemberi_perintah);
		edit_nip_pemberi_perintah = (EditText) findViewById(R.id.edit_nip_pemberi_perintah);
		edit_atas_nama = (EditText) findViewById(R.id.edit_atas_namasatu);
		edit_atas_namadua = (EditText) findViewById(R.id.edit_atas_namadua);

		edit_dikeluarkan.setText("Madiun");
		// edit_pemberi_perintah.setText("M. SUCAHYONO, SKM.M.Kes");
		// edit_nip_pemberi_perintah.setText("19730227 199903 1 003");

		btn_simpan_spt = (Button) findViewById(R.id.btn_simpan_spt);

		Layout_1 = (RelativeLayout) findViewById(R.id.layout_1);
		Layout_2 = (RelativeLayout) findViewById(R.id.layout_2);
		Layout_3 = (RelativeLayout) findViewById(R.id.layout_3);
		Layout_4 = (RelativeLayout) findViewById(R.id.layout_4);
		Layout_5 = (RelativeLayout) findViewById(R.id.layout_5);
		Layout_6 = (RelativeLayout) findViewById(R.id.layout_6);
		Layout_7 = (RelativeLayout) findViewById(R.id.layout_7);
		Layout_8 = (RelativeLayout) findViewById(R.id.layout_8);
		Layout_9 = (RelativeLayout) findViewById(R.id.layout_9);
		Layout_10 = (RelativeLayout) findViewById(R.id.layout_10);

		lay_kepada = (RelativeLayout) findViewById(R.id.lay_kepada);
		lay_untuk = (RelativeLayout) findViewById(R.id.lay_untuk);

		// Layout 1
		nama = (EditText) findViewById(R.id.text_namapetugas_layout_1);
		nip = (EditText) findViewById(R.id.text_nippetugas_layout_1);
		pangkat = (EditText) findViewById(R.id.text_pangkatpetugas_layout_1);
		jabatan = (EditText) findViewById(R.id.text_jabatanpetugas_layout_1);
		// Layout 2
		nama2 = (EditText) findViewById(R.id.text_namapetugas_layout_2);
		nip2 = (EditText) findViewById(R.id.text_nippetugas_layout_2);
		pangkat2 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_2);
		jabatan2 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_2);
		// Layout 3
		nama3 = (EditText) findViewById(R.id.text_namapetugas_layout_3);
		nip3 = (EditText) findViewById(R.id.text_nippetugas_layout_3);
		pangkat3 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_3);
		jabatan3 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_3);
		// Layout 4
		nama4 = (EditText) findViewById(R.id.text_namapetugas_layout_4);
		nip4 = (EditText) findViewById(R.id.text_nippetugas_layout_4);
		pangkat4 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_4);
		jabatan4 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_4);
		// Layout 5
		nama5 = (EditText) findViewById(R.id.text_namapetugas_layout_5);
		nip5 = (EditText) findViewById(R.id.text_nippetugas_layout_5);
		pangkat5 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_5);
		jabatan5 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_5);
		// Layout 6
		nama6 = (EditText) findViewById(R.id.text_namapetugas_layout_6);
		nip6 = (EditText) findViewById(R.id.text_nippetugas_layout_6);
		pangkat6 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_6);
		jabatan6 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_6);
		// Layout 7
		nama7 = (EditText) findViewById(R.id.text_namapetugas_layout_7);
		nip7 = (EditText) findViewById(R.id.text_nippetugas_layout_7);
		pangkat7 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_7);
		jabatan7 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_7);
		// Layout 8
		nama8 = (EditText) findViewById(R.id.text_namapetugas_layout_8);
		nip8 = (EditText) findViewById(R.id.text_nippetugas_layout_8);
		pangkat8 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_8);
		jabatan8 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_8);
		// Layout 9
		nama9 = (EditText) findViewById(R.id.text_namapetugas_layout_9);
		nip9 = (EditText) findViewById(R.id.text_nippetugas_layout_9);
		pangkat9 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_9);
		jabatan9 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_9);
		// Layout 10
		nama10 = (EditText) findViewById(R.id.text_namapetugas_layout_10);
		nip10 = (EditText) findViewById(R.id.text_nippetugas_layout_10);
		pangkat10 = (EditText) findViewById(R.id.text_pangkatpetugas_layout_10);
		jabatan10 = (EditText) findViewById(R.id.text_jabatanpetugas_layout_10);

		nip11 = (EditText) findViewById(R.id.text_nippetugas_layout_11);

		SearchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		cari_nama_pegawai = (SearchView) findViewById(R.id.cari_nama_pegawai).getParent();
		cari_nama_pegawai
				.setQueryHint("Pencarian Nama Pegawai yang Ditugaskan");
		cari_nama_pegawai.setSearchableInfo(SearchManager
				.getSearchableInfo(getComponentName()));
		cari_nama_pegawai.setOnQueryTextListener(this);

		ArrayAdapter<CharSequence> adapter_ttd = ArrayAdapter
				.createFromResource(this, R.array.ttd,
						android.R.layout.simple_spinner_dropdown_item);
		adapter_ttd
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		edit_pemberi_perintah.setAdapter(adapter_ttd);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.jml_petugas_yg_ditugaskan,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spin_jml_petugas.setAdapter(adapter);

		spin_jml_petugas
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int position, long id) {
						// TODO Auto-generated method stub
						String ambil_spinner = spin_jml_petugas
								.getSelectedItem().toString();
						if (ambil_spinner.equals("1")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.GONE);
							Layout_3.setVisibility(View.GONE);
							Layout_4.setVisibility(View.GONE);
							Layout_5.setVisibility(View.GONE);
							Layout_6.setVisibility(View.GONE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);

						} else if (ambil_spinner.equals("2")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.GONE);
							Layout_4.setVisibility(View.GONE);
							Layout_5.setVisibility(View.GONE);
							Layout_6.setVisibility(View.GONE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);

						} else if (ambil_spinner.equals("3")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.GONE);
							Layout_5.setVisibility(View.GONE);
							Layout_6.setVisibility(View.GONE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("4")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.GONE);
							Layout_6.setVisibility(View.GONE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("5")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.VISIBLE);
							Layout_6.setVisibility(View.GONE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("6")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.VISIBLE);
							Layout_6.setVisibility(View.VISIBLE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("7")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.VISIBLE);
							Layout_6.setVisibility(View.VISIBLE);
							Layout_7.setVisibility(View.VISIBLE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("8")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.VISIBLE);
							Layout_6.setVisibility(View.VISIBLE);
							Layout_7.setVisibility(View.VISIBLE);
							Layout_8.setVisibility(View.VISIBLE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("9")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.VISIBLE);
							Layout_6.setVisibility(View.VISIBLE);
							Layout_7.setVisibility(View.VISIBLE);
							Layout_8.setVisibility(View.VISIBLE);
							Layout_9.setVisibility(View.VISIBLE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else if (ambil_spinner.equals("10")) {
							Layout_1.setVisibility(View.VISIBLE);
							Layout_2.setVisibility(View.VISIBLE);
							Layout_3.setVisibility(View.VISIBLE);
							Layout_4.setVisibility(View.VISIBLE);
							Layout_5.setVisibility(View.VISIBLE);
							Layout_6.setVisibility(View.VISIBLE);
							Layout_7.setVisibility(View.VISIBLE);
							Layout_8.setVisibility(View.VISIBLE);
							Layout_9.setVisibility(View.VISIBLE);
							Layout_10.setVisibility(View.VISIBLE);
							lay_kepada.setVisibility(View.VISIBLE);
							lay_untuk.setVisibility(View.VISIBLE);
						} else {
							Layout_1.setVisibility(View.GONE);
							Layout_2.setVisibility(View.GONE);
							Layout_3.setVisibility(View.GONE);
							Layout_4.setVisibility(View.GONE);
							Layout_5.setVisibility(View.GONE);
							Layout_6.setVisibility(View.GONE);
							Layout_7.setVisibility(View.GONE);
							Layout_8.setVisibility(View.GONE);
							Layout_9.setVisibility(View.GONE);
							Layout_10.setVisibility(View.GONE);
							lay_kepada.setVisibility(View.GONE);
							lay_untuk.setVisibility(View.GONE);
							Toast.makeText(
									Pembuatan_SPT.this,
									"Ups,...Pilih Jumlah Petugas Yang Telah Disediakan",
									Toast.LENGTH_LONG).show();
						}

						Change();

					}

					@Override
					public void onNothingSelected(AdapterView<?> parent) {
						// TODO Auto-generated method stub

					}

				});

		Koneksi_Server = new Koneksi();
		listView = (ListView) findViewById(R.id.list_view_activitysppd);

		list = new ArrayList<Daftar_String>();
		new MainActivityAsync().execute("load");

		btn_simpan_spt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*
				 * edit_didapat_spt =
				 * (EditText)findViewById(R.id.edit_didapat_spt);
				 * edit_diperoleh_tgl =
				 * (EditText)findViewById(R.id.edit_diperoleh_tgl);
				 * edit_dasar_spt = (EditText)findViewById(R.id.edit_dasar_spt);
				 * edit_untuk = (EditText)findViewById(R.id.edit_untuk);
				 * edit_lama_perjalanan =
				 * (EditText)findViewById(R.id.edit_lama_perjalanan);
				 * edit_tgl_pelaksanaan_dimulai_tgl =
				 * (EditText)findViewById(R.id
				 * .edit_tgl_pelaksanaan_dimulai_tgl);
				 * edit_tgl_pelaksanaan_sampai_tgl =
				 * (EditText)findViewById(R.id.edit_tgl_pelaksanaan_sampai_tgl);
				 * edit_dikeluarkan =
				 * (EditText)findViewById(R.id.edit_dikeluarkan); edit_pada_tgl
				 * = (EditText)findViewById(R.id.edit_pada_tgl);
				 * spin_jml_petugas = (Spinner)
				 * findViewById(R.id.spin_jml_petugas); edit_pemberi_perintah =
				 * (EditText)findViewById(R.id.edit_pemberi_perintah);
				 * edit_nip_pemberi_perintah =
				 * (EditText)findViewById(R.id.edit_nip_pemberi_perintah);
				 */
				if ((edit_nmrsrt_spt1.length() < 1)
						|| (edit_nmrsrt_spt2.length() < 1)) {
					Toast.makeText(Pembuatan_SPT.this,
							"Nomor SPT Tidak Boleh Kosong", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (edit_didapat_spt.getText().toString().isEmpty()
						|| edit_diperoleh_tgl.getText().toString().isEmpty()
						|| edit_dasar_spt.getText().toString().isEmpty()
						|| edit_untuk.getText().toString().isEmpty()
						|| edit_lama_perjalanan.getText().toString().isEmpty()
						|| edit_tgl_pelaksanaan_dimulai_tgl.getText()
								.toString().isEmpty()
						|| edit_tgl_pelaksanaan_sampai_tgl.getText().toString()
								.isEmpty()
						|| edit_dikeluarkan.getText().toString().isEmpty()
						|| edit_pada_tgl.getText().toString().isEmpty())
				// edit_nip_pemberi_perintah.getText().toString().isEmpty()||
				// edit_pemberi_perintah.getSelectedItem().equals("-Pilih-"))
				{
					Toast.makeText(Pembuatan_SPT.this,
							"Data Tidak Boleh Kosong", Toast.LENGTH_LONG)
							.show();
					return;
				} else if (edit_pemberi_perintah.getSelectedItem().equals(
						"-Pilih-")) {
					Toast.makeText(
							Pembuatan_SPT.this,
							"Anda Belum Memilih Petugas Yang Memberikan Perintah",
							Toast.LENGTH_LONG).show();
					return;
				} else {
					pertanyaan_simpan();
					return;
				}

			}
		});

		final Calendar c = Calendar.getInstance();
		tahun = c.get(Calendar.YEAR);
		bulan = c.get(Calendar.MONTH);
		hari = c.get(Calendar.DAY_OF_MONTH);

		edit_diperoleh_tgl.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				String Cek_No_SPT = edit_nmrsrt_spt1.getText().toString()
						+ edit_nmrsrt_spt2.getText().toString();
				if (Cek_No_SPT.isEmpty()) {
					Toast.makeText(Pembuatan_SPT.this, "Nomor SPT Harus Diisi",
							Toast.LENGTH_LONG).show();
				} else {

					showDialog(DATE_DIALOG_ID);
				}
				return true;
			}
		});

		edit_pada_tgl.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				String Cek_hari_dikeluarkan = edit_dikeluarkan.getText()
						.toString();
				if (Cek_hari_dikeluarkan.isEmpty()) {
					Toast.makeText(Pembuatan_SPT.this,
							"Lokasi Dikeluarkan Tidak Boleh Kosong",
							Toast.LENGTH_LONG).show();
				} else {

					showDialog(DATE_DIALOG_ID_2);
				}
				return true;
			}
		});

		edit_tgl_pelaksanaan_dimulai_tgl
				.setOnTouchListener(new OnTouchListener() {

					@SuppressWarnings("deprecation")
					@SuppressLint("ClickableViewAccessibility")
					@Override
					public boolean onTouch(View arg0, MotionEvent arg1) {
						// TODO Auto-generated method stub

						if (edit_lama_perjalanan.getText().toString().trim()
								.isEmpty()) {
							Toast.makeText(Pembuatan_SPT.this,
									"Lama Perjalanan Dinas Belum Diisi",
									Toast.LENGTH_LONG).show();
						} else {
							showDialog(DATE_DIALOG_ID_3);
						}
						return true;
					}
				});

		edit_lama_perjalanan.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				edit_tgl_pelaksanaan_dimulai_tgl.setText("");
				edit_tgl_pelaksanaan_sampai_tgl.setText("");
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				validasi_lama_perj(edit_lama_perjalanan);
			}
		});

		checkbox_pemberi_perintah
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						// checkbox status is changed from uncheck to checked.
						if (isChecked) {
							pertanyaan_ganti_nama_pemberi_perintah();
						} else if (!isChecked) {
							// edit_pemberi_perintah.setText("M. SUCAHYONO, SKM.M.Kes");
							// edit_nip_pemberi_perintah.setText("19730227 199903 1 003");
						} else {
							Toast.makeText(Pembuatan_SPT.this, "Cancel",
									Toast.LENGTH_LONG).show();
						}
					}
				});

	}

	public boolean validasi_lama_perj(EditText editText) {

		edit_lama_perjalanan.setError(null);
		// length 0 means there is no text
		if (edit_lama_perjalanan.length() == 0) {
			editText.setError(Html
					.fromHtml("<font color='white', style='background:#FFFFFF'>Lama Perjalanan Tidak Boleh Kosong</font>"));
			return false;
		}
		return true;
	}

	private void pertanyaan_ganti_nama_pemberi_perintah() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Ganti Nama Pemberi Perintah Surat Perintah Tugas ... ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				edit_nip_pemberi_perintah.setText("");
				// edit_pemberi_perintah.setText("");
				// edit_pemberi_perintah.setEnabled(true);
				edit_nip_pemberi_perintah.setEnabled(true);
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

	private void pertanyaan_simpan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Data Akan Di Simpan \n"
				+ "Mohon Periksa Data SPT Sekali Lagi ??? \n___________________________\n"
				+ "Jumlah Petugas : "
				+ spin_jml_petugas.getSelectedItem().toString()
				+ "\nDaftar Petugas yg Ditugaskan :\n\n"
				+ nama.getText().toString() + "\n" + nama2.getText().toString()
				+ "\n" + nama3.getText().toString() + "\n"
				+ nama4.getText().toString() + "\n"
				+ nama5.getText().toString() + "\n"
				+ nama6.getText().toString() + "\n"
				+ nama7.getText().toString() + "\n"
				+ nama8.getText().toString() + "\n"
				+ nama9.getText().toString() + "\n"
				+ nama10.getText().toString());

		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();

			}
		});
		ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

				String ambil_TextView_ambil_nomor_surat = edit_nmrsrt_spt1
						.getText().toString().trim()
						+ "/"
						+ edit_nmrsrt_spt2.getText().toString().trim()
						+ nmrsrt3.getText().toString().trim();
				String cek_nip1 = nip.getText().toString().trim();
				String cek_nip2 = nip2.getText().toString().trim();
				String cek_nip3 = nip3.getText().toString().trim();
				String cek_nip4 = nip4.getText().toString().trim();
				String cek_nip5 = nip5.getText().toString().trim();
				String cek_nip6 = nip6.getText().toString().trim();
				String cek_nip7 = nip7.getText().toString().trim();
				String cek_nip8 = nip8.getText().toString().trim();
				String cek_nip9 = nip9.getText().toString().trim();
				String cek_nip10 = nip10.getText().toString().trim();
				String cek_nip11 = nip11.getText().toString().trim();

				String jml_petugas = spin_jml_petugas.getSelectedItem()
						.toString().trim();

				TextView_ambil_nomor_surat_spt
						.setText(ambil_TextView_ambil_nomor_surat);

				if (jml_petugas.equals("1") && cek_nip1.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \n\nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("1") && cek_nip1.length() > 1
						&& cek_nip2.contentEquals("")) {
					pertanyaan_lanjut_input_lembar1SPPD();
					// ---------------------------------
				} else if (jml_petugas.equals("2")
						&& cek_nip2.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("2") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();
					// ---------------------------------
				} else if (jml_petugas.equals("3")
						&& cek_nip3.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("3") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();
					// ---------------------------------
				} else if (jml_petugas.equals("4")
						&& cek_nip4.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("4") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();
					// ---------------------------------
				} else if (jml_petugas.equals("5")
						&& cek_nip5.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("5") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.length() > 1
						&& cek_nip6.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();

				} else if (jml_petugas.equals("6")
						&& cek_nip6.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("6") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.length() > 1
						&& cek_nip6.length() > 1 && cek_nip7.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();

				} else if (jml_petugas.equals("7")
						&& cek_nip7.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("7") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.length() > 1
						&& cek_nip6.length() > 1 && cek_nip7.length() > 1
						&& cek_nip8.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();

				} else if (jml_petugas.equals("8")
						&& cek_nip8.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("8") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.length() > 1
						&& cek_nip6.length() > 1 && cek_nip7.length() > 1
						&& cek_nip8.length() > 1 && cek_nip9.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();

				} else if (jml_petugas.equals("9")
						&& cek_nip9.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("9") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.length() > 1
						&& cek_nip6.length() > 1 && cek_nip7.length() > 1
						&& cek_nip8.length() > 1 && cek_nip9.length() > 1
						&& cek_nip10.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();

				} else if (jml_petugas.equals("10")
						&& cek_nip10.contentEquals("")) {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups... Data Petugas yg Ditugaskan Masih Ada yg Kosong \nPastikan Jumlah Petugas sesuai dengan Petugas yg Ditugaskan",
							Toast.LENGTH_LONG).show();

				} else if (jml_petugas.equals("10") && cek_nip1.length() > 1
						&& cek_nip2.length() > 1 && cek_nip3.length() > 1
						&& cek_nip4.length() > 1 && cek_nip5.length() > 1
						&& cek_nip6.length() > 1 && cek_nip7.length() > 1
						&& cek_nip8.length() > 1 && cek_nip9.length() > 1
						&& cek_nip10.length() > 1
						&& cek_nip11.contentEquals("")) {

					pertanyaan_lanjut_input_lembar1SPPD();

				} else {
					dialog.dismiss();
					Toast.makeText(
							Pembuatan_SPT.this,
							"Ups..Data Petugas yg Ditugaskan Tidak Sesuai dengan Jumlah Petugas yg Dipilih",
							Toast.LENGTH_LONG).show();
				}

			}
		});
		ad.show();
	}

	private void pertanyaan_lanjut_input_lembar1SPPD() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Lanjut Pengolahan Lembar 1 SPPD ");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Menyimpan_serta_Cek_Nomor_surat().execute();
				Pembuatan_SPT.this.finish();
				finish();
				String ambil_spt_kota = edit_dikeluarkan.getText().toString();
				String ambil_spt_nomor = TextView_ambil_nomor_surat_spt
						.getText().toString();
				String ambil_spt_tgl_dikeluarkan = edit_pada_tgl.getText()
						.toString();
				String lama_perj_spt = edit_lama_perjalanan.getText()
						.toString();
				String tgl_berangkat_spt = edit_tgl_pelaksanaan_dimulai_tgl
						.getText().toString();
				String tgl_tiba_spt = edit_tgl_pelaksanaan_sampai_tgl.getText()
						.toString();
				String jml_prtugas = spin_jml_petugas.getSelectedItem()
						.toString();

				Intent i = null;
				i = new Intent(Pembuatan_SPT.this, Sppd.class);
				Bundle Bundle = new Bundle();
				Bundle.putString("nomor_spt", ambil_spt_nomor);
				Bundle.putString("ambil_spt_kota", ambil_spt_kota);
				Bundle.putString("tgl_dikeluarkan", ambil_spt_tgl_dikeluarkan);
				Bundle.putString("lama_perj_spt", lama_perj_spt);
				Bundle.putString("tgl_berangkat_spt", tgl_berangkat_spt);
				Bundle.putString("tgl_tiba_spt", tgl_tiba_spt);
				Bundle.putString("jml_prtugas", jml_prtugas);
				i.putExtras(Bundle);
				startActivity(i);

			}
		});
		ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				new Menyimpan_serta_Cek_Nomor_surat().execute();
				Pembuatan_SPT.this.finish();
				finish();
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
			ProgressDialog1 = new ProgressDialog(Pembuatan_SPT.this);
			ProgressDialog1.setMessage("Loading ...");
			ProgressDialog1.setIndeterminate(false);
			ProgressDialog1.setCancelable(true);
			ProgressDialog1.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;

			String nomor_spt = TextView_ambil_nomor_surat_spt.getText()
					.toString().trim();
			String surat_masuk_dari = edit_didapat_spt.getText().toString()
					.trim();
			String tgl_surat_masuk = edit_diperoleh_tgl.getText().toString()
					.trim();
			String dasar = edit_dasar_spt.getText().toString().trim();
			String untuk = edit_untuk.getText().toString().trim();
			String lama_perj = edit_lama_perjalanan.getText().toString().trim();
			String dikeluarkan = edit_dikeluarkan.getText().toString().trim();
			String tgl_dikeluarkan = edit_pada_tgl.getText().toString().trim();
			// String tgl_dikeluarkan =
			// edit_atas_nama.getText().toString().trim();

			String jml_petugas = spin_jml_petugas.getSelectedItem().toString()
					.trim();
			String nip_petugas_admin = nip_lokal.getText().toString().trim();
			String tgl_berangkat = edit_tgl_pelaksanaan_dimulai_tgl.getText()
					.toString().trim();
			String tgl_tiba = edit_tgl_pelaksanaan_sampai_tgl.getText()
					.toString().trim();
			String ambil_nip_petugas_yg_ditugaskan = nip.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan2 = nip2.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan3 = nip3.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan4 = nip4.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan5 = nip5.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan6 = nip6.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan7 = nip7.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan8 = nip8.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan9 = nip9.getText().toString()
					.trim();
			String ambil_nip_petugas_yg_ditugaskan10 = nip10.getText()
					.toString().trim();
			String nip_pemberi_perintah = edit_pemberi_perintah
					.getSelectedItem().toString();
			// String atas_nama = edit_atas_nama.getText().toString().trim(); //
			// TIDAK DIPAKAI
			// String atas_nama_satu =
			// edit_atas_namadua.getText().toString().trim(); // TIDAK DIPAKAI

			try {
				// Cocokan parameternya username ke username dan
				// password ke password

				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya
						.add(new BasicNameValuePair("nomor_spt", nomor_spt));
				parameterNya.add(new BasicNameValuePair("surat_masuk_dari",
						surat_masuk_dari));
				parameterNya.add(new BasicNameValuePair("tgl_surat_masuk",
						tgl_surat_masuk));
				parameterNya.add(new BasicNameValuePair("dasar", dasar));
				parameterNya.add(new BasicNameValuePair("untuk", untuk));
				parameterNya.add(new BasicNameValuePair("lama_pelaksanaan",
						lama_perj));
				parameterNya.add(new BasicNameValuePair("tgl_berangkat",
						tgl_berangkat));
				parameterNya.add(new BasicNameValuePair("tgl_tiba", tgl_tiba));
				parameterNya.add(new BasicNameValuePair("dikeluarkan",
						dikeluarkan));
				parameterNya.add(new BasicNameValuePair("tgl_dikeluarkan",
						tgl_dikeluarkan));
				parameterNya.add(new BasicNameValuePair("jml_petugas",
						jml_petugas));
				parameterNya.add(new BasicNameValuePair("nip_petugas_admin",
						nip_petugas_admin));
				parameterNya.add(new BasicNameValuePair("nip1",
						ambil_nip_petugas_yg_ditugaskan));
				parameterNya.add(new BasicNameValuePair("nip2",
						ambil_nip_petugas_yg_ditugaskan2));
				parameterNya.add(new BasicNameValuePair("nip3",
						ambil_nip_petugas_yg_ditugaskan3));
				parameterNya.add(new BasicNameValuePair("nip4",
						ambil_nip_petugas_yg_ditugaskan4));
				parameterNya.add(new BasicNameValuePair("nip5",
						ambil_nip_petugas_yg_ditugaskan5));
				parameterNya.add(new BasicNameValuePair("nip6",
						ambil_nip_petugas_yg_ditugaskan6));
				parameterNya.add(new BasicNameValuePair("nip7",
						ambil_nip_petugas_yg_ditugaskan7));
				parameterNya.add(new BasicNameValuePair("nip8",
						ambil_nip_petugas_yg_ditugaskan8));
				parameterNya.add(new BasicNameValuePair("nip9",
						ambil_nip_petugas_yg_ditugaskan9));
				parameterNya.add(new BasicNameValuePair("nip10",
						ambil_nip_petugas_yg_ditugaskan10));
				parameterNya.add(new BasicNameValuePair("nip_pemberi_perintah",
						nip_pemberi_perintah)); // Diambil dari SPINNER
				// parameterNya.add(new BasicNameValuePair("atas_nama",
				// atas_nama)); // TIDAK DIPAKAI
				// .add(new BasicNameValuePair("atas_nama_bawah",
				// atas_nama_satu)); // TIDAK DIPAKAI

				Log.d("Request ke server!", "dimulai");
				JSONParser ambil_classJSONParser = new JSONParser();
				// kirim data dari user ke script di server
				JSONObject jsonObjectNya = ambil_classJSONParser
						.makeHttpRequest(simpan_data_spt, "POST", parameterNya);

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

		@Override
		protected void onPostExecute(String url_registrasi_nya) {
			ProgressDialog1.dismiss();
			if (url_registrasi_nya != null) {
				Toast.makeText(Pembuatan_SPT.this, url_registrasi_nya,
						Toast.LENGTH_LONG).show();
			}

		}

	}

	public void Change() {

		nama.setText(Change_Nama);
		nip.setText(Change_Nip);
		pangkat.setText(Change_Pangkat);
		jabatan.setText(Change_jab);

		nama2.setText(Change_Nama);
		nip2.setText(Change_Nip);
		pangkat2.setText(Change_Pangkat);
		jabatan2.setText(Change_jab);

		nama3.setText(Change_Nama);
		nip3.setText(Change_Nip);
		pangkat3.setText(Change_Pangkat);
		jabatan3.setText(Change_jab);

		nama4.setText(Change_Nama);
		nip4.setText(Change_Nip);
		pangkat4.setText(Change_Pangkat);
		jabatan4.setText(Change_jab);

		nama5.setText(Change_Nama);
		nip5.setText(Change_Nip);
		pangkat5.setText(Change_Pangkat);
		jabatan5.setText(Change_jab);

		nama6.setText(Change_Nama);
		nip6.setText(Change_Nip);
		pangkat6.setText(Change_Pangkat);
		jabatan6.setText(Change_jab);

		nama7.setText(Change_Nama);
		nip7.setText(Change_Nip);
		pangkat7.setText(Change_Pangkat);
		jabatan7.setText(Change_jab);

		nama8.setText(Change_Nama);
		nip8.setText(Change_Nip);
		pangkat8.setText(Change_Pangkat);
		jabatan8.setText(Change_jab);

		nama9.setText(Change_Nama);
		nip9.setText(Change_Nip);
		pangkat9.setText(Change_Pangkat);
		jabatan9.setText(Change_jab);

		nama10.setText(Change_Nama);
		nip10.setText(Change_Nip);
		pangkat10.setText(Change_Pangkat);
		jabatan10.setText(Change_jab);
	}

	private class MainActivityAsync extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			ProgressDialog1 = new ProgressDialog(Pembuatan_SPT.this);
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
					.sendGetRequest(Koneksi.tampil_data_nip_spt);
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
				mhs.setnip(obj.getString("nip"));
				mhs.setnama_pegawai(obj.getString("nama_pegawai"));
				mhs.setjabatan(obj.getString("jabatan"));
				mhs.setgolongan(obj.getString("golongan"));

				list_Daftar_String.add(mhs);
			}
		} catch (JSONException e) {
			Log.d(TAG, e.getMessage());
		}
		return list_Daftar_String;
	}

	private void menampilkan_nama_pegawai() {
		adapter = new List_Tampil_NIP(getApplicationContext(), list);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View v,
					int pos, long id) {
				selectedList = (Daftar_String) adapter.getItem(pos);
				tampil_data();
				cari_nama_pegawai.setQuery("", true);

			}
		});
	}

	private void tampil_data() {
		String cek_nama_petugas1 = nama.getText().toString();
		String cek_nama_petugas2 = nama2.getText().toString();
		String cek_nama_petugas3 = nama3.getText().toString();
		String cek_nama_petugas4 = nama4.getText().toString();
		String cek_nama_petugas5 = nama5.getText().toString();
		String cek_nama_petugas6 = nama6.getText().toString();
		String cek_nama_petugas7 = nama7.getText().toString();
		String cek_nama_petugas8 = nama8.getText().toString();
		String cek_nama_petugas9 = nama9.getText().toString();
		String cek_nama_petugas10 = nama10.getText().toString();

		if (cek_nama_petugas1.contentEquals("")) {
			nama.setText(selectedList.getnama_pegawai());
			nip.setText(selectedList.getnip());
			pangkat.setText(selectedList.getgolongan());
			jabatan.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas1.length() > 1)
				&& (cek_nama_petugas2.contentEquals(""))) {

			nama2.setText(selectedList.getnama_pegawai());
			nip2.setText(selectedList.getnip());
			pangkat2.setText(selectedList.getgolongan());
			jabatan2.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas2.length() > 1)
				&& (cek_nama_petugas3.contentEquals(""))) {

			nama3.setText(selectedList.getnama_pegawai());
			nip3.setText(selectedList.getnip());
			pangkat3.setText(selectedList.getgolongan());
			jabatan3.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas3.length() > 1)
				&& (cek_nama_petugas4.contentEquals(""))) {

			nama4.setText(selectedList.getnama_pegawai());
			nip4.setText(selectedList.getnip());
			pangkat4.setText(selectedList.getgolongan());
			jabatan4.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas4.length() > 1)
				&& (cek_nama_petugas5.contentEquals(""))) {
			nama5.setText(selectedList.getnama_pegawai());
			nip5.setText(selectedList.getnip());
			pangkat5.setText(selectedList.getgolongan());
			jabatan5.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas5.length() > 1)
				&& (cek_nama_petugas6.contentEquals(""))) {
			nama6.setText(selectedList.getnama_pegawai());
			nip6.setText(selectedList.getnip());
			pangkat6.setText(selectedList.getgolongan());
			jabatan6.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas6.length() > 1)
				&& (cek_nama_petugas7.contentEquals(""))) {
			nama7.setText(selectedList.getnama_pegawai());
			nip7.setText(selectedList.getnip());
			pangkat7.setText(selectedList.getgolongan());
			jabatan7.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas7.length() > 1)
				&& (cek_nama_petugas8.contentEquals(""))) {
			nama8.setText(selectedList.getnama_pegawai());
			nip8.setText(selectedList.getnip());
			pangkat8.setText(selectedList.getgolongan());
			jabatan8.setText(selectedList.getjabatan());
		} else if ((cek_nama_petugas8.length() > 1)
				&& (cek_nama_petugas9.contentEquals(""))) {
			nama9.setText(selectedList.getnama_pegawai());
			nip9.setText(selectedList.getnip());
			pangkat9.setText(selectedList.getgolongan());
			jabatan9.setText(selectedList.getjabatan());

		} else if ((cek_nama_petugas9.length() > 1)
				&& (cek_nama_petugas10.contentEquals(""))) {
			nama10.setText(selectedList.getnama_pegawai());
			nip10.setText(selectedList.getnip());
			pangkat10.setText(selectedList.getgolongan());
			jabatan10.setText(selectedList.getjabatan());
		}

	}

	private Runnable runnable = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();

			SimpleDateFormat tgl_skrng = new SimpleDateFormat("d-M-yyyy");
			SimpleDateFormat tgl_skrng2 = new SimpleDateFormat("d MMMM yyyy");
			// SimpleDateFormat jam_skrng = new SimpleDateFormat("h:m:s");
			SimpleDateFormat thn_skrng = new SimpleDateFormat("yyyy");
			SimpleDateFormat sdf1 = new SimpleDateFormat("d/M/yyyy h:m:s a");

			String strdate_thn_skrng = thn_skrng.format(c1.getTime());
			String strdate_tgl_skrng = tgl_skrng.format(c1.getTime());
			String strdate_tgl_skrng2 = tgl_skrng2.format(c1.getTime());

			// edit_atas_nama.setText(strdate_tgl_skrng);//SAYA PAKAI UNTUK
			// TANGGAL SEKARANG
			// edit_pada_tgl.setText(strdate_tgl_skrng2);
			nmrsrt3.setText("/303/" + strdate_thn_skrng);

			handler.postDelayed(this, 1000);
		}

	};

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		// TODO Auto-generated method stub
		adapter.getFilter().filter(newText);
		return true;
	}

	@Override
	public void onBackPressed() {
		infodialogback();
	}

	private void infodialogback() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Anda akan membatalkan Pengolahan Data Surat Perintah Tugas ?");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Pembuatan_SPT.this.finish();
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

	public void refresh(View view) {
		finish();
		startActivity(getIntent());
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, tgl_surat_masuk, tahun, bulan,
					hari);
		case DATE_DIALOG_ID_2:
			return new DatePickerDialog(this, tgl_dikelurkannya_spt, tahun,
					bulan, hari);
		case DATE_DIALOG_ID_3:
			return new DatePickerDialog(this, tgl_berangkat, tahun, bulan, hari);
		}
		return null;
	}

	private DatePickerDialog.OnDateSetListener tgl_surat_masuk = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			tahun = year;
			bulan = monthOfYear;
			hari = dayOfMonth;

			String koncersibulan = LPad(hari + "", "0", 2) + "-"
					+ varBulan[bulan] + "-" + tahun;

			edit_diperoleh_tgl.setText(koncersibulan);

		}
	};

	private DatePickerDialog.OnDateSetListener tgl_dikelurkannya_spt = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			tahun = year;
			bulan = monthOfYear;
			hari = dayOfMonth;

			String koncersibulan = LPad(hari + "", "0", 2) + "-"
					+ varBulan[bulan] + "-" + tahun;

			edit_pada_tgl.setText(koncersibulan);

		}
	};

	private DatePickerDialog.OnDateSetListener tgl_berangkat = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			tahun = year;
			bulan = monthOfYear;
			hari = dayOfMonth;

			String koncersibulan = LPad(hari + "", "0", 2) + "-"
					+ varBulan[bulan] + "-" + tahun;

			edit_tgl_pelaksanaan_dimulai_tgl.setText(koncersibulan);

			// PROSES PENJUMLAHAN ANTAR INPUTAN TANGGAL DAN LAMA HARI

			String ambil_edit_TglBerangkat = edit_tgl_pelaksanaan_dimulai_tgl
					.getText().toString();
			String amnil_edit_lamaperj = edit_lama_perjalanan.getText()
					.toString();

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
				edit_tgl_pelaksanaan_sampai_tgl.setText(tanggalStr);

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

	private static String LPad(String schar, String spad, int len) {
		String sret = schar;
		for (int i = sret.length(); i < len; i++) {
			sret = spad + sret;
		}
		return new String(sret);
	}

}
