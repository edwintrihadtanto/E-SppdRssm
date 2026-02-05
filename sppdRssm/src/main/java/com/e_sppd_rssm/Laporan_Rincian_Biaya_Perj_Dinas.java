package com.e_sppd_rssm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.e_sppd.rssm.R;
import com.ipaulpro.afilechooser.utils.FileUtils;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import koneksi.Java_Connection;
import koneksi.Koneksi;

public class Laporan_Rincian_Biaya_Perj_Dinas extends AppCompatActivity implements
		OnClickListener {
	private static final String TAG = Laporan_Rincian_Biaya_Perj_Dinas.class
			.getSimpleName();

	private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private Uri fileUri; // file url to store image/video

	public Handler handler = new Handler();
	private ProgressDialog dialog_lainnya;
	private TextView edit_lamp_sppd, edit_tgl_lamp, nama_lokal, nip_lokal,
			jab_lokal, infolagi12, textview_grandtotal_riil,
			textview_informasi_riil, status_laporan_petugas, path, status_riil,
			edit_terbilang_total, status_rincian_biaya,
	        btn_upload_ket_1, btn_upload_ket_2, btn_upload_ket_3,
            btn_upload_ket_4, btn_upload_ket_5, btn_upload_ket_6,
            btn_upload_ket_7, btn_upload_ket_8, btn_upload_ket_9,
            btn_upload_ket_10;
	public Button btn_menuju_form_daftar_riil, btn_simpan_rincian_biaya,
  btn_tambh_uraian_upload, btn_upload_image,
			btn_camera;
	private ImageView imgPreview, ImageView_Help,
            img_hapusrincian1, img_hapusrincian2, img_hapusrincian3,
            img_hapusrincian4, img_hapusrincian5, img_hapusrincian6,
            img_hapusrincian7, img_hapusrincian8,
            img_hapusrincian9, img_hapusrincian10;
	private EditText edit_rincian_1, edit_rincian_2, edit_rincian_3,
			edit_rincian_4, edit_rincian_5, edit_rincian_6, edit_rincian_7,
			edit_rincian_8, edit_rincian_9, edit_rincian_10,
			edit_jml_rincian_1, edit_jml_rincian_2, edit_jml_rincian_3,
			edit_jml_rincian_4, edit_jml_rincian_5, edit_jml_rincian_6,
			edit_jml_rincian_7, edit_jml_rincian_8, edit_jml_rincian_9,
			edit_jml_rincian_10, edit_tgl_telah_diterima, edit_grand_total_jml,
			edit_dialog_uraian, edit_textjml_riil;

	private static final String TAG_BERHASIL 				= "sukses";
	private static final String TAG_PESAN 					= "pesan";
	static final int TIME_DIALOG_ID = 0;
	static final int DATE_DIALOG_ID = 1;
	// private String[] varBulan = { "Januari", "Februari", "Maret", "April",
	// "Mei",
	// "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"
	// };
	private final String[] varBulan = { "01", "02", "03", "04", "05", "06", "07",
			"08", "09", "10", "11", "12" };
	int jam, menit, tahun, bulan, hari;

	private static final int REQUEST_CHOOSER_1 = 12345;

	int serverResponseCode = 0;
	ProgressDialog dialog = null;
//	JSONParser classJsonParser = new JSONParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rincian_biaya_dinas);
		handler.postDelayed(runnable, 1000);

		// PROSES PADA MENU LAPORAN RINCIAN BIAYA SEMENTARA BELUM DIGUNAKAN
		status_riil 				= findViewById(R.id.status_riil);
		status_laporan_petugas 		= findViewById(R.id.status_laporan_petugas);
		infolagi12 					= findViewById(R.id.infolagi12);
		nama_lokal 					= findViewById(R.id.nama_lokal);
		nip_lokal 					= findViewById(R.id.nip_lokal);
		jab_lokal 					= findViewById(R.id.jab_lokal);
		edit_lamp_sppd 				= findViewById(R.id.edit_lamp_sppd);
		edit_tgl_lamp 				= findViewById(R.id.edit_tgl_lamp);
		textview_informasi_riil 	= findViewById(R.id.textview_informasi_riil);
		textview_grandtotal_riil 	= findViewById(R.id.textview_grandtotal_riil);
		edit_terbilang_total 		= findViewById(R.id.edit_terbilang_total);
		status_rincian_biaya 		= findViewById(R.id.status_rincian_biaya);
		btn_menuju_form_daftar_riil = findViewById(R.id.btn_menuju_form_daftar_riil);
		btn_simpan_rincian_biaya 	= findViewById(R.id.btn_simpan_rincian_biaya);
		ImageView_Help 				= findViewById(R.id.bantuan_rincian);
		btn_tambh_uraian_upload 	= findViewById(R.id.btn_tambh_uraian_upload);

		//----------------------
        img_hapusrincian1      = findViewById(R.id.img_hapusrincian1);
        img_hapusrincian2      = findViewById(R.id.img_hapusrincian2);
        img_hapusrincian3      = findViewById(R.id.img_hapusrincian3);
        img_hapusrincian4      = findViewById(R.id.img_hapusrincian4);
        img_hapusrincian5      = findViewById(R.id.img_hapusrincian5);
        img_hapusrincian6      = findViewById(R.id.img_hapusrincian6);
        img_hapusrincian7      = findViewById(R.id.img_hapusrincian7);
        img_hapusrincian8      = findViewById(R.id.img_hapusrincian8);
        img_hapusrincian9      = findViewById(R.id.img_hapusrincian9);
        img_hapusrincian10     = findViewById(R.id.img_hapusrincian10);
		// ---------------------
		edit_rincian_1 = findViewById(R.id.edit_rincian_1);
		edit_rincian_2 = findViewById(R.id.edit_rincian_2);
		edit_rincian_3 = findViewById(R.id.edit_rincian_3);
		edit_rincian_4 = findViewById(R.id.edit_rincian_4);
		edit_rincian_5 = findViewById(R.id.edit_rincian_5);
		edit_rincian_6 = findViewById(R.id.edit_rincian_6);
		edit_rincian_7 = findViewById(R.id.edit_rincian_7);
		edit_rincian_8 = findViewById(R.id.edit_rincian_8);
		edit_rincian_9 = findViewById(R.id.edit_rincian_9);
		edit_rincian_10 = findViewById(R.id.edit_rincian_10);

		edit_jml_rincian_1      = findViewById(R.id.edit_jml_rincian_1);
		edit_jml_rincian_2      = findViewById(R.id.edit_jml_rincian_2);
		edit_jml_rincian_3      = findViewById(R.id.edit_jml_rincian_3);
		edit_jml_rincian_4      = findViewById(R.id.edit_jml_rincian_4);
		edit_jml_rincian_5      = findViewById(R.id.edit_jml_rincian_5);
		edit_jml_rincian_6      = findViewById(R.id.edit_jml_rincian_6);
		edit_jml_rincian_7      = findViewById(R.id.edit_jml_rincian_7);
		edit_jml_rincian_8      = findViewById(R.id.edit_jml_rincian_8);
		edit_jml_rincian_9      = findViewById(R.id.edit_jml_rincian_9);
		edit_jml_rincian_10     = findViewById(R.id.edit_jml_rincian_10);
		edit_tgl_telah_diterima = findViewById(R.id.edit_tgl_telah_diterima);
		edit_grand_total_jml    = findViewById(R.id.edit_grand_total_jml);

		btn_upload_ket_1 = findViewById(R.id.btn_upload_ket_1);
		btn_upload_ket_2 = findViewById(R.id.btn_upload_ket_2);
		btn_upload_ket_3 = findViewById(R.id.btn_upload_ket_3);
		btn_upload_ket_4 = findViewById(R.id.btn_upload_ket_4);
		btn_upload_ket_5 = findViewById(R.id.btn_upload_ket_5);
		btn_upload_ket_6 = findViewById(R.id.btn_upload_ket_6);
		btn_upload_ket_7 = findViewById(R.id.btn_upload_ket_7);
		btn_upload_ket_8 = findViewById(R.id.btn_upload_ket_8);
		btn_upload_ket_9 = findViewById(R.id.btn_upload_ket_9);
		btn_upload_ket_10 = findViewById(R.id.btn_upload_ket_10);

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

		edit_grand_total_jml.setEnabled(false);
		// SEMBUNYIKAN BUTTON UPLOAD
		// btn_upload_ket_1.setVisibility(View.GONE);
		// btn_upload_ket_2.setVisibility(View.GONE);
		// btn_upload_ket_3.setVisibility(View.GONE);
		// btn_upload_ket_4.setVisibility(View.GONE);
		// btn_upload_ket_5.setVisibility(View.GONE);
		// btn_upload_ket_6.setVisibility(View.GONE);
		// btn_upload_ket_7.setVisibility(View.GONE);
		// btn_upload_ket_8.setVisibility(View.GONE);
		// btn_upload_ket_9.setVisibility(View.GONE);
		// PARSE INTEGER / PENJUMLAHAN

		// -----------------

		btn_menuju_form_daftar_riil.setOnClickListener(this);
		btn_simpan_rincian_biaya.setOnClickListener(this);		
		btn_tambh_uraian_upload.setOnClickListener(this);

        img_hapusrincian1.setOnClickListener(this);
        img_hapusrincian2.setOnClickListener(this);
        img_hapusrincian3.setOnClickListener(this);
        img_hapusrincian4.setOnClickListener(this);
        img_hapusrincian5.setOnClickListener(this);
        img_hapusrincian6.setOnClickListener(this);
        img_hapusrincian7.setOnClickListener(this);
        img_hapusrincian8.setOnClickListener(this);
        img_hapusrincian9.setOnClickListener(this);
        img_hapusrincian10.setOnClickListener(this);


		// MENAMPILKAN FORM TANGGAL
		final Calendar c = Calendar.getInstance();
		tahun = c.get(Calendar.YEAR);
		bulan = c.get(Calendar.MONTH);
		hari = c.get(Calendar.DAY_OF_MONTH);
		edit_tgl_telah_diterima.setOnTouchListener(new OnTouchListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				showDialog(DATE_DIALOG_ID);
				return true;
			}
		});

		Tampil_data();
		// show_hide_button_upload();

		String cek_status_laporan_petugas = status_laporan_petugas.getText()
				.toString();
		String cek_status_rincian = status_rincian_biaya.getText().toString();

		if (cek_status_laporan_petugas.contains("BELUM")) {
			Toast.makeText(
					Laporan_Rincian_Biaya_Perj_Dinas.this,
					"Ups... Anda Belum Mebuat Laporan Perjalanan Dinas\n"
							+ "\nSilahkan Selesaikan Pembuatan Laporan Perjalanan Dinas Untuk Bisa Mengaktifkan Menu Input Rincian Biaya",
					Toast.LENGTH_LONG).show();
			Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
			finish();
		} else if (cek_status_rincian.contains("SUDAH")) {
			Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
					"Anda Sudah Membuat Laporan Perincian Biaya", Toast.LENGTH_LONG)
					.show();
		} else if (cek_status_rincian.contains("BELUM")) {
			Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
					"Silahkan Melakukan Laporan Perincian Biaya", Toast.LENGTH_LONG)
					.show();
			//Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
			//finish();
		}

		
		ImageView_Help.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//String pesan = "Sipp Mantap";
			//showAlert(pesan);
				Intent i = null;
				i = new Intent(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        Tampil_Bantuan.class);
				startActivity(i);				
			}
		});
	}

	@SuppressLint("DefaultLocale")
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
		String ambilgrand_total_riil = textview_grandtotal_riil.getText()
				.toString();

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
		if (ambilgrand_total_riil.isEmpty()) {
			textview_grandtotal_riil.setText("0");
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
		int grand_total_riil = Integer.parseInt(textview_grandtotal_riil
				.getText().toString());

		String hasil = Integer.toString(jml_1 + jml_2 + jml_3 + jml_4 + jml_5
				+ jml_6 + jml_7 + jml_8 + jml_9 + jml_10 + grand_total_riil);
		edit_grand_total_jml.setText("Rp." + " " + hasil);
		infolagi12.setText(hasil);
		int inputan = Integer.parseInt(hasil);
		String hasil_terbilang = "Terbilang :" + info_terbilang(inputan)
				+ " rupiah";
		String rubah_huruf = hasil_terbilang.toUpperCase();
		edit_terbilang_total.setText(rubah_huruf);
	}

	public void Tampil_data() {
		// DIBAWAH INI DIAMBIL DARI DAFTAR LAPORAN
		String nip = getIntent().getStringExtra("nip");
		String nama = getIntent().getStringExtra("nama_pegawai");
		String jab = getIntent().getStringExtra("jabatan");
		String nomor_sppd = getIntent().getStringExtra("nomor_surat_sppd");
		String tgl_surat_masuk = getIntent().getStringExtra("tgl_surat_masuk");
		String ambil_status_laporan_petugas = getIntent().getStringExtra(
				"status_laporan_petugas");
		String ambil_status_riil = getIntent().getStringExtra("status_riil");
		String ambil_status_rincian = getIntent().getStringExtra(
				"status_rincian_biaya");

		nama_lokal.setText(nip); // POSISI DIBAWAH UTK NIP
		nip_lokal.setText(nama); // POSISI DIATAS UTK NAMA
		jab_lokal.setText(jab);
		edit_lamp_sppd.setText(nomor_sppd);
		edit_tgl_lamp.setText(tgl_surat_masuk);
		status_laporan_petugas.setText(ambil_status_laporan_petugas);
		status_riil.setText(ambil_status_riil);
		status_rincian_biaya.setText(ambil_status_rincian);
		// --------------------

		// --------------------

		// PARSING DARI FORM BIAYA RIIL

		if (edit_lamp_sppd.getText().toString().isEmpty()) {
			Bundle ba = getIntent().getExtras();

			String ambil_nip = ba.getString("nip");
			String ambil_nama = ba.getString("nama");
			String ambil_jab = ba.getString("jab");
			String ambil_sppd = ba.getString("sppd");
			String ambil_tgl_sppd = ba.getString("tgl_sppd");
			String ambil_status_riil_petugas = ba.getString("status_riil");
			String ambil_status_rincian_petugas = ba
					.getString("status_rincian_biaya");

			nama_lokal.setText(ambil_nip); // POSISI DIBAWAH UTK NIP
			nip_lokal.setText(ambil_nama); // POSISI DIATAS UTK NAMA
			jab_lokal.setText(ambil_jab);
			edit_lamp_sppd.setText(ambil_sppd);
			edit_tgl_lamp.setText(ambil_tgl_sppd);
			status_riil.setText(ambil_status_riil_petugas);
			status_rincian_biaya.setText(ambil_status_rincian_petugas);
		}

		Bundle b = getIntent().getExtras();
		String ambil_total_riil = b.getString("ambil_total_riil");
		textview_grandtotal_riil.setText(ambil_total_riil);

		if (textview_grandtotal_riil.getText().toString().isEmpty()
				|| textview_grandtotal_riil.equals("0")) {
			textview_informasi_riil.setVisibility(View.GONE);
			textview_grandtotal_riil.setVisibility(View.GONE);

		} else {
			textview_informasi_riil.setVisibility(View.VISIBLE);
			textview_grandtotal_riil.setVisibility(View.VISIBLE);
			btn_menuju_form_daftar_riil.setVisibility(View.GONE);
		}

		// MENAMPILKAN BUNDLE URAIAN DAN JUMLAH RINCIAN BIAYA
		String ambl_jml1 = b.getString("ambl_jml1");
		String ambl_jml2 = b.getString("ambl_jml2");
		String ambl_jml3 = b.getString("ambl_jml3");
		String ambl_jml4 = b.getString("ambl_jml4");
		String ambl_jml5 = b.getString("ambl_jml5");
		String ambl_jml6 = b.getString("ambl_jml6");
		String ambl_jml7 = b.getString("ambl_jml7");
		String ambl_jml8 = b.getString("ambl_jml8");
		String ambl_jml9 = b.getString("ambl_jml9");
		String ambl_jml10 = b.getString("ambl_jml10");

		String ambil_rincian_1 = b.getString("ambil_rincian_1");
		String ambil_rincian_2 = b.getString("ambil_rincian_2");
		String ambil_rincian_3 = b.getString("ambil_rincian_3");
		String ambil_rincian_4 = b.getString("ambil_rincian_4");
		String ambil_rincian_5 = b.getString("ambil_rincian_5");
		String ambil_rincian_6 = b.getString("ambil_rincian_6");
		String ambil_rincian_7 = b.getString("ambil_rincian_7");
		String ambil_rincian_8 = b.getString("ambil_rincian_8");
		String ambil_rincian_9 = b.getString("ambil_rincian_9");
		String ambil_rincian_10 = b.getString("ambil_rincian_10");

		edit_jml_rincian_1.setText(ambl_jml1);
		edit_jml_rincian_2.setText(ambl_jml2);
		edit_jml_rincian_3.setText(ambl_jml3);
		edit_jml_rincian_4.setText(ambl_jml4);
		edit_jml_rincian_5.setText(ambl_jml5);
		edit_jml_rincian_6.setText(ambl_jml6);
		edit_jml_rincian_7.setText(ambl_jml7);
		edit_jml_rincian_8.setText(ambl_jml8);
		edit_jml_rincian_9.setText(ambl_jml9);
		edit_jml_rincian_10.setText(ambl_jml10);

		edit_rincian_1.setText(ambil_rincian_1);
		edit_rincian_2.setText(ambil_rincian_2);
		edit_rincian_3.setText(ambil_rincian_3);
		edit_rincian_4.setText(ambil_rincian_4);
		edit_rincian_5.setText(ambil_rincian_5);
		edit_rincian_6.setText(ambil_rincian_6);
		edit_rincian_7.setText(ambil_rincian_7);
		edit_rincian_8.setText(ambil_rincian_8);
		edit_rincian_9.setText(ambil_rincian_9);
		edit_rincian_10.setText(ambil_rincian_10);

	}

	@Override
	public void onClick(View view_data) {
		// TODO Auto-generated method stub
		switch (view_data.getId()) {
		case R.id.btn_menuju_form_daftar_riil:
			String cek1 = edit_rincian_1.getText().toString();
			String cek2 = edit_rincian_2.getText().toString();
			if (cek1.length() > 1 || cek2.length() > 1) { // LEBIH DARI 1

				Toast.makeText(
						Laporan_Rincian_Biaya_Perj_Dinas.this,
						"Ups... Selesaikan Terlebih Dahulu Pengisian Rincian Biaya Anda !!! \n"
								+ "Simpan Data Anda Terlebih Dahulu",
						Toast.LENGTH_LONG).show();

			} else if (cek1.length() < 1 || cek2.length() < 1) { // KURANG DARI
																	// 1

				Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
				finish();

				String nip = nip_lokal.getText().toString();
				String nama = nama_lokal.getText().toString();
				String jab = jab_lokal.getText().toString();
				String sppd = edit_lamp_sppd.getText().toString();
				String tgl_sppd = edit_tgl_lamp.getText().toString();
				String cek_status_riil = status_riil.getText().toString();
				String cek_status_rincian = status_rincian_biaya.getText()
						.toString();

				// BAWAH INI PUNYAKNYA LAPORAN RINCIAN
				String ambl_jml1 = edit_jml_rincian_1.getText().toString();
				String ambl_jml2 = edit_jml_rincian_2.getText().toString();
				String ambl_jml3 = edit_jml_rincian_3.getText().toString();
				String ambl_jml4 = edit_jml_rincian_4.getText().toString();
				String ambl_jml5 = edit_jml_rincian_5.getText().toString();
				String ambl_jml6 = edit_jml_rincian_6.getText().toString();
				String ambl_jml7 = edit_jml_rincian_7.getText().toString();
				String ambl_jml8 = edit_jml_rincian_8.getText().toString();
				String ambl_jml9 = edit_jml_rincian_9.getText().toString();
				String ambl_jml10 = edit_jml_rincian_10.getText().toString();

				String ambil_rincian_1 = edit_rincian_1.getText().toString();
				String ambil_rincian_2 = edit_rincian_2.getText().toString();
				String ambil_rincian_3 = edit_rincian_3.getText().toString();
				String ambil_rincian_4 = edit_rincian_4.getText().toString();
				String ambil_rincian_5 = edit_rincian_5.getText().toString();
				String ambil_rincian_6 = edit_rincian_6.getText().toString();
				String ambil_rincian_7 = edit_rincian_7.getText().toString();
				String ambil_rincian_8 = edit_rincian_8.getText().toString();
				String ambil_rincian_9 = edit_rincian_9.getText().toString();
				String ambil_rincian_10 = edit_rincian_10.getText().toString();
				// --------------
				// BAWAH INI PUNYAKNYA LAPORAN RIIL

				Bundle b = getIntent().getExtras();
				String ambl_jml_riil_1 = b.getString("ambl_jml_riil_1");
				String ambl_jml_riil_2 = b.getString("ambl_jml_riil_2");
				String ambl_jml_riil_3 = b.getString("ambl_jml_riil_3");
				String ambl_jml_riil_4 = b.getString("ambl_jml_riil_4");
				String ambl_jml_riil_5 = b.getString("ambl_jml_riil_5");
				String ambl_jml_riil_6 = b.getString("ambl_jml_riil_6");
				String ambl_jml_riil_7 = b.getString("ambl_jml_riil_7");
				String ambl_jml_riil_8 = b.getString("ambl_jml_riil_8");
				String ambl_jml_riil_9 = b.getString("ambl_jml_riil_9");
				String ambl_jml_riil_10 = b.getString("ambl_jml_riil_10");

				String ambil_rincian_riil_1 = b
						.getString("ambil_rincian_riil_1");
				String ambil_rincian_riil_2 = b
						.getString("ambil_rincian_riil_2");
				String ambil_rincian_riil_3 = b
						.getString("ambil_rincian_riil_3");
				String ambil_rincian_riil_4 = b
						.getString("ambil_rincian_riil_4");
				String ambil_rincian_riil_5 = b
						.getString("ambil_rincian_riil_5");
				String ambil_rincian_riil_6 = b
						.getString("ambil_rincian_riil_6");
				String ambil_rincian_riil_7 = b
						.getString("ambil_rincian_riil_7");
				String ambil_rincian_riil_8 = b
						.getString("ambil_rincian_riil_8");
				String ambil_rincian_riil_9 = b
						.getString("ambil_rincian_riil_9");
				String ambil_rincian_riil_10 = b
						.getString("ambil_rincian_riil_10");

//				Intent i = null;
//
//				i = new Intent(Laporan_Rincian_Biaya_Perj_Dinas.this,
//						Laporan_Riil.class);
//
//				Bundle Bundle = new Bundle();
//				Bundle.putString("nama", nip);
//				Bundle.putString("nip", nama);
//				Bundle.putString("jab", jab);
//				Bundle.putString("sppd", sppd);
//				Bundle.putString("tgl_sppd", tgl_sppd);
//				Bundle.putString("status_riil", cek_status_riil);
//				Bundle.putString("status_rincian_biaya", cek_status_rincian);
//
//				Bundle.putString("ambl_jml1", ambl_jml1);
//				Bundle.putString("ambl_jml2", ambl_jml2);
//				Bundle.putString("ambl_jml3", ambl_jml3);
//				Bundle.putString("ambl_jml4", ambl_jml4);
//				Bundle.putString("ambl_jml5", ambl_jml5);
//				Bundle.putString("ambl_jml6", ambl_jml6);
//				Bundle.putString("ambl_jml7", ambl_jml7);
//				Bundle.putString("ambl_jml8", ambl_jml8);
//				Bundle.putString("ambl_jml9", ambl_jml9);
//				Bundle.putString("ambl_jml10", ambl_jml10);
//
//				Bundle.putString("ambil_rincian_1", ambil_rincian_1);
//				Bundle.putString("ambil_rincian_2", ambil_rincian_2);
//				Bundle.putString("ambil_rincian_3", ambil_rincian_3);
//				Bundle.putString("ambil_rincian_4", ambil_rincian_4);
//				Bundle.putString("ambil_rincian_5", ambil_rincian_5);
//				Bundle.putString("ambil_rincian_6", ambil_rincian_6);
//				Bundle.putString("ambil_rincian_7", ambil_rincian_7);
//				Bundle.putString("ambil_rincian_8", ambil_rincian_8);
//				Bundle.putString("ambil_rincian_9", ambil_rincian_9);
//				Bundle.putString("ambil_rincian_10", ambil_rincian_10);
//
//				// -------------------
//				Bundle.putString("ambl_jml_riil_1", ambl_jml_riil_1);
//				Bundle.putString("ambl_jml_riil_2", ambl_jml_riil_2);
//				Bundle.putString("ambl_jml_riil_3", ambl_jml_riil_3);
//				Bundle.putString("ambl_jml_riil_4", ambl_jml_riil_4);
//				Bundle.putString("ambl_jml_riil_5", ambl_jml_riil_5);
//				Bundle.putString("ambl_jml_riil_6", ambl_jml_riil_6);
//				Bundle.putString("ambl_jml_riil_7", ambl_jml_riil_7);
//				Bundle.putString("ambl_jml_riil_8", ambl_jml_riil_8);
//				Bundle.putString("ambl_jml_riil_9", ambl_jml_riil_9);
//				Bundle.putString("ambl_jml_riil_10", ambl_jml_riil_10);
//
//				Bundle.putString("ambil_rincian_riil_1", ambil_rincian_riil_1);
//				Bundle.putString("ambil_rincian_riil_2", ambil_rincian_riil_2);
//				Bundle.putString("ambil_rincian_riil_3", ambil_rincian_riil_3);
//				Bundle.putString("ambil_rincian_riil_4", ambil_rincian_riil_4);
//				Bundle.putString("ambil_rincian_riil_5", ambil_rincian_riil_5);
//				Bundle.putString("ambil_rincian_riil_6", ambil_rincian_riil_6);
//				Bundle.putString("ambil_rincian_riil_7", ambil_rincian_riil_7);
//				Bundle.putString("ambil_rincian_riil_8", ambil_rincian_riil_8);
//				Bundle.putString("ambil_rincian_riil_9", ambil_rincian_riil_9);
//				Bundle.putString("ambil_rincian_riil_10", ambil_rincian_riil_10);
//
//				i.putExtras(Bundle);
//				startActivity(i);

			}

			break;

		case R.id.btn_simpan_rincian_biaya:
			pertanyaan_simpan();
			break;
		//case R.id.btn_refresh:
			// finish();
			// startActivity(getIntent());
		//	Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
		//			"Refresh Data Sukses", Toast.LENGTH_SHORT).show();
		//	break;
		case R.id.btn_tambh_uraian_upload:
            String ambil_jml_edittext = edit_rincian_10.getText().toString();
            if (ambil_jml_edittext.length()>1) {
                String pesan = "Pengisian Uraian Sudah Maksimal\nUntuk Penambahan Uraian Lebih Dari 10 Silahkan Anda Simpan Dahulu,\n" +
                        "Kemudian Anda Tambahkan Pada Proses Edit Perincian Data ";
                //   Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                //     pesan, Toast.LENGTH_SHORT).show();
                informasi(pesan);

            }else{
                dialog_pop_up_tambah_uraian();
            }

			break;

        case R.id.img_hapusrincian1:
           img_hapusrincian1.setOnClickListener(v ->
					konfirmasiHapus(
							edit_rincian_1,
							edit_jml_rincian_1,
                            (Button) btn_upload_ket_1
                    )
			);
            break;

		case R.id.img_hapusrincian2:
            img_hapusrincian2.setOnClickListener(v ->
//				pertanyaan_hapus_button_dua();
				konfirmasiHapus(
						edit_rincian_2,
						edit_jml_rincian_2,
						(Button) btn_upload_ket_2
				)

			);
			break;
		case R.id.img_hapusrincian3:
			img_hapusrincian3.setOnClickListener(v ->

//					pertanyaan_hapus_button_tiga();
					konfirmasiHapus(
							edit_rincian_3,
							edit_jml_rincian_3,
							(Button) btn_upload_ket_3
					)

			);
			break;
		/*case R.id.img_hapusrincian4:
            img_hapusrincian4.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_empat();
				}
			});
			break;
		case R.id.img_hapusrincian5:
            img_hapusrincian5.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_lima();
				}
			});
			break;
		case R.id.img_hapusrincian6:
            img_hapusrincian6.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_enam();

                }
			});
			break;
		case R.id.img_hapusrincian7:
            img_hapusrincian7.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_tujuh();
				}
			});
			break;
		case R.id.img_hapusrincian8:
            img_hapusrincian8.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_delapan();
				}
			});
			break;
		case R.id.img_hapusrincian9:
            img_hapusrincian9.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_sembilan();
				}
			});
			break;
		case R.id.img_hapusrincian10:
            img_hapusrincian10.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
					pertanyaan_hapus_button_sepuluh();
				}
			});
			break;*/

		default:
			break;
		}
	}

	private void dialog_pop_up_tambah_uraian() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_pop_up_perincian_riil);
		edit_dialog_uraian 	= dialog
				.findViewById(R.id.edit_dialog_uraian);
		edit_textjml_riil 	= dialog
				.findViewById(R.id.edit_textjml_riil);
		btn_upload_image 	= dialog.findViewById(R.id.btn_upload_image);
		btn_camera 			= dialog.findViewById(R.id.btn_camera);
		imgPreview 			= dialog.findViewById(R.id.imgPreview);
		TextView judul 		= dialog.findViewById(R.id.judul);
		Button btn_cncl 	= dialog.findViewById(R.id.btn_cncl);
		Button btn_simpan 	= dialog.findViewById(R.id.btn_simpan);

		path = dialog.findViewById(R.id.path);
		judul.setText("Pengisian Uraian Rincian Biaya");
		dialog.show();

		btn_upload_image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				intent.putExtra("image for", "1");
				startActivityForResult(Intent.createChooser(intent,
						"Pilih Gambar Bukti Transaksi"), REQUEST_CHOOSER_1);
				btn_upload_image.setText("Ambil Gambar Dari Galery");
				btn_camera.setText("Ambil Gambar Dari Kamera");
				imgPreview.setVisibility(View.GONE);
				path.setText("");
			}
		});
		btn_camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// capture picture
				btn_upload_image.setText("Ambil Gambar Dari Galery");
				btn_camera.setText("Ambil Gambar Dari Kamera");
				imgPreview.setVisibility(View.GONE);
				path.setText("");
				captureImage();
			}
		});

		btn_simpan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (edit_dialog_uraian.getText().toString().isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Data Wajib Diisi", Toast.LENGTH_SHORT).show();
				} else if (edit_textjml_riil.getText().toString().isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Data Wajib Diisi", Toast.LENGTH_SHORT).show();
				} else if (path.getText().toString().isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Anda Belum Melakukan Upload Bukti",
							Toast.LENGTH_SHORT).show();
					// PROSES SIMPAN DATA APABILA TIDAK UPLOAD FOTO
				} else {
					if (btn_upload_image.getText().toString()
							.contains("Ambil Gambar Dari Galery")) {
						dialog.dismiss();
						tombol_upload_camera(); // PROSES SIMPAN DATA APABILA
												// ADA FILE UPLOAD FOTO
					} else if (btn_camera.getText().toString()
							.contains("Ambil Gambar Dari Kamera")) {
						dialog.dismiss();
						tombol_upload_galery(); // PROSES SIMPAN DATA APABILA
												// ADA FILE UPLOAD FOTO
					}

				}
			}
		});

		btn_cncl.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		if (!isDeviceSupportCamera()) {
			Toast.makeText(getApplicationContext(),
					"Sorry! Your device doesn't support camera",
					Toast.LENGTH_LONG).show();
			// will close the app if the device does't have camera
			finish();
		}

	}

	private boolean isDeviceSupportCamera() {
        // this device has a camera
        // no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
	}

	/**
	 * Launching camera app to capture image
	 */
	private void captureImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

		// start the image capture Intent
		startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
	}

	/**
	 * Creating file uri to store image/video
	 */
	public Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/**
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// save file url in bundle as it will be null on screen orientation
		// changes
		outState.putParcelable("file_uri", fileUri);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		// get the file url
		fileUri = savedInstanceState.getParcelable("file_uri");
	}

	@RequiresApi(api = Build.VERSION_CODES.KITKAT)
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
			case REQUEST_CHOOSER_1:
				if (resultCode == RESULT_OK) {
					imgPreview.setVisibility(View.VISIBLE);
					final Uri uri1 = data.getData();
					final String path1 = FileUtils.getPath(this, uri1);

					btn_upload_image.setText(path1);
					path.setText(path1);

					BitmapFactory.Options options = new BitmapFactory.Options();
					// downsizing image as it throws OutOfMemory Exception for larger
					// images
					options.inSampleSize = 4;
					final Bitmap bitmap = BitmapFactory.decodeFile(path1, options);

					imgPreview.setImageBitmap(bitmap);
				}
				break;
			case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
				if (resultCode == RESULT_OK) {

					// successfully captured the image
					// launching upload activity
					// launchUploadActivity(true);
					previewCapturedImage();

					final String nm_dir = fileUri.getPath();// BERFUNGSI MENGAMBIL
					// LOKASI PATH
					path.setText(nm_dir);
					btn_camera.setText(nm_dir);
					imgPreview.setVisibility(View.VISIBLE);
				} else if (resultCode == RESULT_CANCELED) {

					// user cancelled Image capture
					Toast.makeText(getApplicationContext(),
							"Batal Mengambil Gambar", Toast.LENGTH_SHORT).show();

				} else {
					// failed to capture image
					Toast.makeText(getApplicationContext(),
									"Maaf, Gagal Mengambil Gambar", Toast.LENGTH_SHORT)
							.show();
				}
				break;

		}
	}

	private void previewCapturedImage() {
		try {

			BitmapFactory.Options options = new BitmapFactory.Options();
			// downsizing image as it throws OutOfMemory Exception for larger
			// images
			options.inSampleSize = 8;
			final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
					options);

			imgPreview.setImageBitmap(bitmap);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	private void launchUploadActivity(boolean isImage) { // INI BERFUNGSI JIKA
															// CLASS JAVA NYA
															// TIDAK JADI SATU
		// Intent i = new Intent(Laporan_Rincian_Biaya_Perj_Dinas.this,
		// UploadActivity.class);
		// i.putExtra("filePath", fileUri.getPath());
		// i.putExtra("isImage", isImage);
		// startActivity(i);
	}

	private static File getOutputMediaFile(int type) {

		// External sdcard location
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
						Koneksi.IMAGE_DIRECTORY_NAME);

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(TAG, "Oops! Failed create " + Koneksi.IMAGE_DIRECTORY_NAME
						+ " directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
				Locale.getDefault()).format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "Bukti_" + timeStamp + ".jpg");
			// } else if (type == MEDIA_TYPE_VIDEO) {
			// mediaFile = new File(mediaStorageDir.getPath() + File.separator
			// + "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}

	public void tombol_upload_galery() {
		final String path1 = btn_upload_image.getText().toString();

		if (FileUtils.isLocal(path1)) {

			File file = new File(path1);
		} else if (path1 == null) {
			File file = new File(path1);
		}

		dialog = ProgressDialog.show(Laporan_Rincian_Biaya_Perj_Dinas.this, "",
				"Tunggu Sebentar Masih Dalam Proses Upload Gambar...", true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								Laporan_Rincian_Biaya_Perj_Dinas.this,
								"Tunggu Sebentar Masih Dalam Proses Upload ...",
								Toast.LENGTH_LONG).show();
					}
				});

				int response = 0;

				String uraian_1 = edit_dialog_uraian.getText().toString();
				String jml = edit_textjml_riil.getText().toString();
				String no_sppd = edit_lamp_sppd.getText().toString();
				String ambilnip = nama_lokal.getText().toString();
				String tgl_buat = edit_tgl_lamp.getText().toString(); // MASIH
																		// ERROR

				String ambil = path.getText().toString();

				response = uploadFile(path1, uraian_1, jml, no_sppd, ambilnip,
						tgl_buat, ambil);

				System.out.println("RES : " + response);

			}
		}).start();
	}

	public void tombol_upload_camera() {
		final String path1 = btn_camera.getText().toString();

		if (FileUtils.isLocal(path1)) {

			File file = new File(path1);
		} else if (path1 == null) {
			File file = new File(path1);
		}

		dialog = ProgressDialog.show(Laporan_Rincian_Biaya_Perj_Dinas.this, "",
				"Tunggu Sebentar Masih Dalam Proses Simpan...", true);

		new Thread(new Runnable() {
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(
								Laporan_Rincian_Biaya_Perj_Dinas.this,
								"Tunggu Sebentar Masih Dalam Proses Upload ...",
								Toast.LENGTH_LONG).show();
					}
				});

				int response = 0;

				String uraian_1 = edit_dialog_uraian.getText().toString();
				String jml = edit_textjml_riil.getText().toString();
				String no_sppd = edit_lamp_sppd.getText().toString();
				String ambilnip = nama_lokal.getText().toString();
				String tgl_buat = edit_tgl_lamp.getText().toString(); // MASIH
																		// ERROR

				String ambil = path.getText().toString();

				response = uploadFile(path1, uraian_1, jml, no_sppd, ambilnip,
						tgl_buat, ambil);

				System.out.println("RES : " + response);

			}
		}).start();
	}

	// UPLOAD FILE MELALUI GALERY
	public int uploadFile(String url_simpan_image, String uraian, String jml,
			String no_sppd, String ambilnip, String tgl_buat, String ambil_path) {
		// IP komputer server

		String upLoadServerUri = "http://sppdrssm.rssoedonomadiun.co.id/sppd_rssm_apk/server_upload.php";
		String fileName = url_simpan_image;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1024 * 1024;
		File sourceFile = new File(url_simpan_image);

		if (!sourceFile.isFile()) {
			Log.e("uploadFile", "Source File Does not exist");
			return 0;
		}
		try {
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(upLoadServerUri);
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName);
			dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			// untuk parameter uraian
			dos.writeBytes("Content-Disposition: form-data; name=\"uraian\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(uraian);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			// untuk parameter jumlah
			dos.writeBytes("Content-Disposition: form-data; name=\"jml\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(jml);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			// untuk parameter no_sppd
			dos.writeBytes("Content-Disposition: form-data; name=\"no_sppd\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(no_sppd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			// untuk parameter ambilnip
			dos.writeBytes("Content-Disposition: form-data; name=\"ambilnip\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(ambilnip);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			// untuk parameter ambilnip
			dos.writeBytes("Content-Disposition: form-data; name=\"tgl_buat\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(tgl_buat);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			// untuk parameter ambilnip
			dos.writeBytes("Content-Disposition: form-data; name=\"ambil_path\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes(ambil_path);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + lineEnd);

			dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
					+ fileName + "\"" + lineEnd);
			dos.writeBytes(lineEnd);
			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			// read file and write it into form...'
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {
				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
			}

			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			if (serverResponseCode == 200) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
								"Upload Berhasil.", Toast.LENGTH_SHORT).show();

						function_parse_uraian();
					}
				});
			} else if (serverResponseCode > 200) {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
								"Upload Gagal.", Toast.LENGTH_SHORT).show();
						Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
								"Pastikan Koneksi Lancar", Toast.LENGTH_SHORT)
								.show();
						// function_parse_uraian();
					}
				});
			}

			// close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (MalformedURLException ex) {
			dialog.dismiss();
			ex.printStackTrace();
			Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
					"MalformedURLException", Toast.LENGTH_SHORT).show();
			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);

		} catch (Exception e) {
			dialog.dismiss();
			e.printStackTrace();
			Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
					"Exception : " + e.getMessage(), Toast.LENGTH_SHORT).show();

			Log.e("Upload file",
					"Exception : " + e.getMessage(), e);
		}
		dialog.dismiss();
		return serverResponseCode;

	}

	public void function_parse_uraian() {
		String ambil_uraian = edit_dialog_uraian.getText().toString();
		String ambil_jml = edit_textjml_riil.getText().toString();
		String ambil_nama_file = path.getText().toString();

		if (ambil_uraian.isEmpty() || ambil_jml.isEmpty()) {
			Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
					"Data Wajib Diisi", Toast.LENGTH_LONG).show();
		} else {
			dialog.dismiss();
			if (edit_rincian_1.getText().toString().isEmpty()) {
				edit_rincian_1.setText(ambil_uraian);
				edit_jml_rincian_1.setText(ambil_jml);
				btn_upload_ket_1.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().isEmpty()) {
				edit_rincian_2.setText(ambil_uraian);
				edit_jml_rincian_2.setText(ambil_jml);
				btn_upload_ket_2.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().isEmpty()) {
				edit_rincian_3.setText(ambil_uraian);
				edit_jml_rincian_3.setText(ambil_jml);
				btn_upload_ket_3.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().isEmpty()) {
				edit_rincian_4.setText(ambil_uraian);
				edit_jml_rincian_4.setText(ambil_jml);
				btn_upload_ket_4.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().isEmpty()) {
				edit_rincian_5.setText(ambil_uraian);
				edit_jml_rincian_5.setText(ambil_jml);
				btn_upload_ket_5.setText(ambil_nama_file);
				function_jumlah();
			} else if (edit_rincian_1.getText().toString().length() > 1
					&& edit_rincian_2.getText().toString().length() > 1
					&& edit_rincian_3.getText().toString().length() > 1
					&& edit_rincian_4.getText().toString().length() > 1
					&& edit_rincian_5.getText().toString().length() > 1
					&& edit_rincian_6.getText().toString().isEmpty()) {
				edit_rincian_6.setText(ambil_uraian);
				edit_jml_rincian_6.setText(ambil_jml);
				btn_upload_ket_6.setText(ambil_nama_file);
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
				btn_upload_ket_7.setText(ambil_nama_file);
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
				btn_upload_ket_8.setText(ambil_nama_file);
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
				btn_upload_ket_9.setText(ambil_nama_file);
				function_jumlah();

			} else {
				edit_rincian_10.setText(ambil_uraian);
				edit_jml_rincian_10.setText(ambil_jml);
				btn_upload_ket_10.setText(ambil_nama_file);
				function_jumlah();
			}
		}
	}

	// _______________________________________________________________________________________________

	// PROSES RINCIAN BIAYA

	
	/*DIMATIKAN >> PERTANYAAN MENUJU FORM RIIL 
	private void pertanyaan_menuju_form_riil() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Apakah Anda Ingin Menuju Ke Form Pengeluaran Riil Anda ???");
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
				finish();

				String nip = nip_lokal.getText().toString();
				String nama = nama_lokal.getText().toString();
				String jab = jab_lokal.getText().toString();
				String sppd = edit_lamp_sppd.getText().toString();
				String tgl_sppd = edit_tgl_lamp.getText().toString();
				String cek_status_riil = status_riil.getText().toString();
				String cek_status_rincian_biaya = "SUDAH";
				Intent i = null;

				i = new Intent(Laporan_Rincian_Biaya_Perj_Dinas.this,
						Laporan_Riil.class);

				Bundle Bundle = new Bundle();
				Bundle.putString("nama", nip);
				Bundle.putString("nip", nama);
				Bundle.putString("jab", jab);
				Bundle.putString("sppd", sppd);
				Bundle.putString("tgl_sppd", tgl_sppd);
				Bundle.putString("status_riil", cek_status_riil);
				Bundle.putString("status_biaya", cek_status_rincian_biaya);

				i.putExtras(Bundle);
				startActivity(i);
				new Simpan_Laporan_Rincian().execute();
			}
		});
		ad.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
				finish();
				dialog.dismiss();

			}
		});
		ad.show();
	}
	*/
	private void informasi(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);
		ad.setCancelable(false);
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		
		ad.show();
	}
	
	private void informasi_keluar(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);
		ad.setCancelable(false);
		ad.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
				finish();
				dialog.dismiss();
			}
		});
		
		ad.show();
	}
	private void pertanyaan_simpan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage("Pastikan Data yang Anda Buat Benar ...\n\nSimpan Data ?");
		ad.setCancelable(false);
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if ((infolagi12.getText().toString() == "0")
						|| (infolagi12.getText().toString().isEmpty())) {
					String pesan = "Anda Belum Membuat Perincian Biaya";
					informasi(pesan);
				} else {
					// pertanyaan_menuju_form_riil();
					new Simpan_Laporan_Rincian().execute();
				}

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

	/*public class Simpan_Laporan_Rincian extends AsyncTask<String, String, String> {

		*//**
		 * saat setelah tekan tombol registrasi tunjukanlah progressBar kepada
		 * *pengguna agar ia tahu aplikasi sedang apa
		 * *//*
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog_lainnya = new ProgressDialog(
					Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog_lainnya.setMessage("Loading ...");
			dialog_lainnya.setIndeterminate(false);
			dialog_lainnya.setCancelable(false);
			dialog_lainnya.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;

			String ambil_nomor_sppd = edit_lamp_sppd.getText().toString()
					.trim();
			String nip = nama_lokal.getText().toString().trim();

			String rincian_biaya_default = edit_rincian_1.getText().toString()
					.trim();
			String rincian_biaya_2 = edit_rincian_2.getText().toString().trim();
			String rincian_biaya_3 = edit_rincian_3.getText().toString().trim();
			String rincian_biaya_4 = edit_rincian_4.getText().toString().trim();
			String rincian_biaya_5 = edit_rincian_5.getText().toString().trim();
			String rincian_biaya_6 = edit_rincian_6.getText().toString().trim();
			String rincian_biaya_7 = edit_rincian_7.getText().toString().trim();
			String rincian_biaya_8 = edit_rincian_8.getText().toString().trim();
			String rincian_biaya_9 = edit_rincian_9.getText().toString().trim();
			String rincian_biaya_10 = edit_rincian_10.getText().toString().trim();

			String jml_biaya_default = edit_jml_rincian_1.getText().toString().trim();
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

			String ambil_tgl_pembuat = edit_tgl_telah_diterima.getText()
					.toString().trim();
			String grand_total = infolagi12.getText().toString().trim();

			try {
				// Cocokan parameternya yah 'username ke username dan
				// password ke password
				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("ambil_nomor_sppd",
						ambil_nomor_sppd));
				parameterNya.add(new BasicNameValuePair("nip", nip));
				parameterNya.add(new BasicNameValuePair(
						"rincian_biaya_default", rincian_biaya_default));
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
				// kirim data dari user ke script di server
				JSONObject jsonObjectNya = ambil_classJSONParser
						.makeHttpRequest(Koneksi.simpan_update_data_rincian, "POST",
								parameterNya);

				// json response-nya
				Log.d("Try Again", jsonObjectNya.toString());

				// json berhasil
				berhasil = jsonObjectNya.getInt(TAG_BERHASIL);
				if (berhasil == 1) {
					Log.d("Sukses !!!", jsonObjectNya.toString());
					// finish();

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
			dialog_lainnya.dismiss();
			if (url_registrasi_nya != null) {
				informasi_keluar(url_registrasi_nya);
				//Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
					//	url_registrasi_nya, Toast.LENGTH_LONG).show();
				//pertanyaan_menuju_form_riil();
			}

		}

	}*/

	public class Simpan_Laporan_Rincian extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog_lainnya = new ProgressDialog(
					Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog_lainnya.setMessage("Loading ...");
			dialog_lainnya.setCancelable(false);
			dialog_lainnya.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(String... args) {

			String ambil_nomor_sppd = edit_lamp_sppd.getText().toString().trim();
			String nip = nama_lokal.getText().toString().trim();

			try {
				HashMap<String, String> params = new HashMap<>();

				params.put("ambil_nomor_sppd", ambil_nomor_sppd);
				params.put("nip", nip);

				params.put("rincian_biaya_default", edit_rincian_1.getText().toString().trim());
				params.put("rincian_biaya_2", edit_rincian_2.getText().toString().trim());
				params.put("rincian_biaya_3", edit_rincian_3.getText().toString().trim());
				params.put("rincian_biaya_4", edit_rincian_4.getText().toString().trim());
				params.put("rincian_biaya_5", edit_rincian_5.getText().toString().trim());
				params.put("rincian_biaya_6", edit_rincian_6.getText().toString().trim());
				params.put("rincian_biaya_7", edit_rincian_7.getText().toString().trim());
				params.put("rincian_biaya_8", edit_rincian_8.getText().toString().trim());
				params.put("rincian_biaya_9", edit_rincian_9.getText().toString().trim());
				params.put("rincian_biaya_10", edit_rincian_10.getText().toString().trim());

				params.put("jml_biaya_default", edit_jml_rincian_1.getText().toString().trim());
				params.put("jml_biaya_2", edit_jml_rincian_2.getText().toString().trim());
				params.put("jml_biaya_3", edit_jml_rincian_3.getText().toString().trim());
				params.put("jml_biaya_4", edit_jml_rincian_4.getText().toString().trim());
				params.put("jml_biaya_5", edit_jml_rincian_5.getText().toString().trim());
				params.put("jml_biaya_6", edit_jml_rincian_6.getText().toString().trim());
				params.put("jml_biaya_7", edit_jml_rincian_7.getText().toString().trim());
				params.put("jml_biaya_8", edit_jml_rincian_8.getText().toString().trim());
				params.put("jml_biaya_9", edit_jml_rincian_9.getText().toString().trim());
				params.put("jml_biaya_10", edit_jml_rincian_10.getText().toString().trim());

				params.put("tgl_pembuatan_rincian",
						edit_tgl_telah_diterima.getText().toString().trim());
				params.put("grand_total",
						infolagi12.getText().toString().trim());

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.simpan_update_data_rincian,
						params
				);

				if (response == null) {
					return "Tidak ada respon dari server";
				}

				Log.d("RESPON SERVER", response);

				JSONObject json = new JSONObject(response);
				return json.getString(TAG_PESAN);

			} catch (Exception e) {
				e.printStackTrace();
				return "Terjadi kesalahan saat menyimpan data";
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			dialog_lainnya.dismiss();

			if (hasil != null) {
				informasi_keluar(hasil);
			}
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

	private final DatePickerDialog.OnDateSetListener tgl_ttd_petugas_pembuatlaporan = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			tahun = year;
			bulan = monthOfYear;
			hari = dayOfMonth;

			String koncersibulan = LPad(hari + "", "0", 2) + "-"
					+ varBulan[bulan] + "-" + tahun;

			edit_tgl_telah_diterima.setText(koncersibulan);

		}
	};

	private static String LPad(String schar, String spad, int len) {
		String sret = schar;
		for (int i = sret.length(); i < len; i++) {
			sret = spad + sret;
		}
		return sret;
	}

	private final Runnable runnable = new Runnable() {

		@SuppressLint("SimpleDateFormat")
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Calendar c1 = Calendar.getInstance();
			
			
			
			SimpleDateFormat tgl_skrng = new SimpleDateFormat("d MMM yyyy");

			String strdate_tgl = tgl_skrng.format(c1.getTime());
			
			
			
			edit_tgl_telah_diterima.setText(strdate_tgl);

			handler.postDelayed(this, 1000);
		}

	};

	@Override
	public void onBackPressed() {
		String pesan = "Data anda akan ter HAPUS dari sistem ketika keluar, Klik Tombol CENTANG untuk Menyimpan Daftar Perincian Anda";
		String pesan2 = "Batal Perubahan Daftar Pengeluaran ?";
		String cek_status_rincian = status_rincian_biaya.getText().toString();
		
		if (cek_status_rincian.contains("SUDAH")){
			infodialogback_sudah(pesan2);
		}else if(cek_status_rincian.contains("BELUM")){
			infodialogback_belum(pesan);
		}
		
	}

	private void infodialogback_belum(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);
		ad.setCancelable(false);
		ad.setPositiveButton("Ya", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				new Hapus_Total_Rincian().execute();
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
	private void infodialogback_sudah(String pesan) {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Informasi");
		ad.setMessage(pesan);		
		ad.setPositiveButton("Keluar", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
				Laporan_Rincian_Biaya_Perj_Dinas.this.finish();
				finish();
			}
		});	
		ad.show();
	}

	/*public class Hapus_Total_Rincian extends AsyncTask<String, String, String> {
		boolean failure = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog_lainnya = new ProgressDialog(
					Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog_lainnya.setMessage("Sedang Dalam Proses Penghapusan Total...");
			dialog_lainnya.setIndeterminate(false);
			dialog_lainnya.setCancelable(false);
			dialog_lainnya.show();
		}

		@Override
		protected String doInBackground(String... args) {
			// priksa TAG_BERHASIL
			int berhasil;

			String ambil_nomor_sppd = edit_lamp_sppd.getText().toString()
					.trim();
			String nip = nama_lokal.getText().toString().trim();

			try {
				// Cocokan parameternya yah 'username ke username dan
				// password ke password
				List<NameValuePair> parameterNya = new ArrayList<NameValuePair>();
				parameterNya.add(new BasicNameValuePair("ambil_nomor_sppd",
						ambil_nomor_sppd));
				parameterNya.add(new BasicNameValuePair("nip", nip));

				Log.d("Request ke server!", "dimulai");
				JSONParser ambil_classJSONParser = new JSONParser();
				JSONObject jsonObjectNya = ambil_classJSONParser
						.makeHttpRequest(Koneksi.hapus_data_rincian, "POST", parameterNya);
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
			// matikan progressBar-nya setelah selesai di gunakan
			dialog_lainnya.dismiss();
			if (url_registrasi_nya != null) {
				Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
						url_registrasi_nya, Toast.LENGTH_LONG).show();
			}

		}

	}*/
	public class Hapus_Total_Rincian extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog_lainnya = new ProgressDialog(
					Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog_lainnya.setMessage("Sedang Dalam Proses Penghapusan Total...");
			dialog_lainnya.setCancelable(false);
			dialog_lainnya.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(String... args) {

			String ambil_nomor_sppd = edit_lamp_sppd.getText().toString().trim();
			String nip = nama_lokal.getText().toString().trim();

			try {
				// 🔹 PARAM POST
				HashMap<String, String> params = new HashMap<>();
				params.put("ambil_nomor_sppd", ambil_nomor_sppd);
				params.put("nip", nip);

				// 🔹 REQUEST
				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.hapus_data_rincian,
						params
				);

				if (response == null) {
					return "Tidak ada respon dari server";
				}

				Log.d("RESPON SERVER", response);

				// 🔹 PARSE JSON
				JSONObject json = new JSONObject(response);
				int berhasil = json.getInt(TAG_BERHASIL);

				return json.getString(TAG_PESAN);

			} catch (Exception e) {
				e.printStackTrace();
				return "Terjadi kesalahan saat menghapus data";
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			dialog_lainnya.dismiss();

			Toast.makeText(
					Laporan_Rincian_Biaya_Perj_Dinas.this,
					hasil,
					Toast.LENGTH_LONG
			).show();

			// tutup activity jika sukses
			if (hasil != null && hasil.toLowerCase().contains("sukses")) {
				finish();
			}
		}
	}

	// --------------------------------------------------------------------------------------------------------------------------------
	public void show_hide_button_upload() {

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

		// --------------------------------------------------------------------------------------------------------------------------------
	}

	public String info_terbilang(int value) {

		String[] bilangan = { "", "satu", "dua", "tiga", "empat", "lima",
				"enam", "tujuh", "delapan", "sembilan", "sepuluh", "sebelas" };
		String temp = " ";

		if (value < 12) {

			temp = " " + bilangan[value];

		} else if (value < 20) {

			temp = info_terbilang(value - 10) + " belas";

		} else if (value < 100) {

			temp = info_terbilang(value / 10) + " puluh"
					+ info_terbilang(value % 10);

		} else if (value < 200) {

			temp = " seratus" + info_terbilang(value - 100);

		} else if (value < 1000) {

			temp = info_terbilang(value / 100) + " ratus"
					+ info_terbilang(value % 100);

		} else if (value < 2000) {

			temp = "seribu" + info_terbilang(value - 1000);
			
		} else if (value < 1000000) {

			temp = info_terbilang(value / 1000) + " ribu"
					+ info_terbilang(value % 1000);
			
		}else if (value < 2000000) {

			temp = "juta" + info_terbilang(value - 2000);
		}

		return temp;
	}

	public class HapusRincianTask extends AsyncTask<Void, Void, String> {

		private final EditText etRincian;
        private final EditText etJumlah;
		private final Button btnUpload;
		private ProgressDialog dialog;

		public HapusRincianTask(EditText rincian,
								EditText jumlah,
								Button btnUpload) {
			this.etRincian = rincian;
			this.etJumlah = jumlah;
			this.btnUpload = btnUpload;
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog.setMessage("Sedang Dalam Proses Hapus...");
			dialog.setCancelable(false);
			dialog.show();
		}

		@RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
		protected String doInBackground(Void... voids) {

			try {
				HashMap<String, String> params = new HashMap<>();
				params.put("no_sppd", edit_lamp_sppd.getText().toString().trim());
				params.put("uraian", etRincian.getText().toString().trim());

				Java_Connection jc = new Java_Connection();
				String response = jc.sendPostRequest(
						Koneksi.hapus_data_per_uraian_laporan,
						params
				);

				if (response == null) {
					return "Tidak ada respon dari server";
				}

				Log.d("RESPON HAPUS", response);

				JSONObject json = new JSONObject(response);
				return json.getString(TAG_PESAN);

			} catch (Exception e) {
				e.printStackTrace();
				return "Terjadi kesalahan";
			}
		}

		@Override
		protected void onPostExecute(String hasil) {
			dialog.dismiss();

			if (hasil != null) {
				Toast.makeText(
						Laporan_Rincian_Biaya_Perj_Dinas.this,
						hasil,
						Toast.LENGTH_LONG
				).show();

				etRincian.setText("");
				etJumlah.setText("");
				btnUpload.setText("Bukti");

				function_jumlah();
			}
		}
	}

	private void konfirmasiHapus(
			EditText rincian,
			EditText jumlah,
			Button btnUpload) {

		String isi = rincian.getText().toString().trim();

		if (isi.isEmpty()) {
			Toast.makeText(this, "Tidak Ada Data", Toast.LENGTH_LONG).show();
			return;
		}

		new AlertDialog.Builder(this)
				.setTitle("Informasi")
				.setMessage(
						"Rincian : " + isi +
								"\n\nApakah Data Akan Di Hapus ???"
				)
				.setPositiveButton("Hapus", (d, w) ->
						new HapusRincianTask(
								rincian,
								jumlah,
								btnUpload
						).execute()
				)
				.setNegativeButton("Tidak", (d, w) -> d.dismiss())
				.show();
	}

	/*private void pertanyaan_hapus_button_satu() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_1.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_1.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Satu().execute();
				}
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
	public class Hapus_File_Satu extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;

			String no_sppd = edit_lamp_sppd.getText().toString();
			String uraian = edit_rincian_1.getText().toString();

			try {

				List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
				namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
				namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

				Log.d("requestNya!", "dimulai");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
						Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
				Log.d("Coba login", jsonObjectNya.toString());
				jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

				if (jikaSukses == 1) {
					Log.d("Login_nya Sukses!", jsonObjectNya.toString());
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					Log.d("Login_nya Gagal!",
							jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_PESAN);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String urlFileNya) {
			dialog.dismiss();
			if (urlFileNya != null) {
				Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
						urlFileNya, Toast.LENGTH_LONG).show();
				edit_rincian_1.setText("");
				edit_jml_rincian_1.setText("");
				btn_upload_ket_1.setText("Bukti");
				function_jumlah();
			}

		}

	}
	private void pertanyaan_hapus_button_dua() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_2.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_2.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Dua().execute();
				}
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
	public class Hapus_File_Dua extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
			dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
			dialog.setIndeterminate(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... args) {

			int jikaSukses;

			String no_sppd = edit_lamp_sppd.getText().toString();
			String uraian = edit_rincian_2.getText().toString();

			try {

				List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
				namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
				namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

				Log.d("requestNya!", "dimulai");
				JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
						Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
				Log.d("Coba login", jsonObjectNya.toString());
				jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

				if (jikaSukses == 1) {
					Log.d("Login_nya Sukses!", jsonObjectNya.toString());
					return jsonObjectNya.getString(TAG_PESAN);
				} else {
					Log.d("Login_nya Gagal!",
							jsonObjectNya.getString(TAG_PESAN));
					return jsonObjectNya.getString(TAG_PESAN);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;

		}

		@Override
		protected void onPostExecute(String urlFileNya) {
			dialog.dismiss();
			if (urlFileNya != null) {
				Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
						urlFileNya, Toast.LENGTH_LONG).show();
				edit_rincian_2.setText("");
				edit_jml_rincian_2.setText("");
				btn_upload_ket_2.setText("Bukti");
				function_jumlah();
			}

		}

	}
	private void pertanyaan_hapus_button_tiga() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_3.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_3.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
                    new Hapus_File_Tiga().execute();
				}
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
    public class Hapus_File_Tiga extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_3.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_3.setText("");
                edit_jml_rincian_3.setText("");
                btn_upload_ket_3.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_empat() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_4.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_4.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Empat().execute();
				}
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
    public class Hapus_File_Empat extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_4.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_4.setText("");
                edit_jml_rincian_4.setText("");
                btn_upload_ket_4.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_lima() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_5.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_5.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Lima().execute();
				}
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
    public class Hapus_File_Lima extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_5.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_5.setText("");
                edit_jml_rincian_5.setText("");
                btn_upload_ket_5.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_enam() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_6.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_6.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Enam().execute();
				}
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
    public class Hapus_File_Enam extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_6.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_6.setText("");
                edit_jml_rincian_6.setText("");
                btn_upload_ket_6.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_tujuh() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_7.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_7.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Tujuh().execute();
				}
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
    public class Hapus_File_Tujuh extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_7.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_7.setText("");
                edit_jml_rincian_7.setText("");
                btn_upload_ket_7.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_delapan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_8.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_8.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					//Toast.makeText(
					//		Laporan_Rincian_Biaya_Perj_Dinas.this,
				    //			"Masih Dalam Proses\nHubungi Pihak Admin Untuk Perubahan Data",
					//		Toast.LENGTH_LONG).show();
                    new Hapus_File_Delapan().execute();
				}
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
    public class Hapus_File_Delapan extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_8.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_8.setText("");
                edit_jml_rincian_8.setText("");
                btn_upload_ket_8.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_sembilan() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_9.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_9.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Sembilan().execute();
				}
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
    public class Hapus_File_Sembilan extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_9.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_9.setText("");
                edit_jml_rincian_9.setText("");
                btn_upload_ket_9.setText("Bukti");
                function_jumlah();
            }

        }

    }
	private void pertanyaan_hapus_button_sepuluh() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		String ambil_rincian = edit_rincian_10.getText().toString();
		ad.setTitle("Informasi");
		ad.setMessage("Rincian : " + ambil_rincian
				+ "\n\nApakah Data Akan Di Hapus ???");
		ad.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String ambil_rincian = edit_rincian_10.getText().toString();
				if (ambil_rincian.isEmpty()) {
					Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
							"Tidak Ada Data", Toast.LENGTH_LONG).show();
				} else {
					new Hapus_File_Sepuluh().execute();
				}
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
    public class Hapus_File_Sepuluh extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Laporan_Rincian_Biaya_Perj_Dinas.this);
            dialog.setMessage("Sedang Dalam Proses Hapus... !!!");
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... args) {

            int jikaSukses;

            String no_sppd = edit_lamp_sppd.getText().toString();
            String uraian = edit_rincian_10.getText().toString();

            try {

                List<NameValuePair> namaDanPassword = new ArrayList<NameValuePair>();
                namaDanPassword.add(new BasicNameValuePair("no_sppd", no_sppd));
                namaDanPassword.add(new BasicNameValuePair("uraian", uraian));

                Log.d("requestNya!", "dimulai");
                JSONObject jsonObjectNya = classJsonParser.makeHttpRequest(
                        Koneksi.hapus_data_per_uraian_laporan, "POST", namaDanPassword);
                Log.d("Coba login", jsonObjectNya.toString());
                jikaSukses = jsonObjectNya.getInt(TAG_BERHASIL);

                if (jikaSukses == 1) {
                    Log.d("Login_nya Sukses!", jsonObjectNya.toString());
                    return jsonObjectNya.getString(TAG_PESAN);
                } else {
                    Log.d("Login_nya Gagal!",
                            jsonObjectNya.getString(TAG_PESAN));
                    return jsonObjectNya.getString(TAG_PESAN);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(String urlFileNya) {
            dialog.dismiss();
            if (urlFileNya != null) {
                Toast.makeText(Laporan_Rincian_Biaya_Perj_Dinas.this,
                        urlFileNya, Toast.LENGTH_LONG).show();
                edit_rincian_10.setText("");
                edit_jml_rincian_10.setText("");
                btn_upload_ket_10.setText("Bukti");
                function_jumlah();
            }

        }
    }*/
}
